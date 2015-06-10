package hr.fer.zemris.java.tecaj_13.dao.sql;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.Unos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova konkretna
 * implementacija očekuje da joj veza stoji na raspolaganju preko
 * {@link SQLConnectionProvider} razreda, što znači da bi netko prije no što
 * izvođenje dođe do ove točke to trebao tamo postaviti. U web-aplikacijama
 * tipično rješenje je konfigurirati jedan filter koji će presresti pozive
 * servleta i prije toga ovdje ubaciti jednu vezu iz connection-poola, a po
 * zavrsetku obrade je maknuti.
 * 
 * @author marcupic
 */
public class SQLDAOProvider implements DAO {

	@Override
	public List<Unos> dohvatiOsnovniPopisUnosa() throws DAOException {
		List<Unos> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT id, title FROM Poruke ORDER BY id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						Unos unos = new Unos();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unosi.add(unos);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {}
			}
		} catch (Exception ex) {
			throw new DAOException(
					"Pogreška prilikom dohvata liste korisnika.", ex);
		}
		return unosi;
	}

	@Override
	public Unos dohvatiUnos(long id) throws DAOException {
		Unos unos = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
					"SELECT id, title, message, createdOn, userEMail from Poruke WHERE id=?");
			pst.setLong(1, id);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						unos = new Unos();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setMessage(rs.getString(3));
						unos.setCreatedOn(new Date(rs.getTimestamp(4).getTime()));
						unos.setUserEMail(rs.getString(5));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata korisnika.", ex);
		}
		return unos;
	}

	@Override
	public void update(Unos u) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
					"UPDATE Poruke SET title=?, message=?, createdOn=?, userEMail=? WHERE id=?");
			pst.setLong(5, u.getId());
			pst.setString(1, u.getTitle());
			pst.setString(2, u.getMessage());
			pst.setTimestamp(3, new Timestamp(u.getCreatedOn().getTime()));
			pst.setString(4, u.getUserEMail());
			pst.executeUpdate();
			
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom promjene zapisa");
		} finally {
			try {
				pst.close();
			} catch (SQLException ignorable) {}
		}
	}

	@Override
	public void save(Unos u) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
					"INSERT INTO Poruke (title, message, createdOn, userEMail) "
					+ "values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, u.getTitle());
			pst.setString(2, u.getMessage());
			pst.setTimestamp(3, new Timestamp(u.getCreatedOn().getTime()));
			pst.setString(4, u.getUserEMail());

			int numberOfAffectedRows = pst.executeUpdate();
			System.out.println("Broj redaka koji su pogođeni ovim "
					+ "unosom: " + numberOfAffectedRows);
			
			ResultSet rset = pst.getGeneratedKeys();
			
			try {
				if(rset != null && rset.next()) {
					long noviID = rset.getLong(1);
					System.out.println("Unos je obavljen i podatci su pohranjeni "
							+ "pod ključem id=" + noviID);
				}
			} finally {
				try { rset.close(); } catch(SQLException ignorable) {}
			}
		} catch(SQLException ex) {
			System.out.println("Pogreška prilikom spremanja zapisa");
		} finally {
			try { pst.close(); } catch(SQLException ignorable) {}
		}
	}
}