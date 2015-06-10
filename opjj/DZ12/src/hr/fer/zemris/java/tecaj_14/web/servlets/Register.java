package hr.fer.zemris.java.tecaj_14.web.servlets;

import hr.fer.zemris.java.tecaj_14.dao.DAO;
import hr.fer.zemris.java.tecaj_14.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_14.webforms.UserUnosForm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles the user registration process.
 * @author karlo
 *
 */
@WebServlet("/servleti/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		UserUnosForm uuf = new UserUnosForm();
		req.setAttribute("model.uuf", uuf);
		req.setAttribute("nick.used", "");
		
		uuf.fill(req);
		
		if(uuf.hasErrors()) {
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return;
		}
		
		DAO dao = DAOProvider.getDAO();
		if(dao.getBlogUser(uuf.getNick()) != null) {
			// the selected nickname is already in use
			req.setAttribute("nick.used", "Nick name '" + uuf.getNick() + "' already in use");
			uuf.setNick("");
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
		} else {
			dao.save(uuf.toBlogUser());
			req.getRequestDispatcher("/WEB-INF/pages/reg_success.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		UserUnosForm uuf = new UserUnosForm();
		req.setAttribute("model.uuf", uuf);
		req.setAttribute("nick.used", "");
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
}
