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

int vilica[5] = {1,1,1,1,1};
pthread_mutex_t monitor;
char filozof[5+1] = "OOOOO";
pthread_cond_t uvjet[5];

void jesti(int n)
{
    pthread_mutex_lock(&monitor);
        filozof[n] = 'o';
        while(vilica[n] == 0 || vilica[(n+1)%5] == 0){
            pthread_cond_wait(&uvjet[n], &monitor);
        }
        vilica[n] = vilica[(n+1)%5] = 0;
        filozof[n] = 'X';
        printf("%s (%d)\n", filozof, n+1);
    pthread_mutex_unlock(&monitor);
    sleep(2);   /*jede*/
    pthread_mutex_lock(&monitor);
        filozof[n] = 'O';
        vilica[n] = vilica[(n+1)%5] = 1;
        if(n==0){
                pthread_cond_signal(&uvjet[4]);
        }else{
                pthread_cond_signal(&uvjet[(n-1)%5]);
        }
        printf("%s (%d)\n", filozof, n+1);
    pthread_mutex_unlock(&monitor);
}

void *Filozof(void *n)
{
    while(1){
        sleep(3); /*misli*/
        jesti(*((int*)n));
    }
}

int main()
{
    int i;
    pthread_t t_id[5];
    pthread_mutex_init(&monitor, NULL);
    pthread_cond_init(uvjet, NULL);
    for(i=0; i<5; i++){
        if(pthread_create(&t_id[i], NULL, (void*)Filozof, (void*)&i) != 0){
            printf("Greska kod stvaranja dretvi.\n");
            exit(1);
        }
        sleep(1);
    }
    for(i=0; i<5;i++){
        pthread_join(t_id[i], NULL);
    }
    return 0;
}
