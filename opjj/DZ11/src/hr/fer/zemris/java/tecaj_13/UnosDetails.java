package hr.fer.zemris.java.tecaj_13;

import java.io.IOException;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.Unos;
import hr.fer.zemris.java.tecaj_13.webforms.UnosForm;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Displays a detailed information about a particular record.
 */
@WebServlet("/servleti/details")
public class UnosDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Unos u = DAOProvider.getDao().dohvatiUnos(
				Long.parseLong((String) req.getParameter("id")));
		UnosForm uf = new UnosForm();
		uf.fromDomainObject(u);
		req.setAttribute("path", req.getContextPath()+req.getServletPath());
		req.setAttribute("id", uf.getId());
		req.setAttribute("title", uf.getTitle());
		req.setAttribute("message", uf.getMessage());
		req.setAttribute("createdOn", uf.getCreatedOn());
		req.setAttribute("userEMail", uf.getUserEMail());
		req.getRequestDispatcher("/WEB-INF/pages/RecordDetails.jsp").forward(req, resp);
	}


	
}
