package notifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import notifier.parser.CalendarParser;
import notifier.parser.GoogleCalendarParser;

public class SRMUpdaterServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(SRMNotifierServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		GregorianCalendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			log.info("[" + now + ":" + CalendarParser.getDataFormat().format(now) + "]");
			updateSRM(pm, now);
		} catch (Exception e) {
			log.log(Level.WARNING, "更新時にエラー", e);
		} finally {
			pm.close();
		}
	}

	private void updateSRM(PersistenceManager pm, Date now) {
		CalendarParser parser = new GoogleCalendarParser();
		List<SRM> updates = parser.getSRMs();
		Extent<SRM> extent = pm.getExtent(SRM.class, true);
		for (SRM srm : extent) {
			log.info("格納済みデータ" + srm);
			// check update
			for (SRM update : new ArrayList<>(updates)) {
				if ((srm.getKey().equals(update.getKey())) && !srm.equals(update)) {
					srm.update(update);
					updates.remove(update);
					log.info(srm.getName() + " のデータを更新 to " + srm);
				} else if (srm.equals(update)) {
					updates.remove(update);
				}
			}
		}
		for (SRM update : updates) {
			log.info("追加する？ :" + "[update=" + update + "]" + "[now=" + now
					+ "] u.isafter(n) " + update.getRegisterTime().after(now));
			if (update.getRegisterTime().after(now)) {
				try {
					pm.makePersistent(update);
					log.info(update.getName() + " のデータを追加 " + update);
				} catch (Exception e) {
					log.log(Level.WARNING, "追加時にエラー", e);
				}
			}
		}
	}

}
