/* Test program for semaphore simulation */

/* On raspberry pi compile with: gcc -o test test-semaphore.c -Wall -lbcm2835 */
/* On test system (e.g. Linux):  gcc -o test test-semaphore.c */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <malloc.h>
#include <time.h>

/* IO test for semaphore; set to 0 when not running on Raspberry Pi! */
#define TEST 0

#if TEST == 1 /* running on raspberry pi */

#include <bcm2835.h>

#define io_init()		init()
#define io_delay(ms)		bcm2835_delay(ms)
#define io_input(pin)		bcm2835_gpio_lev(pin)
#define io_output(pin,state)	\
do { bcm2835_gpio_write (pin, state); change (pin, state); } while(0)
#define io_close()		bcm2835_close ()

/* IO pin assignment */
static int button[] = { 2, 3, 4, 14, 28, 29, 30, 31, -1 };
static int led[] = { 17, 27, 22, 10, 9, 11, 7, 8, 25, 24, 23, 18, 15, -1 };

static void init ()
{
	int i;

	if ( bcm2835_init() == 0 ) {
		perror ( "bcm2835_init" );
		exit(1);
	}

	for ( i = 0; button[i] != -1; i++ ) {
		/* FIXME set them high first */
		bcm2835_gpio_fsel ( button[i], BCM2835_GPIO_FSEL_OUTP );
		bcm2835_gpio_write ( button[i], HIGH );
		bcm2835_delay (10);

		bcm2835_gpio_fsel ( button[i], BCM2835_GPIO_FSEL_INPT );
		bcm2835_gpio_set_pud ( button[i], BCM2835_GPIO_PUD_UP );
	}
	for ( i = 0; led[i] != -1; i++ )
		bcm2835_gpio_fsel ( led[i], BCM2835_GPIO_FSEL_OUTP );
}

#else /* simulation */

#define io_init()
#define io_delay(ms)		usleep(ms*1000)
#define io_input(pin)		randomize() /* random button presses */
#define io_output(pin,state)	change (pin, state)
#define io_close()

#endif


#define OFF	0
#define ON	1

#define T	10 /* ms, update interval */
#define all_pressed()	pressed(1)
#define none_pressed() 	pressed(0)

struct LED {
	int state; /* ON/OFF */
	int cycle; /* cycle in T when led changes state */
	int pin;   /* Raspberry Pi pin connected to LED */
	int x,y;   /* position in ASCII map of semaphore (variable map below) */
};

struct button_state {
	int state;
	int pin;
	unsigned long int t_last_change;
};

/* semaphore map, with "all ON" initial state */
static char map[8][17] = {
/*       0123456789012345 */
/* 0 */ "+++++ N    +++++",
/* 1 */ "++++O      O++++",
/* 2 */ "++O++ O    ++O++",
/* 3 */ "           O   E",
/* 4 */ "W   O           ",
/* 5 */ "++O++    O ++O++",
/* 6 */ "++++O      O++++",
/* 7 */ "+++++    S +++++"
};

#define LEDS 12
static struct LED L[LEDS] = {
/*           state cycle pin   x   y */
/* C_N */   { OFF,   0,    9,  2,  6 },
/* C_E */   { OFF,   0,   15,  3, 11 },
/* C_S */   { OFF,   0,   23,  5,  9 },
/* C_W */   { OFF,   0,   25,  4,  4 },

/* P_NWE */ { OFF,   0,   10,  1,  4 },
/* P_NEW */ { OFF,   0,   17,  1, 11 },
/* P_ENS */ { OFF,   0,   27,  2, 13 },
/* P_ESN */ { OFF,   0,   18,  5, 13 },
/* P_SEW */ { OFF,   0,   22,  6, 11 },
/* P_SWE */ { OFF,   0,    7,  6,  4 },
/* P_WSN */ { OFF,   0,    8,  5,  2 },
/* P_WNS */ { OFF,   0,   11,  2,  2 }
};
/* pin assignment by [sem-rpi.png] */

#define BUTTONS 8
static struct button_state b[BUTTONS] =
{{0,31,0},{0,3,0},{0,2,0},{0,4,0},{0,14,0},{0,29,0},{0,28,0},{0,30,0}};

static const char* button_names[BUTTONS] =
{ "B_NW", "B_NE", "B_EN", "B_ES", "B_SE", "B_SW", "B_WS", "B_WN" };

/* get button[i]'s corresponding semaphore index in led cycle/state array */
static const int btn_to_sem[BUTTONS] =
{ 5, 4, 7, 6, 9, 8, 11, 10 }; 
/* 
 * B_NW -> P_NEW
 * B_NE -> P_NWE
 * B_EN -> P_ESN
 * B_ES -> P_ENS
 * B_SE -> P_SWE
 * B_SW -> P_SEW
 * B_WS -> P_WNS
 * B_WN -> P_WSN
 */
 
/*
 * Modes of operation
 *
 * For each mode define led state for cycles:
 * - state = 0 => led is off
 * - state = 1 => led is on
 * - state > 1 => led is blinking with state*T frequency
 */

#define MODE1_CYCLES	1
static int mode1_duration[] = { 0 };
static unsigned short led1[][LEDS] =
{
/* C_N  C_E  C_S  C_W  P_NWE P_NEW P_ENS P_ESN P_SEW P_SWE P_WSN P_WNS */
{  500, 500, 500, 500,  500,  500,  500,  500,  500,  500,  500,  500 }
};

#define MODE2_CYCLES	6
static int mode2_duration[] = { 2000, 15000, 5000, 2000, 15000, 5000 };
static unsigned short led2[][LEDS] =
{
/* C_N  C_E  C_S  C_W P_NWE P_NEW P_ENS P_ESN P_SEW P_SWE P_WSN P_WNS */
{   0,   0,   0,   0,   0,    0,    0,    0,    0,    0,    0,    0 },
{   0,   1,   0,   1,   1,    1,    0,    0,    1,    1,    0,    0 },
{   0, 100,   0, 100,   0,    0,    0,    0,    0,    0,    0,    0 },
{   0,   0,   0,   0,   0,    0,    0,    0,    0,    0,    0,    0 },
{   1,   0,   1,   0,   0,    0,    1,    1,    0,    0,    1,    1 },
{ 100,   0, 100,   0,   0,    0,    0,    0,    0,    0,    0,    0 }
};

#define MODE3_CYCLES 	12
static int mode3_duration[] = { 2000, 5000, 10000, 5000, 5000, 5000,
								2000, 5000, 10000, 5000, 5000, 5000 };
static unsigned short led3[][LEDS] = 
{
/* C_N  C_E  C_S  C_W P_NWE P_NEW P_ENS P_ESN P_SEW P_SWE P_WSN P_WNS */
{	0,	 0,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 1,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 1,	  0,   1,	1,	  1,    0,    0,    1,    1,    0,    0 },
{	0, 100,	  0,   1,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 0,	  0,   1,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 0,	  0, 100,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 0,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 0,	  1,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	1,	 0,	  1,   0,	0,	  0,    1,    1,    0,    0,    1,    1 },
{	1,	 0,	100,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	1,	 0,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{ 100,	 0,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 }
};

#define MODE4_CYCLES	12
static int mode4_duration[] = { 2000, 5000, 30000, 5000, 5000, 5000,
								2000, 5000, 30000, 5000, 5000, 5000 };
static unsigned short led4[][LEDS] = 
{
/* C_N  C_E  C_S  C_W P_NWE P_NEW P_ENS P_ESN P_SEW P_SWE P_WSN P_WNS */
{	0,	 0,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 1,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 1,	  0,   1,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0, 100,	  0,   1,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 0,	  0,   1,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 0,	  0, 100,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 0,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	0,	 0,	  1,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	1,	 0,	  1,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	1,	 0,	100,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{	1,	 0,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 },
{ 100,	 0,	  0,   0,	0,	  0,    0,    0,    0,    0,    0,    0 }
};

#define MODES	4

static struct sem_mode {
	int cycles;
	int *duration;
	short (*led)[LEDS];
}
m[MODES] = {
	{
		.cycles = MODE1_CYCLES,
		.duration = mode1_duration,
		.led = (short (*)[LEDS]) &led1
	},
	{
		.cycles = MODE2_CYCLES,
		.duration = mode2_duration,
		.led = (short (*)[LEDS]) &led2
	},
	{
		.cycles = MODE3_CYCLES,
		.duration = mode3_duration,
		.led = (short (*)[LEDS]) &led3
	},
	{
		.cycles = MODE4_CYCLES,
		.duration = mode4_duration,
		.led = (short (*)[LEDS]) &led4
	}
};

static int counter;

static unsigned int t = 0;
/* time, relative to cycle start */

static unsigned long int tm = 0;
/* time, ever increasing, no reset (for buttons) */

/* simulate random key press on even seconds */
static int randomize(void)
{
	//srand(time(NULL));
	return ((tm % 400) == 0) && (rand() % 10 == 5);
}

static int pressed(int all)
{
	int i;
	
	for (i = 0; i < BUTTONS; i++) 
		if (b[i].state == !all) return 0;
	return 1;
}

static int set_mode(int mode, int cycle)
{
	int i;

	if (mode < 0 || mode >= MODES || cycle < 0 || cycle >= m[mode].cycles) {
		fprintf(stderr, "Invalid arguments to set_mode\n");
		return -1;
	}

	for (i = 0; i < LEDS; i++) {
		if (m[mode].led[cycle][i] <= 1) {
			L[i].state = m[mode].led[cycle][i];
			L[i].cycle = 0;
		} else {
			L[i].cycle = m[mode].led[cycle][i];
			L[i].state = 0;
		}
	}

	t = 0; /* reset time */

	return 0;
}

/* change output state - on map only */
static void change(int pin, int state)
{
	int i;

	for (i = 0; i < LEDS; i++) {
		if (L[i].pin == pin)
			L[i].state = state;

		if (L[i].state)
			map[L[i].x][L[i].y] = 'O';
		else
			map[L[i].x][L[i].y] = '-';
	}
}

/* print map on screen */
static void print(int mode, int cycle)
{
	int i;

	for (i = 0; i < LEDS; i++) {
		if (L[i].state)
			map[L[i].x][L[i].y] = 'O';
		else
			map[L[i].x][L[i].y] = '-';
	}

	printf("\033[2J\033[1;1H"); /* clear screen */

	for (i = 0; i < 8; i++)
		printf("%s\n", map[i]);

	printf("\nt=%.2lf s (%.2lf s)\n", t*T/1000.,
		 m[mode].duration[cycle]/1000.);
	
	for (i = 0; i < BUTTONS; i++)
		if (b[i].state) printf("%s %ld, turn on %d\n", button_names[i], b[i].t_last_change, btn_to_sem[i]);
	
	printf("Duration: %d Presses: %d\n", m[mode].duration[2], counter);
		
	printf("%ld\n", tm);
}

int main(int argc, char *argv[])
{
	int i, mode = 0, cycle = 0, s;

	// test cases only
	srand(time(NULL));
	//
	
	io_init();

	if (argc > 1)
		i = atoi(argv[1]);
	if (i >= 0 && i <= MODES)
		mode = i;

	if (set_mode(mode, cycle))
		return -1;

	while (1) {
		/* cycle through mode cycles */
		if (m[mode].duration[cycle] > 0 && t > 0 &&
			t % (m[mode].duration[cycle]/T) == 0) {
			cycle = (cycle + 1) % m[mode].cycles;
			set_mode(mode, cycle);
		}

		/* if L[i].cycle.cycle is set, change LEDs state */
		for (i = 0; i < LEDS; i++)
			if (L[i].cycle > 0 &&  t > 0 && t % (L[i].cycle/T) == 0)
				L[i].state = 1 - L[i].state;
		
		/* service pedestrians in special mode */
		if (mode == 3) {
			for (i = 0; i < BUTTONS; i++) {
				if ((b[i].state == 1) && (led3[cycle][btn_to_sem[i]] == 1)) {
					L[btn_to_sem[i]].state = 1;
					b[i].state = 0;
				}
			}
		}
				 
		/* set output to calculated values */
		for (i = 0; i < LEDS; i++)
			io_output(L[i].pin, L[i].state);

		print(mode, cycle);

		io_delay(T);
		t++;
		tm++;

		/* update buttons state and take appropriate action */
		for (i = 0; i < BUTTONS; i++) {
			s = io_input(b[i].pin);
			if (s) {
				counter++;
				b[i].state = 1;
				b[i].t_last_change = tm;
			}
		}
		
		/* take appropriate action according to buttons pressed */
		if (all_pressed()) {
			// next mode
			for (i = 0; i < BUTTONS; i++)
				b[i].state = 0;
			cycle = 0;
			mode = (mode + 1) % MODES;
			set_mode(mode, cycle);
		} else {
			if (mode == 3) {
				if (none_pressed())
					// no pedestrians, switch to mode4 durations
					m[mode].duration = mode4_duration;
				else
					// service pedestrians, shorten car cycles
					m[mode].duration = mode3_duration;
			}
		}
	}

	io_close();

	return 0 ;
}
