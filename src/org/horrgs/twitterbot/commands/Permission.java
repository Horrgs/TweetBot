package org.horrgs.twitterbot.commands;

import twitter4j.Status;
import twitter4j.StatusUpdate;

/**
 * Created by Horrgs on 10/17/2015.
 */
public class Permission implements SubCommand {
    @Override
    public void onCommand(Status status, String[] args) {
        //TODO: lets do it so it doesn't go through tweets but rather DM's??
        //http://twitter4j.org/oldjavadocs/4.0.2/twitter4j/api/DirectMessagesResources.html#getDirectMessages--

    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getPermission() {
        return "tweetbot.admin.permission";
    }
}
