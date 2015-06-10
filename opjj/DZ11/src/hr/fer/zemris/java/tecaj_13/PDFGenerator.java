package hr.fer.zemris.java.tecaj_13;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.Unos;
import hr.fer.zemris.java.tecaj_13.webforms.UnosForm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Creates a PDF document in which it writes down detailed information
 * about all the records in the storage.
 */
@WebServlet("/servleti/toPdf")
public class PDFGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/pdf");
		resp.setHeader("Content-Disposition", "attachment; " 
				+ "filename=\"records.pdf\"");
		List<Unos> unosi = DAOProvider.getDao().dohvatiOsnovniPopisUnosa();
		List<Long> ids = new ArrayList<Long>();
		for(Unos u : unosi) {
			ids.add(u.getId());
		}
		try {
			Document doc = new Document();
			PdfWriter writer = PdfWriter.getInstance(doc, resp.getOutputStream());
			doc.open();
			for(Long id : ids) {
				UnosForm uf = new UnosForm();
				uf.fromDomainObject(DAOProvider.getDao().dohvatiUnos(id));
				Paragraph p = new Paragraph(
						"Title: " + uf.getTitle() + "\n"
								+ "Message: "+ uf.getMessage() + "\n"
								+ "Created On: "+uf.getCreatedOn() + "\n"
								+ "User E-Mail: "+uf.getUserEMail());
				p.setFont(FontFactory.getFont("Arial", BaseFont.IDENTITY_H));
				doc.add(p);
				doc.newPage();
			}
			doc.close();
			writer.close();
		} catch (DocumentException e) {
			System.err.println("Unable to write to the PDF document");
		}
	}
}
