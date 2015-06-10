#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <time.h>

int pid = 0;

void prekidna_rutina(int sig){
	kill(pid, SIGKILL);
	exit(0);
}

int main(int argc, char *argv[]){
	int y;
	pid = atoi(argv[1]);
	sigset(SIGINT, prekidna_rutina);
	while(1){
		srand((unsigned)time(NULL));
		y = rand()%3 + 3;               /*slucajno odaberi vremenski interval (3-5s)*/
		sleep(y);
		y = rand()%4 + 1;               /*slucajno odaberi signal*/
		if(y == 1) y = SIGUSR1;
		else if(y == 2) y = SIGUSR2;
		else if(y == 3) y = SIGCHLD;
		else if(y == 4) y = SIGURG;
		kill(pid, y);
	}
	return 0;
}
