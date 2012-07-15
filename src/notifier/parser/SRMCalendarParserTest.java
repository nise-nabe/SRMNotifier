package notifier.parser;

import static org.junit.Assert.*;

import java.io.IOException;

import notifier.SRM;

import org.junit.Test;

public class SRMCalendarParserTest {
	@Test
	public void instance() throws Exception {
		new SRMCalendarParserMock(null);
	}

	@Test
	public void getUrl() throws Exception {
		SRMCalendarParserMock parser = new SRMCalendarParserMock("http://community.topcoder.com/tc?module=Static&d1=calendar&d2=thisMonth");
		assertSame("http://community.topcoder.com/tc?module=Static&d1=calendar&d2=thisMonth", parser.getUrl());
	}

	@Test
	public void printSRMs() throws Exception {
		SRMCalendarParserMock parser = new SRMCalendarParserMock("http://community.topcoder.com/tc?module=Static&d1=calendar&d2=thisMonth");
		for(SRM srm : parser.getSRMs()){
			System.out.println(srm);
		}
	}
}

class SRMCalendarParserMock extends SRMCalendarParser{
	public SRMCalendarParserMock(String url) {
		super(url);
	}

	@Override
	protected String getContent(String url) throws IOException{
		return super.getContent(url);
	}
}
