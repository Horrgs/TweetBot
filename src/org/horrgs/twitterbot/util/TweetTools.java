package org.horrgs.twitterbot.util;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Horrgs on 10/12/2015.
 */
public class TweetTools {


    public String[] splitTweet(String status) {
        String[] tweets = new String[Integer.valueOf(status.length() / 130)];
        if (status.length() < 140) {
            tweets[0] = status;
            return tweets;
        } else {
            HashMap<Integer, Character> periodMap = new HashMap<>();
            for(int a = 0; a < tweets.length; a++) {
                for (int x = 130; x != 0; x--) {
                    if (status.charAt(x) == '.' || status.charAt(x) == '?' || status.charAt(x) == ',') {
                        periodMap.put(x, status.charAt(x));
                    }
                }
                boolean s = a * 130 < a * 130 + 130;
                for(int x = a * 130; x < a * 130 + 130; x++) {
                    s = periodMap.containsKey(x);
                }
                if(s) {
                    for(int c = 130 * a; c < 130 * a + 130; c++) {
                        tweets[a] = tweets[a] + status.charAt(a);
                    }
                }
            }
        }
        return tweets;
    }


}
