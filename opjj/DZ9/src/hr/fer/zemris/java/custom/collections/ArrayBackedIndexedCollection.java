package hr.fer.zemris.java.custom.collections;

public class ArrayBackedIndexedCollection {
	private int size = 0;
	private int capacity;
	private Object[] elements;
	
	/**
	 * Konstruktor bez argumenata postavlja inicijalni kapacitet
	 * kolekcije na 16.
	 */
	public ArrayBackedIndexedCollection(){
		capacity = 16;
		elements = new Object[16];
	}
	
	/**
	 * Konstruktor, postavlja kapacitet kolekcije kako je zadano argumentom
	 * @param int inicijalni kapacitet
	 * @throws IllegalArgumentException ukoliko je kapacitet < 1
	 */
	public ArrayBackedIndexedCollection(int initialCapacity) 
			throws IllegalArgumentException {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		capacity = initialCapacity;
		elements = new Object[initialCapacity];
	}
	
	/**
	 * Vraca true ako je kolekcija prazna, false inace.
	 * @return Boolean
	 */
	public boolean isEmpty() {
		return size < 1 ? true : false;
	}
	
	/**
	 * Vraca velicinu kolekcije
	 * @return trenutni broj elemenata
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Sluzi za ubacivanje bilo kakvog objekta u kolekciju.
	 * @param element koji se ubacuje
	 * @throws IllegalArgumentException ukoliko je zadan null, koji
	 * kolekcija ne prima
	 */
	public void add(Object value) throws IllegalArgumentException {
		if(value == null) {
			throw new IllegalArgumentException();
		}
		if(size < capacity) {
			elements[size++] = value;
		} else {
			elements = reallocateArray();
			elements[size++] = value;
		}
	}
	
	/**
	 * Vraca element s indeksa zadanog argumentom.
	 * @param indeks s kojeg zelimo dohvatiti element
	 * @return element
	 * @throws IndexOutOfBoundsException za pogresno zadan indeks
	 */
	public Object get(int index) throws IndexOutOfBoundsException {
		if(index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		return elements[index];
	}
	
	/**
	 * Mice element koji je spremljen pod indeksom zadanim argumentom.
	 * Buduci da ne dozvoljava null elemente u kolekciji, svi se elementi
	 * pomicu za jedno mjesto ulijevo (osim ako maknuti element nije bio
	 * posljednji).
	 * @param index elementa koji se zeli ukloniti
	 * @throws IndexOutOfBoundsException za pogresno zadan indeks
	 */
	public void remove(int index) throws IndexOutOfBoundsException {
		if(index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		for(int i = index; i < size - 1; i++) {
			swap(i, i + 1);
		}
		size--;
	}
	
	/**
	 * Ubacuje element u kolekciju na mjesto zadano parametrom position.
	 * @param value element koji se ubacuje u listu
	 * @param position ili indeks na koji se ubacuje
	 * @throws IndexOutOfBoundsException za pogresno zadanu poziciju
	 */
	public void insert(Object value, int position) throws IndexOutOfBoundsException {
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}
		add(value);
		for(int i = size - 1; i > position; i--) {
			swap(i, i - 1);
		}
	}
	
	/**
	 * Vraca indeks odredjenog elementa ukoliko isto postoji u nizu.
	 * @param element koji se trazi
	 * @return indeks ukoliko je element u kolekciji, -1 inace
	 */
	public int indexOf(Object value) {
		for(int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Provjerava da li kolekcija sadrzi zadani element. Slicno kao i 
	 * indexOf, koju cak i koristi, samo sto vraca Boolean.
	 * @param element koji se trazi
	 * @return true ako kolekcija sadrzi element, false inace
	 */
	public boolean contains(Object value) {
		return indexOf(value) > 0 ? true : false;
	}
	
	/**
	 * "Brise" sve elemente kolekcije na nacin da postavi velicinu kolekcije
	 * na nulu; naknadnim upisom se "stari elementi", koji vise nisu nikako
	 * dostupni, biti prepisani.
	 */
	public void clear() {
		elements = new Object[capacity];
		size = 0;
	}
	
	/**
	 * Realocira polje kada postane premalo za korisnicke potrebe,
	 * udvostrucujuci njegov kapacitet.
	 * @return Object[] novo, uvecano polje
	 */
	private Object[] reallocateArray() {
		capacity *= 2; 
		Object[] elementsNew = new Object[capacity];
		System.arraycopy(elements, 0, elementsNew, 0, size);
		return elementsNew;
	}
	
	/**
	 * Pomocna funkcija koja sluzi za zamjenu dva elementa indeksa
	 * i i j u polju.
	 * @param int i
	 * @param int j
	 */
	private void swap(int i, int j) {
		Object temp = elements[i];
		elements[i] = elements[j];
		elements[j] = temp;
	}
}
