package org.horrgs.twitterbot.util;

import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.commands.CommandHandler;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by Horrgs on 10/16/2015.
 */
public class TweetListener implements Runnable {

    @Override
    public void run() {
        try {
            for(Status status : HorrgsTwitter.twitter.getHomeTimeline()) {
                if(status.getText().startsWith("%")) {
                    new CommandHandler().onCommand(status, null);
                    System.out.println("see tweet. #2");
                }
            }
        } catch (TwitterException ex) {
            ex.printStackTrace();
        }
    }
}
