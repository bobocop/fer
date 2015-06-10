#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <string.h>
#include <pthread.h>
#include <signal.h>
#include <semaphore.h>
#include <unistd.h>
#include <mqueue.h>
#include <fcntl.h>
#include <time.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <limits.h>

#define Q_TIMEOUT 	3
#define Q_MAX_MSGS 	10
#define Q_BUFSZ		2 * sizeof(struct msg)

struct msg {
	int job_id;
	int job_duration;
	char shm_name[NAME_MAX];
};

struct mlist {
	struct msg m;
	struct mlist* next;
};

static int quit = 0;
static pthread_mutex_t local_mutex = PTHREAD_MUTEX_INITIALIZER;
static struct mlist* local_list = NULL;
static pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
static int m_num = 0;
static int m_duration = 0;

static void save_message_in_local_list(struct msg m)
{
	struct mlist* new = (struct mlist*) malloc(sizeof(struct mlist));
	new->m = m;
	new->next = NULL;
	
	if (local_list == NULL) {
		local_list = new;
	} else {
		struct mlist* temp;
		for (temp = local_list; temp->next != NULL; temp = temp->next);
		temp->next = new;
	}
}

static struct mlist* get_first_message_from_local_list(void)
{
	struct mlist* ret = NULL;
	
	if (local_list != NULL) {
		ret = local_list;
		local_list = local_list->next;
		// deleted by worker upon completition
	}
	
	return ret;
}

static void handle_signal(int signal)
{
	printf("S: signal received, closing\n");
	quit = 1;
	pthread_cond_broadcast(&cond);
}

static int process(int* data, int size) 
{
	int i, sum = 0;
	
	for (i = 0; i < size; i++) {
		sum += data[i];
		sleep(1);	// rest after hard work
	}
	
	return sum;
}
	

static void* worker(void* x)
{
	pthread_mutex_lock(&local_mutex);
	
	do {
		struct mlist* ml;
		int shm_d;
		int result;
		int* data;
		
		while ((ml  = get_first_message_from_local_list()) == NULL && quit == 0) {
			pthread_cond_wait(&cond, &local_mutex);
		}
		
		if (quit == 1) {
			break;
		}
		
		m_num--;
		m_duration -= ml->m.job_duration;
		
		printf("W: processing %d %d %s\n", ml->m.job_id, ml->m.job_duration, ml->m.shm_name);
		pthread_mutex_unlock(&local_mutex);
		
		shm_d = shm_open(ml->m.shm_name, O_RDONLY, 00600);
		data = (int*) mmap(NULL, ml->m.job_duration * sizeof(int), PROT_READ, MAP_SHARED, shm_d, 0);
		
		result = process(data, ml->m.job_duration);
		
		shm_unlink(ml->m.shm_name);
		close(shm_d);	
		
		pthread_mutex_lock(&local_mutex);
		printf("W: completed %d %d %s (result: %d)\n", ml->m.job_id, ml->m.job_duration, ml->m.shm_name, result);
		free(ml);
	} while (quit != 1);
	
	pthread_mutex_unlock(&local_mutex);
	pthread_exit(0);
}

static void schedule(int N, int M)
{
	int i;
	char q_name[NAME_MAX] = { '/' };
	mqd_t q;
	
	pthread_t* thr_id = (pthread_t*) malloc(N * sizeof(pthread_t));
	pthread_attr_t thr_attr;
	pthread_attr_init(&thr_attr);
	pthread_attr_setinheritsched(&thr_attr, PTHREAD_EXPLICIT_SCHED);
	pthread_attr_setschedpolicy(&thr_attr, SCHED_RR);
	struct sched_param prio;
	prio.sched_priority = 40;
	pthread_attr_setschedparam(&thr_attr, &prio);
	
	for (i = 0; i < N; i++) {
		if (pthread_create(&thr_id[i], &thr_attr, worker, NULL) != 0) {
			perror("Unable to create worker thread");
			exit(EXIT_FAILURE);
		}
	}
	
	struct sigaction act;
	act.sa_handler = handle_signal;
	act.sa_flags = SA_RESTART;
	sigemptyset(&act.sa_mask);
	sigaction(SIGTERM, &act, NULL);
	
	strncat(q_name, getenv("SERVER_LAB5"), NAME_MAX - 2);
	
	struct mq_attr attr;
	attr.mq_maxmsg = Q_MAX_MSGS;
	attr.mq_msgsize = sizeof(struct msg);
	q = mq_open(q_name, O_RDONLY | O_CREAT, 00600, &attr);
	
	if (q == (mqd_t) -1) {
		perror("Failed to open message queue");
		exit(EXIT_FAILURE);
	}
	
	do {
		int len;
		struct timespec ts;
		struct msg msg_buf;
		
		clock_gettime(CLOCK_REALTIME, &ts);
		ts.tv_sec += Q_TIMEOUT;
		len = mq_timedreceive(q, (char*) &msg_buf, sizeof(struct msg), NULL, &ts);
		
		if (len != -1) {
			pthread_mutex_lock(&local_mutex);
			printf("S: received %d %d %s\n", msg_buf.job_id, msg_buf.job_duration, msg_buf.shm_name);
			save_message_in_local_list(msg_buf);
			pthread_mutex_unlock(&local_mutex);
			m_num++;
			m_duration += msg_buf.job_duration;
			if (m_num >= N && m_duration >= M) {
				printf("s: waking workers\n");
				pthread_cond_broadcast(&cond);
			}
		} else {
			printf("S: timeout\n");
			if (m_num > 0) {
				printf("S: waking workers\n");
				pthread_cond_broadcast(&cond);
			}
		}
	} while (quit != 1);
	
	for (i = 0; i < N; i++) {
		pthread_join(thr_id[i], NULL);
	}
	
	mq_unlink(q_name);
	mq_close(q);
	pthread_cond_destroy(&cond);
	free(thr_id);
	printf("S: exiting, all threads closed\n");
}

int main(int argc, char** argv)
{
	int n, m;
	
	if (argc != 3) {
		printf("call: ./server N M\n");
		return 0;
	}
	
	n = strtol(argv[1], NULL, 10);
	m = strtol(argv[2], NULL, 10);
	
	struct sched_param param;
	param.sched_priority = 60;
	sched_setscheduler(0, SCHED_RR, &param);
	
	schedule(n, m);
	
	return 0;
}
