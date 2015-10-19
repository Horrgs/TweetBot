package org.horrgs.twitterbot.util;

import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.commands.CommandHandler;
import twitter4j.TwitterException;
import twitter4j.Status;

/**
 * Created by Horrgs on 10/16/2015.
 */
public class TweetListener implements Runnable {

    @Override
    public void run() {
        try {
            for(Status status : HorrgsTwitter.twitter.getHomeTimeline()) {
                TweetTools tweetTools = new TweetTools();
                if(status.getText().startsWith("%")) {
                    if (!tweetTools.hasRespondedToTweet(status.getId())) {
                        new CommandHandler().onCommand(status, null);
                    }
                }
            }
        } catch (TwitterException ex) {
            ex.printStackTrace();
        }
    }
}
