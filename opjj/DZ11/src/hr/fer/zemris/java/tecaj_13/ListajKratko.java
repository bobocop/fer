package hr.fer.zemris.java.tecaj_13;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides a short list of (ID, Message title) pairs.
 */
@WebServlet("/servleti/listajKratko")
public class ListajKratko extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<Unos> unosi = DAOProvider.getDao().dohvatiOsnovniPopisUnosa();
		req.setAttribute("unosi", unosi);
		
		req.getRequestDispatcher("/WEB-INF/pages/ListajKratko.jsp").forward(req, resp);
	}
}
