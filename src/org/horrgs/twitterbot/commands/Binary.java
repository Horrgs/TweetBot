package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.api.BinaryAPI;
import org.horrgs.twitterbot.api.FileManager;
import org.horrgs.twitterbot.util.Words;
import org.jpaste.exceptions.PasteException;
import org.jpaste.pastebin.Pastebin;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import java.net.URL;

/**
 * Created by horrg on 9/23/2015.
 */
public class Binary implements SubCommand {
    //TODO: allow the command sender to send a pastie and have that be translated.
    @Override
    public void onCommand(Status status, String[] args) {
        try {
            StatusUpdate statusUpdate = new StatusUpdate("");
            long r1 = status.getId();
            String[] props = {"toBinary", "fromBinary"};
            if (args.length >= 2) {
                boolean t = false;
                for (int x = 0; x < props.length; x++) {
                    if (props[x].contains(args[0])) {
                        t = true;
                        break;
                    }
                }
                if (t) {
                    BinaryAPI binaryAPI = new BinaryAPI();
                    String f = "";
                    for (int x = 2; x < args.length; x++) {
                        f = f + args[x];
                    }
                    switch (args[0]) {
                        case "fromBinary":
                            statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + binaryAPI.toText(f));
                            statusUpdate.setInReplyToStatusId(r1);
                            HorrgsTwitter.twitter.updateStatus(statusUpdate);
                            break;
                        case "toBinary":
                            try {
                                String apiKey = FileManager.getInstance().getKey("pasteBinKey");
                                Words words = new Words();
                                String[] x = words.getRandomWords(3);
                                String title = x[0] + x[1] + x[2];
                                URL url = Pastebin.pastePaste(apiKey, binaryAPI.toBinary(f), title);
                                statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + "Binary: " + url.toString());
                                statusUpdate.setInReplyToStatusId(r1);
                                HorrgsTwitter.twitter.updateStatus(statusUpdate);
                            } catch (PasteException ex) {
                                ex.printStackTrace();
                            }
                            break;

                    }
                }
            }
        } catch (TwitterException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "Translate things from or two Binary.";
    }

    @Override
    public String getPermission() {
        return "tweetbot.user.binary";
    }
}
