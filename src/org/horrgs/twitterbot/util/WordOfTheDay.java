package org.horrgs.twitterbot.util;

import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.io.Word;
import org.json.JSONException;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

/**
 * Created by Horrgs on 10/12/2015.
 */
public class WordOfTheDay implements Runnable {

    @Override
    public void run() {
        Word.WordOfTheDay words = new Words();
        try {
            String[] status = new TweetTools().splitTweet("Word: " + words.getWordOTD() + "\n" + "Definition: " + words.getWDefinition() + "\n" + "PoS: " + words.getWPartOfSpeech());
            for (int x = 0; x < status.length; x++) {
                StatusUpdate statusUpdate = new StatusUpdate("[" + x + "/" + status.length + "]\n" + status[x]);
                HorrgsTwitter.twitter.updateStatus(statusUpdate);
                Thread.sleep(5000);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (TwitterException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
