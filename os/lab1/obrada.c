#include <stdio.h>
#include <signal.h>

#define N 6

int OZNAKA_CEKANJA[N];
int PRIORITET[N];
int TEKUCI_PRIORITET = 0;
void obrada_prekida(int);
int sig[] = {SIGUSR1, SIGUSR2, SIGCHLD, SIGURG, SIGCONT, SIGINT};

void zabrani_prekidanje(){
	int i;
	for(i=0; i<5; i++)
		sighold(sig[i]);
	}

void dozvoli_prekidanje(){
	int i;
	for(i=0; i<5; i++)
		sigrelse(sig[i]);
	}

void prekidna_rutina(int sig){
	int n=1;
	int x, j;
	zabrani_prekidanje();
	switch(sig){
		case SIGUSR1:
			n=1;
			printf("-\tX \t-\t-\t-\t-\n");
			break;
		case SIGUSR2:
			n=2;
			printf("-\t-\tX\t-\t-\t-\n");
			break;
		case SIGCHLD:
			n=3;
			printf("-\t-\t-\tX\t-\t-\n");
			break;
		case SIGURG:
			n=4;
			printf("-\t-\t-\t-\tX\t-\n");
			break;
		case SIGINT:
			n=5;
			printf("-\t-\t-\t-\t-\tX\n");
			break;
	}
	OZNAKA_CEKANJA[n]++;;
	do{
		x=0;
		for(j = TEKUCI_PRIORITET + 1; j < N; j++){
			if(OZNAKA_CEKANJA[j] > 0) x = j;
			}
		if(x > 0){
			OZNAKA_CEKANJA[x]--;
			PRIORITET[x] = TEKUCI_PRIORITET;
			TEKUCI_PRIORITET = x;
			dozvoli_prekidanje();
			obrada_prekida(x);
			zabrani_prekidanje();
			TEKUCI_PRIORITET = PRIORITET[x];
		 	}
		}while (x > 0);
	}

void obrada_prekida(int i){
	int k;
	switch(i){
		case 1:
			printf("-\tP\t-\t-\t-\t-\n");
			for(k=1; k<6; k++){
				sleep(1);
				printf("-\t%d\t-\t-\t-\t-\n", k);
				}
			printf("-\tK\t-\t-\t-\t-\n");
			break;
		case 2:
			printf("-\t-\tP\t-\t-\t-\n");
                        for(k=1; k<6; k++){
                                sleep(1);
                                printf("-\t-\t%d\t-\t-\t-\n", k);
                                }
			printf("-\t-\tK\t-\t-\t-\n");
                        break;
		case 3:
			printf("-\t-\t-\tP\t-\t-\n");
                        for(k=1; k<6; k++){
                                sleep(1);
                                printf("-\t-\t-\t%d\t-\t-\n", k);
                                }
			printf("-\t-\t-\tK\t-\t-\n");
                        break;
		case 4:
			printf("-\t-\t-\t-\tP\t-\n");
                        for(k=1; k<6; k++){
                                sleep(1);
                                printf("-\t-\t-\t-\t%d\t-\n", k);
                                }
			printf("-\t-\t-\t-\tK\n -");
                        break;
		case 5:
			printf("-\t-\t-\t-\t-\tP\n");
                        for(k=1; k<6; k++){
                                sleep(1);
                                printf("-\t-\t-\t-\t-\t%d\n", k);
                                }
			printf("-\t-\t-\t-\t-\tK\n");
                        break;
		}
	}
int main(){
	int i;
	sigset(SIGUSR1, prekidna_rutina);
	sigset(SIGUSR2, prekidna_rutina);
	sigset(SIGCHLD, prekidna_rutina);
	sigset(SIGURG, prekidna_rutina);
	sigset(SIGINT, prekidna_rutina);
	printf("Proces obrade prekida, pid=%d\n", getpid());
	printf("G\t1 \t2\t3\t4\t5\n-----------------\n");
	for(i = 0; i < 50; i++){
		sleep(1);
		printf("%d\t-\t-\t-\t-\t-\n", i);
		}
	printf("Proces je zavrsio s poslom");
	return 0;
	}
