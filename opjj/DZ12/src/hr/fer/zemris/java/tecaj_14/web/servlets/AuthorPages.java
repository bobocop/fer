package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_14.model.BlogComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is mainly used for interaction with registered/logged in
 * users, providing them with "new" and "edit" options for their
 * blog entry management. It also handles "addcomment" requests, which
 * can come from any visitor.
 * @author karlo
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorPages extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathSplit[] = req.getPathInfo().substring(1).split("/");	
		// [0] - nick, [1] - entry ID / option
		BlogUser user = DAOProvider.getDAO().getBlogUser(pathSplit[0]);
		long loggedInID = 0;
		if(req.getServletContext().getAttribute("current.user.id") != null) {
			loggedInID = (long) req.getServletContext().getAttribute("current.user.id");
		}
		if(loggedInID == user.getId()) {
			req.setAttribute("owner", "true");
		}
		if(pathSplit.length == 1) {
			if(user != null) {
				req.setAttribute("entries", user.getBlogEntries());
				req.setAttribute("nick", user.getNick());
				req.getRequestDispatcher("/WEB-INF/pages/user_entries.jsp")
					.forward(req, resp);
			}	// no such user?
		} else {
			switch(pathSplit[1]) {
			case "new":
				req.getRequestDispatcher("/WEB-INF/pages/new_entry.jsp")
					.forward(req, resp);
				break;
			case "edit":
				req.setAttribute("entry", 
						DAOProvider.getDAO().getBlogEntry(
								Long.parseLong(req.getParameter("id"))
								)
						);
				req.getRequestDispatcher("/WEB-INF/pages/edit_entry.jsp")
					.forward(req, resp);
				break;
			default:
				long id = Long.parseLong(pathSplit[1]);
				req.setAttribute("entry", DAOProvider.getDAO().getBlogEntry(id));
				req.getRequestDispatcher("/WEB-INF/pages/display_entry.jsp")
					.forward(req, resp);
				// invalid id?
			}
		}
	}

	/**
	 * Used for creating commments and entry editing.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BlogUser loggedInUser = DAOProvider.getDAO()
				.getBlogUser(
						(long) req.getServletContext()
						.getAttribute("current.user.id")
						);
		String pathSplit[] = req.getPathInfo().substring(1).split("/");	
		// [0] - nick, [1] - option
		System.out.println(req.getPathInfo());
		switch(pathSplit[1]) {
		case "addcomment":
			BlogComment newComm = new BlogComment();
			newComm.setBlogEntry(
					DAOProvider.getDAO().getBlogEntry(
							Long.parseLong(req.getParameter("entryID"))
							)
					);
			newComm.setMessage(req.getParameter("message"));
			try {
				newComm.setPostedOn(
						dateFormat.parse(req.getParameter("postedOn").trim()));
			} catch (ParseException ignorable) {}
			newComm.setUsersEMail(req.getParameter("usersEMail"));
			DAOProvider.getDAO().save(newComm);
			resp.sendRedirect("/aplikacija4/servleti/author/"
					+ pathSplit[0] + "/" 
					+ req.getParameter("entryID"));
			break;
		case "new":
			BlogEntry newEntry = new BlogEntry();
			newEntry.setTitle(req.getParameter("title"));
			newEntry.setText(req.getParameter("text"));
			newEntry.setCreator(loggedInUser);
			try {
				newEntry.setCreatedOn(dateFormat.parse(req.getParameter("createdOn")));
			} catch (ParseException ignorable) {}
			DAOProvider.getDAO().save(newEntry);
			resp.sendRedirect("/aplikacija4/servleti/author/"
					+ loggedInUser.getNick());
			break;
		case "edit":
			long id = Long.parseLong(req.getParameter("entryID"));
			String message = req.getParameter("text");
			String title = req.getParameter("title");
			Date lastModifiedOn = null;
			try {
				lastModifiedOn = dateFormat.parse(req.getParameter("lastModifiedOn"));
			} catch (ParseException ignorable) {}
			if(loggedInUser.equals(
					DAOProvider.getDAO()
					.getBlogEntry(id)
					.getCreator())) {
				// some security
				DAOProvider.getDAO().update(id, title, message, lastModifiedOn);
			}
			resp.sendRedirect("/aplikacija4/servleti/author/"
					+ loggedInUser.getNick() + "/" + req.getParameter("entryID"));
			break;
		default:
			break;
		}
	}
}