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
    public void onCommand(Status status, String[] args) {
        if(status.getUser().getScreenName().equals("Horrgs")) {
            try {
                long r1 = status.getId();
                StatusUpdate statusUpdate = new StatusUpdate("");
                System.out.println(args.length);
                System.out.println(args[1]);
                if (args.length == 2) {
                    if (args[1].equals("2/3") || args[1].equals("3/5")) {
                        Random random = new Random(2);
                        String flips[] = new String[args[1].length()];
                        System.out.println(args[1].length());
                        for (int x = 0; x < Integer.parseInt(String.valueOf(args[1].length())); x++) {
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
                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + "Coin Flipped!\nResults: " + string);
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    } else {
                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + "You gave an invalid argument.\n%flipcoin [arg1]\n[arg1] is optional" +
                                "but must be 2/3 or 3/5");
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    }
                } else {
                    Random r = new Random(2);
                    if(r.nextInt() == 0) {
                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + "Coin Flipped!\nResults: Heads");
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    } else {
                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + "Coin Flipped!\nResults: Tails");
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    }
                }
            } catch (TwitterException ex) {
                ex.printStackTrace();
            }
        }
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