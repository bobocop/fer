package hr.fer.zemris.java.dz3;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class PrimjerSkupa {

	/**
	 * Program iz niza Stringova koji su uneseni kao argumenti iz komandne
	 * linije izbacuje duplikate na nacin da svaki element niza ubaci u Set.
	 * Pri tome Set, koji predstavlja skup elemenata, 
	 * ne prihvaca dodavanje novog elementa ako je on u skupu vec sadrzan.
	 * Prilikom ispisa elemenata skupa, pokazuje se da razlicite implementacije
	 * Setova imaju razlicite interne poretke elemenata. U slucaju HashSeta 
	 * (ukloniDuplikate1) to je nasumicno, jer je prioritet brzina, u slucaju
	 * TreeSeta (ukloniDuplikate2) elementi su poredani prirodno, a
	 * LinkedHashSet (ukloniDuplikate3) ce poredati elemente redom 
	 * kako su ubacivani u skup. 
	 * @param args niz rijeci koji se obradjuje
	 */
	public static void main(String[] args) {
		System.out.println("Preko HashSeta:");
		ispisiSkup(ukloniDuplikate1(args));
		System.out.println("Preko TreeSeta:");
		ispisiSkup(ukloniDuplikate2(args));
		System.out.println("Preko LinkedHashSeta:");
		ispisiSkup(ukloniDuplikate3(args));
	}

	private static void ispisiSkup(Collection<String> col) {
		for(String element : col) {
			System.out.println(element);
		}
	}
	
	private static Collection<String> ukloniDuplikate1(String[] polje) {
		Set<String> set = new HashSet<String>();
		for(String element : polje) {
			set.add(element);
		}
		return set;
	}
	
	private static Collection<String> ukloniDuplikate2(String[] polje) {
		Set<String> set = new TreeSet<String>();
		for(String element : polje) {
			set.add(element);
		}
		return set;
	}
	
	private static Collection<String> ukloniDuplikate3(String[] polje) {
		Set<String> set = new LinkedHashSet<String>();
		for(String element : polje) {
			set.add(element);
		}
		return set;
	}
}
