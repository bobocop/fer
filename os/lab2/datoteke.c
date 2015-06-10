#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#define MS 512

pthread_t dbr[3];
char buf[MS];
int gbr, d2br=0, d3br=0;
int zs;
int zastavica; /*0 = .txt, 1 = .html*/
int brrt;
FILE *fp;
int kraj=0;

void ocisti_buf()
{
    int i;
    for (i = 0; i < MS; i++){
        buf[i] = 0;
    }
}

int broji_rijeci(char *redak)
{
    int br=0, zr=0, i;

    for (i = 0; redak[i] != '\n' && redak[i] != 0; i++){
        if (redak[i] == ' ' && zr){
            br++;
            zr = 0;
        } else{
            if(!zr) zr = 1;
        }
    }
    if ((redak[i] == '\n' || redak[i] == 0) && zr) br++;
    return br;
}

int broji_tagove(char *redak)
{
    int br=0, zt = 0, i,  zatt;
    for (i = 0; redak[i] != '\n' && redak[i] != 0; i++){
        if (redak[i] == '<'){
             if(redak[i+1] == '/') zatt = 1;
             else zt=1;
        } else if(redak[i] == '>'){
            if(!zatt){
                zt=0;
                br++;
            }
            else zatt=0;
        }
    if ((redak[i] == '\n' || redak[i] == 0) && zt) br++;
    }
    return br;
}

void *dohvati()
{
    gbr = 0;
    while (fgets(buf, MS, fp) != NULL){
        gbr++;
        while (d2br <= gbr || d3br <= gbr);
        ocisti_buf();
    }
    kraj = 1;
    gbr++;
}

void *broji()
{
    d2br = 1;
    while (kraj != 1){
        while (d2br > gbr);
        if (zastavica) brrt += broji_tagove(buf);
        else brrt += broji_rijeci(buf);
        d2br++;
    }
}

void *suma()
{
    int i;
    d3br = 1;
    while (kraj != 1){
        while (d3br > gbr);
        for (i = 0; i < MS; i++){
            zs ^= buf[i];
        }
        d3br++;
    }
}
void analiza(char *datoteka)
{
    int i;
    if (strstr(datoteka, ".txt") != NULL) zastavica = 0;
    else if (strstr(datoteka, ".html") != NULL) zastavica = 1;
    else exit(0);
    if ((fp = fopen(datoteka, "r")) == NULL){
        printf("Ne mogu otvoriti %s.\n", datoteka);
        exit(0);
    }
    pthread_create(&dbr[0], NULL, dohvati, NULL);
    pthread_create(&dbr[1], NULL, broji, NULL);
    pthread_create(&dbr[2], NULL, suma, NULL);
    for (i = 0; i < 3; i++){
        if (pthread_join(dbr[i], NULL)){
            printf("Greska pri radu s dretvama!\n");
            exit(0);
        }
    }
    printf("Datoteka \"%s\" ima %d ", datoteka, brrt);
    zastavica ? printf("tagova") : printf("rijeci");
    printf("; zastitna suma je: %d\n", zs);
    exit(0);
}

int main(int argc, char *argv[])
{
    int i;
    if (argc <= 1){
        printf("Neispravan poziv programa.\n");
        return -1;
    }
    for (i = 1; i < argc; i++){
        switch(fork()){
            case -1:
                    printf("Ne mogu stvoriti procese!\n");
                    return -1;
            case 0:
                    analiza(argv[i]);
                    exit(0);
            default:
                    break;
        }
    }
    printf("Cekam na procese i dretve...\n");
    while (i--) wait(NULL);
    exit(0);
}
