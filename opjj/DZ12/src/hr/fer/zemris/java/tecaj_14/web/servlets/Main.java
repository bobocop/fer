package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;
import hr.fer.zemris.java.tecaj_14.utility.PasswordChecker;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles the starting page generation and the login process.
 * @author karlo
 *
 */
@WebServlet("/servleti/main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String doLogout = req.getParameter("logout");
		if(doLogout != null && doLogout.equals("true")) {
			req.getServletContext().setAttribute("current.user.id", null);
		}
		req.getServletContext().setAttribute("users", DAOProvider.getDAO().getAllUsers());
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");
		BlogUser user = DAOProvider.getDAO().getBlogUser(nick);
		if(user == null) {
			req.setAttribute("err.nouser", "The specifed user does not exist.");
		} else {
			if(new PasswordChecker().check(user.getPasswordHash(), password)) {
				req.getServletContext().setAttribute("current.user.id", user.getId());
				req.getServletContext().setAttribute("current.user.fn", user.getFirstName());
				req.getServletContext().setAttribute("current.user.ln", user.getLastName());
				req.getServletContext().setAttribute("current.user.nick", user.getNick());
			} else {
				req.setAttribute("err.invalidpass", "Incorrect password.");
			}
		}
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
	
	
}
