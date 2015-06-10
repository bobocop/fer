package hr.fer.zemris.java.dz10.servleti;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Used to change the background color of every page included in this application.
 */
public class SetColor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static List<String> allowedColors = Arrays.asList(
			"white", "green", "cyan", "red"
			);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pickedCol = req.getParameter("picked");
		if(pickedCol != null && allowedColors.contains(pickedCol)) {
			resp.setContentType("text/html; charset=UTF-8");
			req.getSession().setAttribute("pickedBgCol", pickedCol);
			Writer out = resp.getWriter();
			out.write("<html>" + TagGenerator.generateBodyBGColor(
					(String) req.getSession().getAttribute("pickedBgCol"))
					+ "<p>Changed color to <b>" + pickedCol + "</b></p><br>"
					+ "Return to <a href=\"index.jsp\">index</a>"
					+ "</body></html>");
		} else {
			// shouldn't ever get here
			req.getSession().setAttribute("pickedBgCol", "white");
			resp.getWriter().write("<html><body>Unsupported color, " +
					"using the default..." + "</body></html>");
		}
	}
}
