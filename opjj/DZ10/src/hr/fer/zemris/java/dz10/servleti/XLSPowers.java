package hr.fer.zemris.java.dz10.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * Provides a Microsoft Excel document that contains 'n' sheets. On each sheet,
 * a range of numbers between 'a' and 'b' (inclusive) is printed as well as
 * their n-th powers. a, b and n are provided as arguments when calling the applet.<br>
 * Constraints:<br>
 * 1. a, b in [-100, 100]<br>
 * 2. b > a<br>
 * 3. n in [1, 5]<br>
 * An error page is displayed of the constraints are not respected.
 */
public class XLSPowers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String aParam = null;
		String bParam = null;
		String nParam = null;
		if((aParam = req.getParameter("a")) == null
				|| (bParam = req.getParameter("b")) == null
				|| (nParam = req.getParameter("n")) == null) {
			req.getRequestDispatcher("/aplikacija2/powers_invalid_params.jsp");
		}
		
		int a = Integer.parseInt(aParam);
		int b = Integer.parseInt(bParam);
		int n = Integer.parseInt(nParam);
		
		if((a < -100 || a > 100)
				|| (b < -100 | b > 100)
				|| (n < 1 || n > 5)
				|| (b < a)) {
			req.getRequestDispatcher("/aplikacija2/powers_invalid_params.jsp");
		}
		
		// generate the XLS document
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheetRef;
		HSSFRow rowRef;
		
		for(int i = 1; i <= n; i++) {
			sheetRef = hwb.createSheet(Integer.valueOf(i).toString());
			for(int j = 0; j < b-a+1; j++) {
				rowRef = sheetRef.createRow(j);
				rowRef.createCell(0).setCellValue(a+j);
				rowRef.createCell(1).setCellValue(Math.pow(a+j, i));
			}
		}
		
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; " +
				"filename=\"powers_tables.xls\"");
		hwb.write(resp.getOutputStream());
	}

}
