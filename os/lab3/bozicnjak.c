#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <stdlib.h>
#include <values.h>
#include <sys/sem.h>
#include <pthread.h>
#include <string.h>
#include <unistd.h>

#define MAXDRETVI 30
#define ISPITAJ -1
#define POSTAVI 1

/*OSemi*/
#define SOBOVI 0
#define KONZULTACIJE 1
#define GODISNJI 2
/*BSemi*/
pthread_mutex_t djedbozicnjak, K, godisnji;

int br_sobova;
int br_patuljaka;
int semId;

void *Djed_Bozicnjak(void)
{
    do{
        pthread_mutex_lock(&djedbozicnjak);
        pthread_mutex_lock(&K);
        if(br_sobova == 10 && br_patuljaka > 0){
            pthread_mutex_unlock(&K);
            printf("Raznosenje poklona...\n");
            sleep(2); /*ukrcaj poklone i raznosi*/
            pthread_mutex_lock(&K);
            SemSetVal(GODISNJI, 10); /*pusti sobove na godisnji*/
            SemSetVal(SOBOVI, 10);
            br_sobova = 0;
        }
        if(br_sobova == 10){
            pthread_mutex_unlock(&K);
            printf("Djed hrani sobove...\n");
            sleep(2); /*nahrani sobove*/
            pthread_mutex_lock(&K);
        }
        while(br_patuljaka >= 3){
            pthread_mutex_unlock(&K);
            printf("Konzultacije kod Djeda...\n");
            sleep(2); /*konzultacije*/
            pthread_mutex_lock(&K);
            SemSetVal(KONZULTACIJE, 3);
            br_patuljaka -= 3;
        }
        pthread_mutex_unlock(&K);
    }while(1);
}

void *patuljak(void)
{
    pthread_mutex_lock(&K);
    br_patuljaka++;
    printf("Stvoren patuljak br. %d\n", br_patuljaka);
    if(br_patuljaka == 3){
        pthread_mutex_unlock(&djedbozicnjak);
    }
    pthread_mutex_unlock(&K);
    SemOp(KONZULTACIJE, ISPITAJ);
}

void *sob(void)
{
    pthread_mutex_lock(&K);
    br_sobova++;
    printf("Stvoren sob br. %d\n", br_sobova);
    if(br_sobova == 10){
        pthread_mutex_unlock(&djedbozicnjak); /*probudi djeda*/
        /*budi nahranjen i cekaj pred vratima*/
        pthread_mutex_unlock(&K);
        /*raznosi poklone kad treba*/
        SemOp(GODISNJI, ISPITAJ);
        pthread_exit(NULL); /*idi na godisnji*/
    }
    pthread_mutex_unlock(&K);
}
/*----------------------------------------------------*/
int SemSetVal(int SemNum, int SemVal)
{
    return !semctl(semId, SemNum, SETVAL, SemVal);
}
/*----------------------------------------------------*/
int SemOp(int SemNum, int SemOp)
{
    struct sembuf SemBuf;
    SemBuf.sem_num = SemNum;
    SemBuf.sem_op = SemOp;
    SemBuf.sem_flg = 0;
    return semop(semId, &SemBuf, 1);
}

int main()
{
    int i=1;
    float vjs, vjp;
    pthread_t t_id[MAXDRETVI];
    semId = semget(IPC_PRIVATE, 3, 0600);
    if(!SemSetVal(KONZULTACIJE, 3) || !SemSetVal(SOBOVI, 10) || !SemSetVal(GODISNJI, 0)){
            printf("Ne mogu inicijalizirati semafor!\n");
            exit(1);
    }
    pthread_mutex_init(&djedbozicnjak, NULL);
    pthread_mutex_init(&K, NULL);
    pthread_create(&t_id[0], NULL, (void*)Djed_Bozicnjak, NULL);
    pthread_join(t_id[0], NULL);
    do{
        sleep(rand()%4);
        vjs = (rand()%6);
        vjp = (rand()%7);
        if(vjs > (6/2) && br_sobova < 10){
            pthread_create(&t_id[i], NULL, (void*)sob, NULL);
            pthread_join(t_id[i], NULL);
            i++;
        }
        if(vjp > (7/2)){
            pthread_create(&t_id[i], NULL, (void*)patuljak, NULL);
            pthread_join(t_id[i], NULL);
            i++;
        }
    }while(1);
    return 0;
}
