package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import java.util.Random;

/**
 * Created by horrgs on 9/17/15.
 */
public class FlipCoin implements SubCommand {

    @Override
    public boolean onCommand(Status status, String[] args) {
        if(status.getUser().getScreenName().equals("Horrgs")) {
            try {
                long r1 = status.getId();
                StatusUpdate statusUpdate = new StatusUpdate("");
                if (args.length == 1) {
                    if (args[0].equals("2/3") || args[0].equals("3/5")) {
                        Random random = new Random(2);
                        String flips[] = {};
                        char cha = args[0].charAt(3);
                        for (int x = 0; x < Integer.parseInt(String.valueOf(cha)); x++) {
                            int i = random.nextInt();
                            flips[x] = i == 0 ? "Heads" : "Tails";
                        }
                        String string = "";
                        for (int x = 0; x < flips.length; x++) {
                            if (x != 0) {
                                string = string + "/" + flips[x];
                            } else {
                                string = flips[x];
                            }
                        }
                        statusUpdate = new StatusUpdate("Coin Flipped!\nResults: " + string);
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    } else {
                        statusUpdate = new StatusUpdate("You gave an invalid argument.\n%flipcoin [arg1]\n[arg1] is optional" +
                                "but must be 2/3 or 3/5");
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    }
                } else {
                    Random r = new Random(2);
                    if(r.nextInt() == 0) {
                        statusUpdate = new StatusUpdate("Coin Flipped!\nResults: Heads");
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    } else {
                        statusUpdate = new StatusUpdate("Coin Flipped!\nResults: Tails");
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    }
                }
            } catch (TwitterException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "Flips a coin and will return the result(s).";
    }

    @Override
    public String getPermission() {
        return "tweetbot.user.flipcoin";
    }
}