#include <stdio.h>
#include <math.h>
#include <malloc.h>
#include <stdlib.h>

#define BLOK 8
#define POMAK -128
#define PI 3.141
#define COSX(x, s) cos(((2 * x + 1) * s * PI)/16)

/* funkcija C, treba za transformaciju */
double C(double arg)
{
	return arg < 1 ? 1.0/sqrt(2.0) : 1;
}

/* diskretna kosinusna transformacija */
double DCT(int u, int v, double* komponenta)
{
	int i, j;
	double sume = 0;
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			sume += komponenta[i * BLOK + j] * COSX(i, u) * COSX(j, v); 
		}
	}
	return sume * C(u) * C(v) * 0.25;
}

/* za zaokruzivanje */
int round_i(double x)
{
	return x > 0.0 ? x + 0.5 : x - 0.5;
}

int main(int argc, char* argv[]) 
{
	FILE* in = fopen(argv[1], "r");
	FILE* out = fopen(argv[3], "w+");

	unsigned char* buf;
	char optbuf[16];
	char c;
	
	unsigned char* r;
	unsigned char* g;
	unsigned char* b;

	double* y = (double*)malloc(BLOK * BLOK * sizeof(double));
	double* cr = (double*)malloc(BLOK * BLOK * sizeof(double));
	double* cb = (double*)malloc(BLOK * BLOK * sizeof(double));

	double* y_transf = (double*)malloc(BLOK * BLOK * sizeof(double));
	double* cr_transf = (double*)malloc(BLOK * BLOK * sizeof(double));
	double* cb_transf = (double*)malloc(BLOK * BLOK * sizeof(double));

	int m,n;
	int rgb_comp;
	int i;
	int j;
	int k;
	int blok_x = atoi(argv[2]);
	
	int K1[] = { 16, 11, 10, 16, 24, 40, 51, 61,
				 12, 12, 14, 19, 26, 58, 60, 55,
				 14, 13, 16, 24, 40, 57, 69, 56,
				 14, 17, 22, 29, 51, 87, 80, 62,
				 18, 22, 37, 56, 68, 109, 103, 77,
				 24, 35, 55, 64, 81, 104, 113, 92,
				 49, 64, 78, 87, 103, 121, 120, 101,
				 72, 92, 95, 98, 112, 100, 103, 99 };

	int K2[] = { 17, 18, 24, 47, 99, 99, 99, 99,
				 18, 21, 26, 66, 99, 99, 99, 99,
				 24, 26, 56, 99, 99, 99, 99, 99,
				 47, 66, 99, 99 ,99, 99, 99, 99,
				 99, 99, 99, 99, 99, 99, 99 ,99,
				 99, 99, 99, 99, 99, 99, 99 ,99,
				 99, 99, 99, 99, 99, 99, 99 ,99,
				 99, 99, 99, 99, 99, 99, 99 ,99 };

	if (argc != 4) {
		printf("Neispravan poziv programa\n");
		return 1;
	}

	if (!in) {
		printf("Nije moguce otvoriti sliku\n");
		return 1;
	}

	fgets(optbuf, sizeof(optbuf), in);
	
	if (optbuf[0] != 'P' || optbuf[1] != '6') {
		printf("Format mora biti P6\n");
		return 1;
	}

	c = getc(in);
	while (c == '#') {
		while (getc(in) != '\n');
		c = getc(in);
	}
	ungetc(c, in);

	if (fscanf(in, "%d %d", &m, &n) != 2) {
		printf("Nepravilna velicina slike\n");
		return 1;
	}
	buf = (unsigned char*)malloc(m * n * 3 * sizeof(unsigned char));
	r = (unsigned char*)malloc(m * n * sizeof(unsigned char));
	g = (unsigned char*)malloc(m * n * sizeof(unsigned char));
	b = (unsigned char*)malloc(m * n * sizeof(unsigned char));
	
	if (fscanf(in, "%d", &rgb_comp) != 1) {
		printf("Nemoguce ucitati dubinu RGB komponente\n");
		return 1;
	}

	if (rgb_comp != 255) {
		printf("Nepravilna dubina RGB komponente\n");
		return 1;
	}
	
	fscanf(in, "%d", &rgb_comp);	/* visak... */
	
	fread(buf, 3 * m, n, in);

	for (i = 0, j = 0; i < m * n * 3; i += 3, j++) {
		r[j] = buf[i];
		g[j] = buf[i + 1];
		b[j] = buf[i + 2];
	}

	/* izvadi blok */
	k = 0;
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			int K = blok_x - (blok_x / (m / BLOK)) * (m / BLOK);
			y[k] = r[i * m + (blok_x / (m / BLOK))
					* (m * BLOK) + K * BLOK + j];
			cb[k] = g[i * m + (blok_x / (m / BLOK))
					* (m * BLOK) + K * BLOK + j];
			cr[k] = b[i * m + (blok_x / (m / BLOK))
					* (m * BLOK) + K * BLOK + j];
			k++;
		}
	}

	/* prebaci u YCbCr */
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			int ij = i * BLOK + j;
			double r = y[ij];
			double g = cb[ij];
			double b = cr[ij];
			y[ij] = 0.299 * r + 0.587 * g + 0.114 * b;
			cb[ij] = 128 - 0.168736 * r - 0.331264 * g + 0.5 * b;
			cr[ij] = 128 + 0.5 * r -0.418688 * g - 0.081312 * b;
		}
	}

	/* napravi pomak */
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			y[i * BLOK + j] = y[i * BLOK + j] + POMAK;
			cb[i * BLOK + j] = cb[i * BLOK + j] + POMAK;
			cr[i * BLOK + j] = cr[i * BLOK + j] + POMAK;
		}
	}

	/* napravi DCT */
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			y_transf[i * BLOK + j] = DCT(i, j, y);
			cb_transf[i * BLOK + j] = DCT(i, j, cb);
			cr_transf[i * BLOK + j] = DCT(i, j, cr);
		}
	}
	
	/* kvantiziraj */
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			y_transf[i * BLOK + j] = 
				round_i(y_transf[i * BLOK + j] / K1[i * BLOK + j]);
			cb_transf[i * BLOK + j] = 
				round_i(cb_transf[i * BLOK + j] / K2[i * BLOK + j]);
			cr_transf[i * BLOK + j] = 
				round_i(cr_transf[i * BLOK + j] / K2[i * BLOK + j]);
		}
	}

	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			fprintf(out, "%.0f ", y_transf[i * BLOK + j]);
		}
		fprintf(out, "\n");
	}
		
	fprintf(out, "\n");
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			fprintf(out, "%.0f ", cb_transf[i * BLOK + j]);
		}
		fprintf(out, "\n");
	}

	fprintf(out, "\n");
	for (i = 0; i < BLOK; i++) {
		for (j = 0; j < BLOK; j++) {
			fprintf(out, "%.0f ", cr_transf[i * BLOK + j]);
		}
		fprintf(out, "\n");
	}
	
	fclose(out);
	fclose(in);
	free(buf);
	free(r);
	free(g);
	free(b);
	free(y);
	free(cb);
	free(cr);
	free(y_transf);
	free(cb_transf);
	free(cr_transf);
	return 0;
}
