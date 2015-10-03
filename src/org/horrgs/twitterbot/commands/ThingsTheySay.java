package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
import twitter4j.*;

import java.util.Random;

/**
 * Created by horrgs on 9/17/15.
 */
public class ThingsTheySay implements SubCommand {
    @Override
    public void onCommand(Status status, String[] args) {
        try {
            if(status.getUser().getScreenName().equals("Horrgs")) {
                StatusUpdate statusUpdate = new StatusUpdate("");
                if(args.length != 1) {
                    long r1 = status.getId();
                    statusUpdate = new StatusUpdate("Invalid arguments.\n%thingstheysay <handle1>");
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                } else {
                    long r1 = status.getId();
                    ResponseList tweets = HorrgsTwitter.twitter.getUserTimeline(args[0], new Paging(1, 200));
                    Random r = new Random(tweets.size());
                    Status status1 = (Status) tweets.get(r.nextInt());
                    statusUpdate = new StatusUpdate(args[1] + " \"" + status1.getText() + "\"");
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                }
            }
        } catch (TwitterException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "Responds with a random tweet tweeted by the requested party.";
    }

    @Override
    public String getPermission() {
        return "tweetbot.user.thingstheysay";
    }
}