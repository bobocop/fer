package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Dohvaća sve postojeće unose u bazi, ali puni samo dva podatka:
	 * id i title.
	 * 
	 * @return listu unosa
	 * @throws DAOException u slučaju pogreške
	 */
	public List<Unos> dohvatiOsnovniPopisUnosa() throws DAOException;
	
	/**
	 * Dohvaća Unos za zadani id. Ako unos ne postoji, vraća <code>null</code>.
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public Unos dohvatiUnos(long id) throws DAOException;

	/**
	 * Updates the already stored record.
	 * @param u an Unos with the new parameters
	 */
	public void update(Unos u);

	/**
	 * Inserts a new record into the storage
	 * @param u an Unos which encapsulates a new record
	 */
	public void save(Unos u);
	
}