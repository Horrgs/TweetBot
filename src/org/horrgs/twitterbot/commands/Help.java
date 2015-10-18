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
        StatusUpdate statusUpdate = new StatusUpdate("");
        long r1 = status.getId();
        if(args.length == 1) {
            statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " here are a list of commands: http://pastebin.com/5y1MtE6f");
            statusUpdate.setInReplyToStatusId(r1);
            try {
                HorrgsTwitter.twitter.updateStatus(statusUpdate);
            } catch (TwitterException ex) {
                ex.printStackTrace();
            }
        } else if(args.length == 2) {
            String command = args[1];
            try {
                Class<?> myClass = Class.forName("org.horrgs.twiterbot.commands." + command);
                Object a = myClass.newInstance();
                if (a instanceof SubCommand) {
                    SubCommand subCommand = (SubCommand ) a;
                    statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + command + " - " + subCommand.getHelp());
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | TwitterException ex) {
                ex.printStackTrace();
            }
        } else {
            //TODO: invalid syntax.
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
