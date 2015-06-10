package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenFunction extends Token {
	private String name;
	
	/**
	 * Ime funkcije sprema se bez pocetnog znaka "@" kojem se u
	 * izvornom nizu oznacava funkcija
	 * @param ime funkcije
	 */
	public TokenFunction(String name) {
		this.name = name;
	}
	
	/**
	 * Ova se metoda moze koristiti ukoliko se pri ispisu zeli naglasiti
	 * da je Token upravo funkcija (a ne npr. varijabla). Ako je potrebno
	 * "cisto" ime funkcije, koristiti metodu getName.
	 * @return String @ + ime funkcije
	 */
	public String asText() {
		return "@" + name;
	}
	
	/**
	 * Za razliku od asText, vraca samo ime funkcije, bez oznake.
	 * @return String ime funkcije
	 */
	public String getName() {
		return name;
	}
	
	public void accept(ITokenVisitor visitor) {
		visitor.visitTokenFunction(this);
	}
}
