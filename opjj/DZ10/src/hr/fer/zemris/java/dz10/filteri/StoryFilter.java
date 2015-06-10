package hr.fer.zemris.java.dz10.filteri;

import java.io.IOException;
import java.util.Calendar;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filters the requests based on a current time in minutes; if the number
 * is odd, it will display a "Not Available" page.
 */
public class StoryFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if(Calendar.getInstance().get(Calendar.MINUTE) % 2 == 0) {
			chain.doFilter(request, response);
		} else {
			request.getRequestDispatcher("not_avi.jsp").forward(
					request, response);
		}
	}

	@Override
	public void destroy() {}
}
