package org.horrgs.twitterbot.commands;

import twitter4j.Status;
import twitter4j.User;

/**
 * Created by horrgs on 9/17/15.
 */
public interface SubCommand {

    public boolean onCommand(Status status, String[] args);

    public String getHelp();

    public String getPermission();
}
