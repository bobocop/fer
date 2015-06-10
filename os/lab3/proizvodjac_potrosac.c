#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <stdlib.h>
#include <values.h>
#include <sys/sem.h>

#define MS_SIZ 5
#define BUF_SIZ 30
#define POSTAVI 1
#define ISPITAJ -1
#define PUN 0
#define PISI 1
#define PRAZAN 2
#define UPIS1 3
#define UPIS2 4

void proizvodjac(int);

char *m;
int *ulaz, *izlaz;
int semId, memId, ulazId, izlazId;

/*----------------------------------------------------*/
void SemGet(int n)
{
    semId = semget(IPC_PRIVATE, n, 0600);
    if(semId == -1){
        printf("Nema semafora!\n");
        exit(1);
    }
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
/*---------------------------------------------------*/
void brisi()
{
    int i;
    shmdt((char *)ulaz);
    shmdt((char *)izlaz);
    shmdt(m);
    shmctl(memId, IPC_RMID, NULL);
    shmctl(ulazId, IPC_RMID, NULL);
    shmctl(izlazId, IPC_RMID, NULL);
    for(i = 0; i < 5; i++){
        semctl(semId, i, IPC_RMID, 0);
    }
    printf("Ocistio memoriju i semafore.\n");
}
/*--------------------------------------------------*/
void stvori_proizvodjace()
{
    switch(fork()){
        case 0:
                proizvodjac(2);
                break;
        default:
                proizvodjac(1);
                break;
    }
}
/*---------------------------------------------------*/
void proizvodjac(int proId)
{
    int i = 0;
    char niz[BUF_SIZ+1];
    SemOp(UPIS1, ISPITAJ);
    printf("Unesti znakove za proizvodjaca %d\n", proId);
    fgets(niz, 50, stdin);
    SemOp(UPIS1, POSTAVI);
    if(proId == 1) SemOp(UPIS2, ISPITAJ);
    if(proId == 2) SemOp(UPIS2, POSTAVI);
    while(niz[i]){
        SemOp(PUN, ISPITAJ);
        SemOp(PISI, ISPITAJ);
        sleep(1);
        printf("PROIZVODJAC%d -> %c\n", proId, niz[i]);
        m[*ulaz] = niz[i];
        *ulaz = ((*ulaz)+1) % MS_SIZ;
        SemOp(PISI, POSTAVI);
        SemOp(PRAZAN, POSTAVI);
        if(niz[i] == 10) break;
        i++;
    }
    exit(0);
}
/*----------------------------------------------------*/
void potrosac(void)
{
    int i = 0;
    int kraj = 2;
    char niz[2*BUF_SIZ+1];
    while(kraj){
        SemOp(PRAZAN, ISPITAJ);
        printf("POTROSAC <- %c\n", m[*izlaz]);
        if(m[*izlaz] != 10){
                niz[i] = m[*izlaz];
                i++;
        }
        else{
                kraj--;
        }
        *izlaz = ((*izlaz+1)) % 5;
        SemOp(PUN, POSTAVI);
    }
    niz[i] = 0;
    printf("Primljeno je: %s\n", niz);
    brisi();
    exit(0);
}
/*----------------------------------------------------*/
/*----------------------------------------------------*/
int main()
{
    memId = shmget(IPC_PRIVATE, sizeof(char)*MS_SIZ, 0600);
    m = (char*) shmat(memId, NULL, 0);
    ulazId = shmget(IPC_PRIVATE, sizeof(int), 0600);
    ulaz = (int *) shmat(ulazId, NULL, 0);
    izlazId = shmget(IPC_PRIVATE, sizeof(int), 0600);
    izlaz = (int *) shmat(izlazId, NULL, 0);
    semId = semget(IPC_PRIVATE, 5, 0600);
    (*ulaz) = (*izlaz) = 0;
    if(!SemSetVal(PUN, 1) || !SemSetVal(PRAZAN, 0)
       || !SemSetVal(PISI, 1) || !SemSetVal(UPIS1, 1)
       || !SemSetVal(UPIS2, 0)){
            printf("Ne mogu inicijalizirati semafor!\n");
            exit(1);
    }
    switch(fork()){
        case 0:
                potrosac();
                break;
        default:
                stvori_proizvodjace();
                break;
    }
    exit(0);
}
