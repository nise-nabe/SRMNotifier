package notifier;

import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

// 2010/5/27 3:00 作成
// 2011/1/21 3:00 twitter4j-2.1.12でtwitter4j.propertiesに情報載っける
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

	// 更新履歴
	// 2010/5/6 4:40 OAuthに対応するついでにメソッド化
	private static Twitter getTwitterAccount() {
		TwitterFactory twitterFactory = new TwitterFactory();
		Twitter twitter = twitterFactory.getInstance();
		return twitter;
	}

}
