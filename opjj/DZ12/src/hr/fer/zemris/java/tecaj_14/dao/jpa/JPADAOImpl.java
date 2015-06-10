package hr.fer.zemris.java.tecaj_14.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import hr.fer.zemris.java.tecaj_14.dao.DAO;
import hr.fer.zemris.java.tecaj_14.dao.DAOException;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getBlogUser(String nick) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		/*
		 * I can certainly say that the following query will always return
		 * exactly one or no objects because a nickname is unique. 
		 * What bugs me and makes the following part of code a little bit 
		 * confusing is that I didn't choose to use the 'getSingleResult()' 
		 * method because it throws an exception (instead of, say, null).
		 * I consider that a really annoying practice because 'SELECT' 
		 * returning nothing is a very common occurence, definitely not an exception
		 * which leads to pointless try/catch blocks.
		 */
		@SuppressWarnings("unchecked")
		List<BlogUser> blogUser = (List<BlogUser>) em
				.createQuery("select u from BlogUser as u where u.nick=:n")
				.setParameter("n", nick)
				.getResultList();
		return blogUser.isEmpty() ? null : blogUser.get(0);
	}

	@Override
	public void save(Object o) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(o);
	}

	@Override
	public List<BlogUser> getAllUsers() throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<BlogUser> blogUsers = (List<BlogUser>) em
				.createQuery("select u from BlogUser as u")
				.getResultList();
		return blogUsers;
	}

	@Override
	public void update(long id, String title, String text, Date lastModifiedOn) 
			throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery("update BlogEntry "
				+ "set title=:t, text=:m, lastModifiedOn=:l "
				+ "where id=:i")
				.setParameter("t", title)
				.setParameter("m", text)
				.setParameter("l", lastModifiedOn)
				.setParameter("i", id)
				.executeUpdate();
	}

	@Override
	public BlogUser getBlogUser(long id) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<BlogUser> blogUser = (List<BlogUser>) em
				.createQuery("select u from BlogUser as u where id=?")
				.setParameter(1, id)
				.getResultList();
		return blogUser.isEmpty() ? null : blogUser.get(0);
	}
}
