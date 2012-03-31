package notifier.parser;

import static org.junit.Assert.*;

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
	public void printSRMs() throws Exception {
		SRMCalendarParser parser = new SRMCalendarParser("http://community.topcoder.com/tc?module=Static&d1=calendar&d2=thisMonth");
		for(SRM srm : parser.getSRMs()){
			System.out.println(srm);
		}
	}

}
