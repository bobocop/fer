package hr.fer.zemris.java.tecaj_14.dao;

import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Dohvaća korisnika sa zadanim <code>nick</code>om. Ako takav ne postoji,
	 * vraća <code>null</code>
	 * 
	 * @param nick Nadimak korisnika kojeg se dohvaća
	 * @return BlogUser objekt ili <code>null</code>
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogUser getBlogUser(String nick) throws DAOException;

	/**
	 * Omogućuje dohvat korisnika preko ID-a (koji je jedinstven).
	 * @param id korisnika
	 * @return BlogUser objekt ili <code>null</code>
	 * @throws DAOException
	 */
	public BlogUser getBlogUser(long id) throws DAOException;
	
	/**
	 * Ubacuje novu ntorku u bazu podataka.
	 * @param BlogUser/BlogComment/BlogEntry objekt
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public void save(Object o) throws DAOException;
	
	/**
	 * @return listu svih korisnika ili null ako ne postoji ni jedan korisnik
	 * @throws DAOException ako dođe do porgreške pri dohvatu podataka
	 */
	public List<BlogUser> getAllUsers() throws DAOException;
	
	/**
	 * Izmjena postojećeg zapisa.
	 * @param id ID zapisa
	 * @param title novi naslov
	 * @param text the novi tekst zapisa
	 */
	public void update(long id, String title, String text, Date modifiedOn) 
			throws DAOException;
}
