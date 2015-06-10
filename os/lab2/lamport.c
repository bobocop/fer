#include <stdio.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/msg.h>
#include <values.h>
#include <stdlib.h>

int brp;
int ulazid, brojid, maxid;
int *ulaz, *broj, *maxbroj;

void brisi(int sig)
{
    (void) shmdt((char *) ulaz);
    (void) shmdt((char *) broj);
    (void) shmdt((char *) maxbroj);
    (void) shmctl(ulazid, IPC_RMID, NULL);
    (void) shmctl(brojid, IPC_RMID, NULL);
    (void) shmctl(maxid, IPC_RMID, NULL);
    exit(0);
}

void udji_u_ko(int i)
{
    int j;
    ulaz[i] = 1;
    broj[i] = ++(*maxbroj);
    ulaz[i] = 0;
    for(j = 0; j < brp; j++){
        while(ulaz[j] != 0);
        while((broj[j] != 0) && ((broj[j] < broj[i]) || ((broj[j] == broj[i]) && (j < i))));
    }
}

void izadji_iz_ko(int i)
{
    broj[i] = 0;
}

void ispisi(int i, int k, int m)
{
    printf("Proces: %d, RBr: %d, K.O. br: %d, (%d/5)\n", i+1, broj[i], k, m);
}

void proces_i(int i)
{
    int k, m;
    for(k = 1; k <= 5; k++){
        udji_u_ko(i);
        for(m = 1; m <= 5; m++){
            ispisi(i, k, m);
            sleep(1);
        }
        izadji_iz_ko(i);
    }
}

int main(int argc, char *argv[])
{
    int i;
    if(argc != 2){
        printf("Neispravno pokrenut program\n");
        return -1;
    }
    brp = atoi(argv[1]);
    ulazid = shmget(IPC_PRIVATE, brp*sizeof(int), 0600);
    brojid = shmget(IPC_PRIVATE, brp*sizeof(int), 0600);
    maxid = shmget(IPC_PRIVATE, sizeof(int), 0600);
    if(ulazid  == -1 || brojid == -1 || maxid == -1){
        printf("Nema dovoljno memorije.\n");
        return -1;
    }
    maxbroj = (int *) shmat(maxid, NULL, 0);
    *maxbroj = 0;
    ulaz = (int *) shmat(ulazid, NULL, 0);
    broj = (int *) shmat(brojid, NULL, 0);
    sigset(SIGINT, brisi);
    for(i = 0; i < brp; i++){
        printf("Proces %d stvoren.\n", i+1);
        switch(fork()){
            case 0:
                    proces_i(i);
                    exit(0);
            case -1:
                    printf("Ne mogu stvoriti procese\n.");
                    return -1;
            default:
                    break;
        }
    }
    while(i--) wait(NULL);
    brisi(0);
    return 0;
}
