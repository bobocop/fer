#include <iostream>
#include <cstdlib>
#include <ctime>
using namespace std;
class Okvir{
    public:
    Okvir();
    int stranica;
    int rec_use;
    void IspisiStr(int);
    void IspisiStatus();
};

Okvir::Okvir()
{
    stranica = 0;
    rec_use = 0;
}

void Okvir::IspisiStr(int arg)
{
    if(stranica){
        switch(arg){
        case 0:
                cout << stranica << "\t";   //0 - nista posebno
                break;
        case 1:
                cout << "(" << stranica << ")\t";   //1 - pogodak
                break;
        case 2:
                cout << "[" << stranica << "]\t";   //2 - promjena vrijednosti
                break;
        }
    }
    else cout << "-\t";
}
void Okvir::IspisiStatus()
{
    cout << "(" << stranica << ", " << rec_use << ")   ";
}

int main(int argc, char *argv[])
{
    if(argc != 3){
        cout << "Neispravan poziv programa" << endl;
        return -1;
    }
    int br_okvira = atoi(argv[1]);
    int br_zahtjeva = atoi(argv[2]);
    if((br_okvira < 4 || br_okvira > 10) ||
       (br_zahtjeva < 10 || br_zahtjeva > 100)){
            cout << "Okvira treba biti 4-10, zahtjeva 1-100" << endl;
            return -1;
    }
    //generiranje zahtjeva
    cout << "Zahtjevi: ";
    srand(static_cast<unsigned int>(time(NULL)));
    int zahtjevi[br_zahtjeva];
    for(int i=0; i<br_zahtjeva; i++){
        zahtjevi[i] = rand()%8 + 1;
        cout << zahtjevi[i] << " ";
    }
    cout << endl;
    //tablica za ispis
    cout << "#N\t";
    for(int i=1; i <= br_okvira; i++){
        cout << i << "\t";
    }
    for(int i=1; i<= br_okvira; i++){
        cout << "(v, ru)  ";
    }
    cout << endl << "----------------------------------------" << endl;
    //posao
    Okvir o[br_okvira];
    for(int i=0; i < br_zahtjeva; i++){
        int pogodak = -1, prazan = -1, najmanji=-1;
        //prvo pogledaj je li stranica u memoriji, usput oznaci prvog praznog na kojeg naletis
        for(int j=0; j < br_okvira; j++){
            if(o[j].stranica == zahtjevi[i]){
                pogodak = j;
                o[j].rec_use++;
                break;
            }
            else if(o[j].stranica == 0 && prazan < 0) prazan = j;
        }
        if(pogodak < 0){
            if(prazan > -1){
                o[prazan].stranica = zahtjevi[i];
                o[prazan].rec_use++;
            }
           //odredjivanje LRU okvira

            else{
                najmanji = 0;
                for(int j=1; j<br_okvira; j++){
                    if(o[j].rec_use < o[najmanji].rec_use) najmanji = j;
                }
                o[najmanji].stranica = zahtjevi[i];
                o[najmanji].rec_use++;
                }
        }
    //ispis
        cout << zahtjevi[i] << "\t";
        for(int j = 0; j < br_okvira; j++){
            if(j == pogodak) o[j].IspisiStr(1);
            else if(j == najmanji || j == prazan) o[j].IspisiStr(2);
            else o[j].IspisiStr(0);
        }
        for(int j = 0; j < br_okvira; j++){
            o[j].IspisiStatus();
        }
        cout << endl;
    }
    return 0;
}
