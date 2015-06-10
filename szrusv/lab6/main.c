//
// Created by karlo on 22.05.15..
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

#define BUF 64

#define RMPA    0
#define EDF     1
#define LLF     2
#define SCHED   3

#define SCHED_RR    0
#define SCHED_FIFO  1

struct task {
    int period;
    int duration;
    int status;     // how much is done, 0 <= status <= duration
    int priority;   // used by
    int sch_method; // SCHED_RR and SCHED_FIFO
    int id;
};

static struct task* tasks = 0;
static int* quantity = 0;   // how many of each task is there
static int n_tasks = 0;
static int tm = 0;
static int strat;
static int t;
static int (*compare)(const void*, const void*);

int rmpa_compare(const void* t1, const void* t2)
{
    return ((struct task*) t1)->period - ((struct task*) t2)->period;
}

int edf_compare(const void* t1, const void* t2)
{
    return (((struct task*) t1)->period - (tm % ((struct task*) t1)->period))
           - (((struct task*) t2)->period - (tm % ((struct task*) t2)->period));
}

int llf_compare(const void* t1, const void* t2)
{
    struct task* ta1 = (struct task*) t1;
    struct task* ta2 = (struct task*) t2;
    return ((ta1->period - (tm % ta1->period)) - (ta1->duration - ta1->status))
           - ((ta2->period - (tm % ta2->period)) - (ta2->duration - ta2->status));
}

int sched_compare(const void* t1, const void* t2)
{
    struct task* ta1 = (struct task*) t1;
    struct task* ta2 = (struct task*) t2;

    int diff = ta1->priority - ta2->priority;

    if (diff == 0) {
        // lowest id was first in
        diff = ta1->id - ta2->id;
        // unless RR limit had been reached
        if (ta1->sch_method == SCHED_RR && ta1->status > 0 && ta1->status % t == 0) diff = 1;
        if (ta2->sch_method == SCHED_RR && ta2->status > 0 && ta2->status % t == 0) diff = -1;
    }

    return diff;
}

void print_status(char* event, int active_id)
{
    int i;

    printf("time: %d ms\nevent: %s\nready: ", tm, event);
    for (i = 0; i < n_tasks; i++)
        if (quantity[tasks[i].id] > 0 && ((tasks[i].id != active_id) || (active_id > 1)))
            printf("%d ", tasks[i].id);
    if (active_id != -1)
        printf("\nactive: %d\n", active_id);
    else
        printf("\nactive: none\n");
    printf("--------------------\n");
}

int get_active_task_index()
{
    int i;

    if (!(strat == LLF && (tm % t) != 0)) {
        qsort(tasks, n_tasks, sizeof(struct task), compare);
    }

    for (i = 0; i < n_tasks; i++)
        if (quantity[tasks[i].id] > 0)
            return i;

    return -1;
}

int main(int argc, char* argv[])
{
    FILE* fd;
    int sim_duration;
    int i;
    char line_buf[BUF];
    char* opt_ptr;

    if (argc != 2) {
        fprintf(stderr, "Usage: ./%s + [filename]\n", argv[0]);
        return 0;
    }

    fd = fopen(argv[1], "r");
    if (fd == NULL) {
        perror("Error opening file");
        return -1;
    }

    fgets(line_buf, BUF, fd);    // [STRATEGY] [opts]...
    opt_ptr = strtok(line_buf, " ");

    if (strcmp(opt_ptr, "RMPA") == 0) {
        strat = RMPA;
        compare = rmpa_compare;
    } else if(strcmp(opt_ptr, "EDF") == 0) {
        strat = EDF;
        compare = edf_compare;
    } else if(strcmp(opt_ptr, "LLF") == 0) {
        strat = LLF;
        compare = llf_compare;
    } else if(strcmp(opt_ptr, "SCHED") == 0) {
        strat = SCHED;
        compare = sched_compare;
    } else {
        fprintf(stderr, "Unknown strategy %s\n", line_buf);
        return -1;
    }

    opt_ptr = strtok(NULL, " \n");
    sim_duration = strtol(opt_ptr, NULL, 10);

    if (strat == LLF || strat == SCHED) {
        //opt_ptr = strtok(line_buf, "\n");
        t = strtol(opt_ptr, NULL, 10); // LLF - laxity refresh period, SCHED - time interval for RR
    }

    for (i = 0; fgets(line_buf, BUF, fd); i++) {
        n_tasks++;
        tasks = realloc(tasks, n_tasks * sizeof(struct task));
        tasks[i].period = strtol(strtok(line_buf, " "), NULL, 10);
        tasks[i].duration = strtol(strtok(NULL, " \n"), NULL, 10);
        tasks[i].id = i;
        tasks[i].status = 0;
        if (strat == SCHED) {
            tasks[i].priority = strtol(strtok(NULL, " \n"), NULL, 10);
            opt_ptr = strtok(NULL, "\n");
            if (strcmp(opt_ptr, "RR") == 0) {
                tasks[i].sch_method = SCHED_RR;
            } else if (strcmp(opt_ptr, "FIFO") == 0) {
                tasks[i].sch_method = SCHED_FIFO;
            } else {
                fprintf(stderr, "Unknown scheduling method %s\n", opt_ptr);
                return -1;
            }
        }
    }

    quantity = malloc(n_tasks * sizeof(int));
    for (i = 0; i < n_tasks; i++) quantity[i] = 0;  // all arrive at t = 0

    int new_active = get_active_task_index();

    while (tm < sim_duration) {
        char t_chr[5];
        char msg[BUF];
        int active_id = new_active == -1 ? new_active : tasks[new_active].id;

        // check if active task is done or print if it has changed
        if (active_id != -1 && (tasks[new_active].status == tasks[new_active].duration)) {
            quantity[active_id]--;
            sprintf(t_chr, "%d", active_id);
            strcat(strcat(strcat(msg, "task "), t_chr), " completed");
            print_status(msg, active_id);
        }

        msg[0] = t_chr[0] = 0;

        // check for new arrivals
        for (i = 0; i < n_tasks; i++) {
            if (tm % tasks[i].period == 0) {
                quantity[tasks[i].id]++;
                tasks[i].status = 0;
                if (msg[0] == 0)
                    strcat(msg, "task(s) ");
                if (quantity[i] < 2) {
                    // if >= 2 they missed deadline, printed later
                    sprintf(t_chr, "%d ", tasks[i].id);
                    strcat(msg, t_chr);
                }
            }
        }

        if (msg[0] != 0) {
            strcat(msg, "arrived");
            print_status(msg, tasks[new_active].id);
        }

        new_active = get_active_task_index();

        if (new_active != -1 && active_id != tasks[new_active].id) {
            active_id = tasks[new_active].id;
            print_status("active task changed", active_id);
        }

        msg[0] = t_chr[0] = 0;

        for (i = 0; i < n_tasks; i++) {
            if ((tm % tasks[i].period) == 0 && quantity[i] > 1) {
                if (msg[0] == 0)
                    strcat(msg, "task(s) ");
                sprintf(t_chr, "%d ", tasks[i].id);
                strcat(msg, t_chr);
            }
        }

        if (msg[0] != 0) {
            strcat(msg, "missed deadline");
            print_status(msg, active_id);
        }


        //sleep(1);
        if (new_active != -1)
            tasks[new_active].status++;
        tm++;
    }
    return 0;
}