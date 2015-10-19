package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.util.TweetTools;
import org.horrgs.twitterbot.util.Words;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

/**
 * Created by Horrgs on 9/27/2015.
 */
public class Define implements SubCommand {
    @Override
    public void onCommand(Status status, String[] args) {
        try {
            StatusUpdate statusUpdate = new StatusUpdate("");
            long r1 = statusUpdate.getInReplyToStatusId();
            if (args.length > 2) {
                String define = "";
                for (int x = 0; x < args.length; x++) {
                    define = "" + args[x];
                }
                define.replace(" ", "%20");
                Words words = new Words();
                String def = words.defineWord(define);
                boolean split = def.length() >= 130;
                if (split) {
                    String[] tweets = TweetTools.splitTweet(def);
                    for (int x = 0; x < tweets.length; x++) {
                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " \n" + tweets[x]);
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                        Thread.sleep(5000);
                    }
                } else {
                    statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + def);
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                }
            } else if (args.length == 2) {
                Words words = new Words();
                String def = words.defineWord(args[1]);
                boolean split = def.length() >= 130;
                if (split) {
                    String[] tweets = TweetTools.splitTweet(def);
                    for (int x = 0; x < tweets.length; x++) {
                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " \n" + tweets[x]);
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                        Thread.sleep(5000);
                    }
                } else {
                    statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + def);
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                }
            } else {
                //TODO: throw improper syntax.
            }
        } catch (TwitterException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "Define a word specified.";
    }

    @Override
    public String getPermission() {
        return "tweetbot.user.define";
    }
}
