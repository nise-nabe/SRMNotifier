package notifier.parser;

import static org.junit.Assert.*;

import java.util.List;

import notifier.SRM;

import org.junit.Test;

public class SRMCalendarParserTest {
	@Test
	public void instance() throws Exception {
		new SRMCalendarParser(null);
	}

	@Test
	public void getUrl() throws Exception {
		SRMCalendarParser parser = new SRMCalendarParser("http://community.topcoder.com/tc?module=Static&d1=calendar&d2=thisMonth");
		assertSame("http://community.topcoder.com/tc?module=Static&d1=calendar&d2=thisMonth", parser.getUrl());
	}

	@Test
	public void parseSRM() throws Exception {
		SRMCalendarParser parser = new SRMCalendarParser();
		List<SRM> srms = parser.parse("<html><div class=\"srm\"><strong><A href=\"/tc?module=MatchDetails&rd=14737\">SRM 545</A></strong><br />11:00</div></html>");
		assertSame(1, srms.size());
		SRM srm = srms.get(0);
		assertSame("SRM 545", srm.getName());
		assertSame("http://community.topcoder.com/tc?module=MatchDetails&rd=14737", srm.getUrl());
	}

	@Test
	public void printSRMs() throws Exception {
		SRMCalendarParser parser = new SRMCalendarParser("http://community.topcoder.com/tc?module=Static&d1=calendar&d2=thisMonth");
		for(SRM srm : parser.getSRMs()){
			System.out.println(srm);
		}
	}

}
