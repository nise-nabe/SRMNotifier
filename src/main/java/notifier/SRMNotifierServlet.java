package notifier;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import notifier.parser.CalendarParser;
import twitter4j.TwitterException;

public class SRMNotifierServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(SRMNotifierServlet.class.getName());
	private static final String hash = "#Topcoder #SRM";
	private static final SimpleDateFormat format = CalendarParser.getDataFormat();
	private static final String[] msgs = { "開始24時間前です", "開始12時間前です",
			"登録を開始しました", "開始1時間前です", "開始30分前です", "開始15分前です", "開始5分前です",
			"Coding Phase を開始しました", "Coding Phase を終了しました",
			"Challenge Phase を開始しました", "終了しました" };
	private static final long[] dates = { -toLong(24, 60), -toLong(12, 60),
			-toLong(4, 60), -toLong(1, 60), -toLong(1, 30), -toLong(1, 15),
			-toLong(1, 5), 0, toLong(1, 75), toLong(1, 80), toLong(1, 95) };

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		log.info("場所(Locale):" + Locale.getDefault());

		GregorianCalendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		log.info("[" + now + ":" + format.format(now) + "]");
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			log.info("更新時間（分）：" + cal.get(Calendar.MINUTE) + "　更新時間（時）：" + cal.get(Calendar.HOUR_OF_DAY));
			SRM srm = getNearestSRM(pm);
			if (srm == null) {
				log.info("通知対象 SRM なし");
				return;
			}
			log.info("srm :" + srm.toString());

			log.info("compeTime :" + format.format(srm.getCompetitionTime()));

			Date target = new Date(srm.getCompetitionTime().getTime() + dates[srm.getCount()]);
			log.info("通知判定 [now:" + format.format(now) + "].after[target:"
					+ format.format(target) + "]==" + now.after(target));
			log.info("通知判定 " + now.after(target));
			while (now.after(target)) {
				// 通知判定
				if (now.before(new Date(target.getTime() + toLong(1, 4)))) {
					String notifyDate = "at " + format.format(target);
					if (srm.getCount() < 8) {
						notifyDate = "開始時間: " + format.format(srm.getCompetitionTime());
					}
					post(msgs[srm.getCount()], srm, notifyDate);
				}
				srm.setCount(srm.getCount() + 1);
				// SRM終了判定
				if (srm.getCount() >= dates.length) {
					log.info(srm.getName() + "のデータを削除");
					pm.deletePersistent(srm);

					// 消すついでに次のSRMの時間も告知
					SRM nextSrm = getNearestSRM(pm);
					if (nextSrm != null) { // null チェックやめたい
						postNextSRM(nextSrm);
					}
					break;
				}
				target = new Date(srm.getCompetitionTime().getTime() + dates[srm.getCount()]);
			}
		} catch (Exception e) {
			log.log(Level.WARNING, "追加時にエラー", e);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	private SRM getNearestSRM(PersistenceManager pm) {
		Query query = pm.newQuery(SRM.class);
		query.setRange(0, 1);
		query.setOrdering("competitionTime");
		List<SRM> srms = (List<SRM>) query.execute();
		if (srms.isEmpty()) {
			return null;
		}
		SRM srm = srms.get(0);
		log.info("最近傍SRM取得:" + srm);
		return srm;
	}

	private void post(String msg, SRM srm, String date) throws TwitterException {
		// Twitter twitter;
		// SRM 463 終了しました at 2010年03月02日（火） 22時35分 #Topcoder #SRM
		String status = " " + msg + " " + date;
		if (2 <= srm.getCount() && srm.getCount() <= 7) {
			status += " WebArena -> https://arena.topcoder.com/";
		}
		status += " " + hash;
		if ((srm.getName() + status).length() > 140) {
			status = srm.getName().substring(0, 140 - status.length()) + status;
		} else {
			status = srm.getName() + status;
		}
		TwitterManager.post(status);
	}

	// 次のSRMをpostするため
	private void postNextSRM(SRM srm) throws TwitterException {
		// Twitter twitter;
		// 次の SRM000 は 20XX年XX月XX日（Ｘ） XX時XX分 からです #Topcoder #SRM
		String status = "次の " + srm.getName() + " は " + format.format(srm.getCompetitionTime()) + " からです " + hash;
		TwitterManager.post(status);
	}

	private static long toLong(int hour, int minute) {
		return hour * minute * 60 * 1000;
	}

}
