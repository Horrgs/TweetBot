package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by horrgs on 9/17/15.
 */
public class Help implements SubCommand {
    @Override
    public void onCommand(Status status, String[] args) {
        if(status.getUser().getScreenName().equals("Horrgs")) {
            long r1 = status.getId();
            StatusUpdate statusUpdate = new StatusUpdate("https://testing.com/");
            statusUpdate.setInReplyToStatusId(r1);
            try {
                HorrgsTwitter.twitter.updateStatus(statusUpdate);
            } catch (TwitterException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Returns the list of TweetBot commmands.";
    }

    @Override
    public String getPermission() {
        return "tweetbot.user.help";
    }
}
