package notifier;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import notifier.parser.SRMCalendarParser;

import com.google.gson.Gson; 

@SuppressWarnings("serial")
public class SRMApiServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(SRMNotifierServlet.class
			.getName());
	private static final SimpleDateFormat format = SRMCalendarParser.getDataFormat();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		log.info("場所(Locale):" + Locale.getDefault());

		GregorianCalendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		log.info("[" + now + ":" + format.format(now) + "]");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Extent<SRM> extent = pm.getExtent(SRM.class, true);
		Gson gson = new Gson();
		List<SRM> srms = new ArrayList<SRM>();
		for (SRM srm : extent) {
			srms.add(srm);
		}
		extent.closeAll();
		String result = gson.toJson(srms.toArray(new SRM[0]));
		log.info("generated result: " + result);
		resp.getWriter().println(result);
	}

}
