package hr.fer.zemris.java.tecaj_13;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.Unos;
import hr.fer.zemris.java.tecaj_13.webforms.UnosForm;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prepares a form for collecting the entered data.
 */
@WebServlet("/servleti/unos/*")
public class UnosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UnosForm uf = new UnosForm();
		req.setAttribute("path", req.getContextPath());
		req.setAttribute("savepath", req.getContextPath()+req.getServletPath()+"/save");
		System.out.println(req.getPathInfo().substring(1));
		switch(req.getPathInfo().substring(1)) {
		case "new":
			uf.fromDomainObject(new Unos());
			req.setAttribute("model.object", uf);
			req.setAttribute("action", "New");
			req.getRequestDispatcher("/WEB-INF/pages/unos.jsp").forward(req, resp);
			break;
		case "edit":
			long idParam = Long.parseLong(req.getParameter("id"));
			DAO dao = DAOProvider.getDao();
			uf.fromDomainObject(dao.dohvatiUnos(idParam));
			req.setAttribute("model.object", uf);
			req.setAttribute("action", "Edit");
			req.getRequestDispatcher("/WEB-INF/pages/unos.jsp").forward(req, resp);
			break;
		case "save":
			uf.fill(req);
			uf.checkErrors();
			if(!uf.hasError() && !uf.hasError("id")) {
				Unos u;
				if((u = DAOProvider.getDao()
						.dohvatiUnos(
								Long.parseLong(req.getParameter("id")
										))) != null) {
					// if there exists an entity with the provided id
					uf.toDomainObject(u);
					DAOProvider.getDao().update(u);
				}
			} else if(uf.hasError("id") && uf.getErrorCount() == 1){
				String id = uf.getId();
				if(id == null || id.equals("")) {
					Unos u = new Unos();
					uf.toDomainObject(u);
					DAOProvider.getDao().save(u);
				}
			} else {
				req.setAttribute("model.object", uf);
				req.setAttribute("action", "New");
				req.getRequestDispatcher("/WEB-INF/pages/unos.jsp").forward(req, resp);
				break;
			}
			resp.sendRedirect(req.getContextPath()+"/servleti/listajKratko");
			break;
		default:
			break;
		}
	}
}
