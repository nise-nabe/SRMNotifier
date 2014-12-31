package notifier;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.Calendar;


public class SRMTest{
	private SRM srm;

	@Before
	public void initialize(){
		srm = new SRM();
	}

	@Test
	public void name() {
		srm.setName("srm name");
		assertEquals("srm name", srm.getName());
	}

	@Test
	public void url() {
		srm.setUrl("srm url");
		assertEquals("srm url", srm.getUrl());
	}

	@Test
	public void registerTime() {
		Date date = Calendar.getInstance().getTime();
		srm.setRegisterTime(date);
		assertSame(date, srm.getRegisterTime());
	}

	@Test
	public void competisionTime() {
		Date date = Calendar.getInstance().getTime();
		srm.setCompetisionTime(date);
		assertEquals(date, srm.getCompetisionTime());
	}

	@Test
	public void count() {
		srm.setCount(100);
		assertEquals(100, srm.getCount());
	}
}