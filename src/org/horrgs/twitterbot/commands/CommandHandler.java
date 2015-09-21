package org.horrgs.twitterbot.commands;

import java.util.HashMap;

/**
 * Created by horrg on 9/19/2015.
 */
public class CommandHandler {
    public HashMap<String, SubCommand> commands = new HashMap<>();

    public void loadCommands() {
        commands.put("flipcoin", new FlipCoin());
        commands.put("help", new Help());
        commands.put("news", new News());
        commands.put("thingstheysay", new ThingsTheySay());
    }
}
