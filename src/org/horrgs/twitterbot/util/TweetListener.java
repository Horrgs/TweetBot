package org.horrgs.twitterbot.util;

import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.commands.CommandHandler;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.StatusUpdate;
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
                    System.out.println(status.getUser().getId());
                }
            }
           /* for(DirectMessage directMessage : HorrgsTwitter.twitter.getDirectMessages()) {
                if(TweetTools.hasPermission(directMessage.getSenderId(), "tweetbot.admin.permission")) {
                    //new CommandHandler().onCommand();
                }
            }  */
        } catch (TwitterException ex) {
            ex.printStackTrace();
        }
    }
}
