package notifier;

import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterManager {
	private static final Logger log = Logger.getLogger(TwitterManager.class
			.getName());

	public static boolean post(String status) {
		Twitter twitter = getTwitterAccount();
		try {
			twitter.updateStatus(status);
			log.info("post message :" + status);
		} catch (TwitterException e) {
			e.printStackTrace();
			log.warning(e.getMessage());
			return false;
		}
		return true;
	}

	private static Twitter getTwitterAccount() {
		TwitterFactory twitterFactory = new TwitterFactory();
		Twitter twitter = twitterFactory.getInstance();
		return twitter;
	}

}
