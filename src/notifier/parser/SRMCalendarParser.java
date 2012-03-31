package notifier.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import notifier.SRM;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

public class SRMCalendarParser {
	private static final Logger log = Logger.getLogger(SRMCalendarParser.class
			.getName());
	private static final SimpleDateFormat format;
	static {
		format = new SimpleDateFormat("yyyy年MM月dd日（E） HH時mm分", Locale.JAPAN);
		format.setTimeZone(TimeZone.getTimeZone("GMT+09:00"));
	}

	private String url;

	public SRMCalendarParser(String url) {
		this.url = url;
	}

	public List<SRM> getSRMs() {
		ArrayList<SRM> result = new ArrayList<SRM>();
		try {
			Parser parser = new Parser(url);
			log.info("カレンダー取得 from " + url);
			NodeList list = parser
					.parse(new HasAttributeFilter("class", "srm"));
			SimpleNodeIterator it = list.elements();
			while (it.hasMoreNodes()) {
				NodeList children = it.nextNode().getChildren();
				children.keepAllNodesThatMatch(new TagNameFilter("a"));
				Node a = children.elementAt(0);
				if (a instanceof LinkTag) {
					LinkTag link = (LinkTag) a;
					SRM srm = new SRM();
					srm.setName(link.toPlainTextString());
					srm.setUrl(link.getLink());
					List<Date> dates = getTimes(srm.getUrl());
					srm.setRegisterTime(dates.get(0));
					srm.setCompetisionTime(dates.get(1));
					srm.setCount(0);
					result.add(srm);
					log.info("SRMデータ解析 : " + srm);
				}
			}
		} catch (Exception e) {
			log.warning(e.getMessage());
		}
		return result;
	}

	private List<Date> getTimes(String url) {
		ArrayList<Date> dates = new ArrayList<Date>();
		try {
			Parser parser = new Parser(url);
			NodeList list = parser.parse(new HasAttributeFilter("class",
					"statText"));
			SimpleNodeIterator it = list.elements();
			String day = "";
			while (it.hasMoreNodes()) {
				Node node = it.nextNode();
				String text = node.toPlainTextString().replaceAll("\\s", "");
				if (Pattern.matches("..\\..+", text)) {
					day = text;
				} else {
					SimpleDateFormat format2 = new SimpleDateFormat(
							"MM.dd.yyyyhh:mmaz", Locale.US);
					Date parse = format2.parse(day + text);
					dates.add(parse);
					log.info("データ取得 from " + url + ",date=" + day + text
							+ " to " + parse + " and " + format.format(parse));

				}
			}
		} catch (Exception e) {
		}
		return dates;
	}

}
