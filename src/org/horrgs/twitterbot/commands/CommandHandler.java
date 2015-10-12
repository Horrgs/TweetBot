package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import java.util.HashMap;

/**
 * Created by horrg on 9/19/2015.
 */
public class CommandHandler implements SubCommand {
    public HashMap<String, SubCommand> commands = new HashMap<>();

    public void loadCommands() {
        commands.put("binary", new Binary());
        commands.put("define", new Define());
        commands.put("flipcoin", new FlipCoin());
        commands.put("help", new Help());
        commands.put("news", new News());
        commands.put("thingstheysay", new ThingsTheySay());
        commands.put("weather", new Weather());
    }

    @Override
    public void onCommand(Status status, String[] args) {
        if(status.getUser().getScreenName().equals("Horrgs")) {
            if(status.getText().startsWith("%")) {
                String[] strings = status.getText().split(" ");
                args = strings;
                try {
                    if(!commands.containsKey(args[0].replace("%", ""))) {
                        StatusUpdate statusUpdate = new StatusUpdate("Command \"" + args[0] + "\" doesn't exist.");
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                        return;
                    }
                    try {
                        commands.get(args[0].replace("%", "")).onCommand(status, args);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } catch (TwitterException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getHelp() {
        return "tweetbot.user.performcommand";
    }

    @Override
    public String getPermission() {
        return "tweetbot.user.performcommand";
    }
}
