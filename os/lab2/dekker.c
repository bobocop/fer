#include <stdio.h>
#include <pthread.h>

#define N 2

int pravo = 1;
int zastavica[N];

void ispisi(int i, int k, int m)
{
    printf("Dretva: %d, K.O. br: %d, (%d/5) *** pravo: %d, z[0]: %d, z[1] = %d\n", i+1, k, m, pravo, zastavica[0], zastavica[1]);
}

void udji_u_ko(int i, int j)
{
    zastavica[i] = 1;
    while(zastavica[j] != 0){
        if(pravo == j){
            zastavica[i] = 0;
            while(pravo == j);
            zastavica[i] = 1;
        }
    }
}

void izadji_iz_ko(int i, int j)
{
    pravo = j;
    zastavica[i] = 0;
}

void *dretva(void *x)
{
    int i = *((int*)x);
    int k, m;
    for(k = 1; k <= 5; k++){
        udji_u_ko(i, !i);
        for(m = 1; m <= 5; m++){
            ispisi(i, k, m);
            sleep(1);
        }
        izadji_iz_ko(i, !i);
    }
}


int main()
{
    int i;
    int br[N];
    pthread_t dbr[N];
    for(i = 0; i < N; i++){
        br[i] = i;
        if(pthread_create(&dbr[i], NULL, dretva, (void *)&br[i])){
            printf("Stvaranje dretve nije uspjelo.\n");
            return -1;
        }
    }
    for(i = 0; i < N; i++){
        pthread_join(dbr[i], NULL);
    }
    return 0;
}
