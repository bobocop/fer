package hr.fer.zemris.java.dz10.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prints the range of numbers and their squares. The range is determined
 * by the passed arguments 'a' and 'b'. If a < b, their values are switched.
 * The range is at most 20 units long.
 */
public class SquareRange extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String aStr = req.getParameter("a");
		String bStr = req.getParameter("b");
		int a = (aStr != null) ? Integer.parseInt(aStr) : 0;
		int b = (bStr != null) ? Integer.parseInt(bStr) : 20;
		
		if(a > b) {
			int tmp = b;
			b = a;
			a = tmp;
		}
		if(a + 20 < b) {
			b = a + 20;
		}
		
		int[] results = new int[b-a+1+1];	// 1 to store 'a'
		for(int i = 0; i < results.length; i++) {
			results[i] = (a+i)*(a+i);
		}
		
		results[results.length-1] = a;
		
		req.setAttribute("sqResults", results);
		req.getRequestDispatcher("squares.jsp").forward(req, resp);
	}
}
