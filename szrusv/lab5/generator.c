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
#include <features.h>

#define DURATION_MIN 	2
#define DURATION_MAX 	10
#define Q_MAX_MSGS 		10

struct shared {
	pthread_mutex_t shared_mutex;
	int last_used_job_id;
};

struct msg {
	int job_id;
	int job_duration;
	char shm_name[NAME_MAX];
};

int main(int argc, char** argv)
{
	char q_name[NAME_MAX] = { '/' };
	mqd_t q;
	int shm_d;
	struct shared* sm;
	int job_id;
	int i, j, k;
	
	if (argc != 2) {
		printf("call: ./generator J\n");
		return 0;
	}
	
	struct sched_param param;
	param.sched_priority = 50;
	sched_setscheduler(0, SCHED_RR, &param);
	
	srand(time(NULL));
	
	j = strtol(argv[1], NULL, 10);
	
	strncat(q_name, getenv("SERVER_LAB5"), NAME_MAX - 2);
	
	struct mq_attr attr;
	attr.mq_maxmsg = Q_MAX_MSGS;
	attr.mq_msgsize = sizeof(struct msg);
	q = mq_open(q_name, O_RDWR | O_CREAT, 00600, &attr);
	
	shm_d = shm_open(q_name, O_RDWR | O_CREAT | O_EXCL, 00600);
	
	if (shm_d != -1) {
		if (ftruncate(shm_d, sizeof(struct shared)) == -1) {
			perror("Error while creating shared memory");
			return -1;
		}
		
		sm = (struct shared*) mmap(NULL, sizeof(struct shared), PROT_READ | PROT_WRITE, MAP_SHARED, shm_d, 0);
	
		if (sm == MAP_FAILED) {
			perror("Error while mapping shared memory");
			return -1;
		}
		
		pthread_mutex_init(&(sm->shared_mutex), NULL);
	} else {
		shm_d = shm_open(q_name, O_RDWR, 00600);
		sm = (struct shared*) mmap(NULL, sizeof(struct shared), PROT_READ | PROT_WRITE, MAP_SHARED, shm_d, 0);
		if (sm == MAP_FAILED) {
			perror("Error while mapping shared memory");
			return -1;
		}
	}
	
	pthread_mutex_lock(&(sm->shared_mutex));
	job_id = sm->last_used_job_id + 1;
	sm->last_used_job_id = job_id + j;

	shm_unlink(q_name);
	close(shm_d);
	
	for (i = 0; i < j; i++) {
		char snum[5];
		char shm2_name[NAME_MAX] = { 0 };
		int duration;
		int shm_d2;
		int* sm_job_data;
		struct msg m;
		
		snprintf(snum, 5, "%d", job_id);
		
		strcat(strcat(strcat(shm2_name, q_name), "-"), snum);	// cba to check with strncat
		duration = rand() % DURATION_MAX + DURATION_MIN;
		
		shm_d2 = shm_open(shm2_name, O_RDWR | O_CREAT, 00600);
		
		if (ftruncate(shm_d2, duration * sizeof(int)) == -1) {
			perror("Error while creating shared memory");
			return -1;
		}
		
		sm_job_data = (int *) mmap(NULL, duration * sizeof(int), PROT_READ | PROT_WRITE, MAP_SHARED, shm_d2, 0);
		
		if (sm == NULL) {
			perror("Error while mapping shared memory");
			return -1;
		}
		
		for (k = 0; k < duration; k++) {
			sm_job_data[k] = rand() % 10;
		}
		
		//shm_unlink(shm2_name);
		close(shm_d2);
		
		m.job_id = job_id;
		m.job_duration = duration;
		strcpy(m.shm_name, shm2_name);
		
		mq_send(q, (const char*) &m, sizeof(struct msg), 0);
		
		printf("G: sent %d %d %s (data: ", m.job_id, m.job_duration, m.shm_name);
		for (k = 0; k < duration; k++) {
			printf("%d ", sm_job_data[k]);
		}
		printf(")\n");
		job_id++;
	}
	
	return 0;
}	
