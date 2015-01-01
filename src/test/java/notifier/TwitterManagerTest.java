package notifier;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import twitter4j.TwitterFactory;

public class TwitterManagerTest {
	@Test
	@Ignore
	public void getAccount() throws Exception {
		String expected = "tc_srm_jp_bot";
		String actual  = new  TwitterFactory().getInstance().getScreenName();
		assertEquals(expected, actual);
	}

}
