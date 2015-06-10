#include <stdio.h>
#include <string.h>
#define RS 10000U
#define ZAG 12U

typedef struct Zaglavlje_s{
	unsigned int velicina, zauzet;
	struct Zaglavlje_s *sljed;
}Zaglavlje;

Zaglavlje *Lista;
char Spremnik[RS];
int brBlok = 1, brSlob=1, brZau=0;

void init(void)
/*Postavlja zaglavlje na pocetak spremnika koji postaje prvi blok*/
{
	Zaglavlje *prvo;
	prvo = (Zaglavlje*)Spremnik;
	prvo->velicina = RS;
	prvo->zauzet = 0;
	prvo->sljed = NULL;
	Lista = prvo;
}


void *dodijeli(unsigned int velicina)
{
	Zaglavlje *pom; 
	char *novo;
	/*Nadji u listi slobodnih blok adekvatne velicine*/
	for(pom = Lista; pom != NULL; pom = pom->sljed){
		if(pom->velicina >= velicina + ZAG){
			if(pom->velicina - velicina - ZAG >= sizeof(char)+ZAG){
				/*Podijeli blok*/
				/*Slobodni blok je sada zauzet*/
				pom->zauzet = 1;
				/*Zaglavlje za novi slobodni blok*/
				novo = (char*)pom;
				novo += (velicina + ZAG);
				((Zaglavlje*)novo)->velicina = pom->velicina - velicina - ZAG;
				pom->velicina = velicina + ZAG;
				((Zaglavlje*)novo)->zauzet = 0;
				((Zaglavlje*)novo)->sljed = Lista->sljed;
				/*Ubaci novo zaglavlje u listu slobodnih*/
				Lista = ((Zaglavlje*)novo);
				printf("Dodijeljen blok na adresi %d\n", novo-Spremnik);
				brBlok++;
			}
			else{
				/*Dodijeli cijeli blok*/
				pom->zauzet = 1;
				Lista = Lista->sljed;
				printf("Dodijeljen blok na adresi %d\n", (char*)pom-Spremnik);
				brSlob--;
			}
			brZau++;
			return pom;
		}
	}
	printf("Nema dovoljno prostora u spremniku\n");
	return NULL;
}

void oslobodi(void *kazaljka)
{
	/*Oznaci blok slobodnim*/
	Zaglavlje *pom1, *pom2;
	pom1 = (Zaglavlje*)kazaljka;
	pom1->zauzet = 0;
	/*Ubaci ga u listu slobodnih*/
	/*Lista slobodnih je prazna*/
	if(Lista == NULL){
		Lista = pom1;
		brSlob++;
	}
	/*Lista slobodnih nije prazna*/
	else{
		if(Lista > pom1){
			pom1->sljed = Lista;
			Lista = pom1;
		}
		else{
			for(pom2 = Lista; pom2->sljed && pom2->sljed < pom1; pom2=pom2->sljed);
			pom1->sljed = pom2->sljed;
			pom2->sljed = pom1;
		}
	}
	brSlob++;
	brZau--;
	/*Ispisi listu slobodnih*/
	for(pom2 = Lista; pom2; pom2=pom2->sljed){
		printf("Poc. adr.: %d Velicina: %d\n", (char*)pom2-Spremnik, pom2->velicina);
	}
	/*Spoji susjedne slobodne blokove, ako je moguce, koliko je moguce*/
	while(1){
		int biloSpajanja = 0;
		for(pom2 = Lista; pom2->sljed; pom2=pom2->sljed){
			if(((char*)pom2->sljed - (char*)pom2) == pom2->velicina){
				/*Napravi spajanje*/
				pom2->velicina += pom2->sljed->velicina;
				pom2->sljed = pom2->sljed->sljed;
				biloSpajanja = 1;
				brSlob--;
				brBlok--;
				break;
			}
		}
		if(!biloSpajanja) break;
	}
	printf("Nakon spajanja...\n");
	for(pom2 = Lista; pom2; pom2=pom2->sljed){
		printf("Poc. adr.: %d Velicina: %d\n", (char*)pom2-Spremnik, pom2->velicina);
	}
}

void Ispis(void)
/*Trazi zaglavlja po spremniku i ispisuj*/
{
	int brojac = 0, offset=0;
	char *pok = Spremnik;
	Zaglavlje *zag;
	printf("RS: broj blokova = %d, slobodni = %d, zauzeti = %d\n", brBlok, brSlob, brZau);
	while(brojac++ < brBlok){
		zag = (Zaglavlje*)pok;
		printf("%d: pocetak = %d, velicina = %d, oznaka = %c\n", brojac, offset, zag->velicina, zag->zauzet?'z':'s');
		offset += zag->velicina;
		pok += zag->velicina;
	}
}
int main()
{
	char *pom;
	Zaglavlje *zahtjev;
	char izbor;
	unsigned int vel;
	init();
	Ispis();
	while(1){
		printf("\nUnesi zahtjev (d-dodijeli, o-oslobodi): ");
		izbor = getc(stdin);
		if(izbor == 'd'){
			printf("Unesi velicinu programa (u oktetima): ");
			scanf("%d", &vel);
			fflush(stdin);
			printf("\n");
			zahtjev = (Zaglavlje*)dodijeli(vel);
			Ispis();
		}
		else if(izbor == 'o'){
			printf("Unesi pocetnu adresu programa: ");
			scanf("%d", &vel);
			pom = Spremnik+vel;
			oslobodi(pom);
			printf("\n");
			Ispis();
			fflush(stdin);
		}
		else printf("\n");
	}
	return 0;
}