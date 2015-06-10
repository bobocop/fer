#include <stdio.h>
#include <math.h>
#include <malloc.h>
#include <stdlib.h>

#define BLOK 16
#define PROZOR 16

// daje redni broj prvog piksela u bloku x
int bl_u_pocpx(int x, int m)
{
	return (x / (m / BLOK)) * BLOK * m 
			+ (x - ((x / (m / BLOK)) * (m / BLOK))) * BLOK;
}

// vadi blok prema rednom broju (iz 1. dz)
void izvadi_blok(int x, int m, int n, unsigned char* slika, 
		unsigned char* blok)
{
	int i, j, k;
			
	k = 0;
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			int K = x - (x / (m / BLOK)) * (m / BLOK);
			blok[k] = slika[i * m + (x / (m / BLOK))
					* (m * BLOK) + K * BLOK + j];
			k++;
		}
	}
}

// vadi blok prema pocetnom pikselu
void izvadi_blok_px(int poc, unsigned char* slika, unsigned char* blok)
{
	int i, j;
	
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			blok[i * BLOK + j] = slika[poc + i * 512 + j];
		}
	}
}

// usporedba blokova MAD metodom
double mad(unsigned char* blok1, unsigned char* blok2)
{
	int i, j;
	int suma = 0;
	int raz;
	
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			raz = blok1[i * BLOK + j] - blok2[i * BLOK + j];
			raz = raz < 0 ? raz * -1 : raz;
			suma += raz;
		}
	}
	
	return (1.0 / (BLOK * BLOK)) * suma;
}

int main(int argc, char* argv[]) 
{
	FILE* in1 = fopen("lenna1.pgm", "r");
	FILE* in2 = fopen("lenna.pgm", "r");

	unsigned char* buf1;
	unsigned char* buf2;
	char optbuf[16];
	char c;
	
	unsigned char* blok1 = (unsigned char*) malloc(
			BLOK * BLOK * sizeof(unsigned char));
	unsigned char* blok2 = (unsigned char*) malloc(
			BLOK * BLOK * sizeof(unsigned char));

	int m,n;
	int comp;
	int i;
	int j;
	int blok_x = atoi(argv[1]);
	double razlika_min;
	int prviput = 1;
	int najsl;

	if (argc != 2) {
		printf("Neispravan poziv programa\n");
		return 1;
	}

	if (!in1) {
		printf("Nije moguce otvoriti sliku lenna1.pgm\n");
		return 1;
	}
	
	if (!in2) {
		printf("Nije moguce otvoriti sliku lenna.pgm\n");
		return 1;
	}

	/* provjeri formate */
	
	fgets(optbuf, sizeof(optbuf), in1);
	
	if (optbuf[0] != 'P' || optbuf[1] != '5') {
		printf("Format mora biti P5\n");
		return 1;
	}
	
	fgets(optbuf, sizeof(optbuf), in2);
	
	if (optbuf[0] != 'P' || optbuf[1] != '5') {
		printf("Format mora biti P5\n");
		return 1;
	}

	/* prodji komentare */
	
	c = getc(in1);
	while (c == '#') {
		while (getc(in1) != '\n');
		c = getc(in1);
	}
	ungetc(c, in1);
	
	c = getc(in2);
	while (c == '#') {
		while (getc(in2) != '\n');
		c = getc(in2);
	}
	ungetc(c, in2);

	/* ucitaj velicine slika (pretpostavi da su iste) */

	if (fscanf(in1, "%d %d", &m, &n) != 2) {
		printf("Nepravilna velicina slike\n");
		return 1;
	}
	buf1 = (unsigned char*)malloc(m * n * sizeof(unsigned char));
	
	if (fscanf(in2, "%d %d", &m, &n) != 2) {
		printf("Nepravilna velicina slike\n");
		return 1;
	}
	buf2 = (unsigned char*)malloc(m * n * sizeof(unsigned char));
	
	/* ucitaj dubine */
	
	if (fscanf(in1, "%d", &comp) != 1) {
		printf("Nemoguce ucitati dubinu komponente\n");
		return 1;
	}

	if (comp != 255) {
		printf("Nepravilna dubina komponente\n");
		return 1;
	}
	
	if (fscanf(in2, "%d", &comp) != 1) {
		printf("Nemoguce ucitati dubinu komponente\n");
		return 1;
	}

	if (comp != 255) {
		printf("Nepravilna dubina komponente\n");
		return 1;
	}
	
	/* viskovi... */
	
	fscanf(in1, "%d", &comp);
	fscanf(in2, "%d", &comp);
	
	/* ucitaj slike u memoriju */
	
	fread(buf1, m, n, in1);
	fread(buf2, m, n, in2);

	/* izvadi blok iz prve slike */
	
	izvadi_blok(blok_x, m, n, buf1, blok1);
	
	for (i = -PROZOR; i <= PROZOR; i++) {
		for (j = -PROZOR; j <= PROZOR; j++) {
			double razlika;
			int poc = bl_u_pocpx(blok_x, m);
			int za_usporedbu = poc + i * m + j;
			
			if (za_usporedbu < 0) continue;
			izvadi_blok_px(za_usporedbu, buf2, blok2);
			if (prviput) {
				prviput = !prviput;
				razlika_min = mad(blok1, blok2);
				najsl = poc + i * m + j;
			}
			razlika = mad(blok1, blok2);
			if (razlika < razlika_min) {
				razlika_min = razlika;
				najsl = poc + i * m + j;
			}	
		}
	}
	
	printf("v_p: (%d, %d)\n", najsl % m - bl_u_pocpx(blok_x, m) % m,
			najsl / m - bl_u_pocpx(blok_x, m) / m);
	
	fclose(in1);
	fclose(in2);
	free(buf1);
	free(buf2);
	free(blok1);
	free(blok2);
	
	return 0;
}
