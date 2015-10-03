package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
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
        if(status.getUser().getScreenName().equals("Horrgs")) {
            try {
                StatusUpdate statusUpdate = new StatusUpdate("");
                long r1 = statusUpdate.getInReplyToStatusId();
                if(args.length > 1) {
                    String define = "";
                    for(int x = 0; x < args.length; x++) {
                        define = "" + args[x];
                    }
                    define.replace(" ", "%20");
                    Words words = new Words();
                    String def = words.defineWord(define);
                    statusUpdate = new StatusUpdate(def);
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                } else if(args.length == 1) {
                    Words words = new Words();
                    String def = words.defineWord(args[0]);
                    statusUpdate = new StatusUpdate(def);
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                } else {
                    //TODO: throw improper syntax.
                }
            } catch (TwitterException ex) {

            }
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
