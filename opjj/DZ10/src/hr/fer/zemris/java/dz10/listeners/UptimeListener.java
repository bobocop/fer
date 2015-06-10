package hr.fer.zemris.java.dz10.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Registers the moment the server was turned on. It is used by an applet
 * to calculate the uptime by compering current time to the stored value
 * name "uptime".
 */
public class UptimeListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("uptime", System.currentTimeMillis());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("uptime");
	}

}
