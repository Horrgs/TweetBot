package org.horrgs.twitterbot.util;

import com.google.gson.*;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Horrgs on 10/12/2015.
 */
public class TweetTools {

    public static String[] splitTweet(String status) {
        String[] tweets = new String[Integer.valueOf(status.length() / 130 + 1)];
        if (status.length() < 140) {
            tweets[0] = status;
            return tweets;
        } else {
            HashMap<Integer, Character> periodMap = new HashMap<>();
            for(int x = 0; x < status.length(); x++) {
                if(status.charAt(x) == '.' || status.charAt(x) == ',' || status.charAt(x) == '?' || status.charAt(x) == '!' || status.charAt(x) == '\n') {
                    periodMap.put(x, status.charAt(x));
                }
            }
            int highestPeriods[] = new int[tweets.length];
            ArrayList<Integer> ss = new ArrayList<>();
            for(int x = 0; x < tweets.length; x++) {
                int starting = x == 0 ? 0 : x * 130;
                for(int a = starting; a < starting + 130; a++) {
                    if(periodMap.containsKey(a)) {
                        ss.add(a);
                    }
                }
                Collections.sort(ss);
                highestPeriods[x] = ss.get(ss.size() - 1);
                ss.clear();
                starting = x == 0 ? 0 : highestPeriods[x - 1] + 2;
                int end = x == tweets.length + 1 ? tweets.length : highestPeriods[x];
                for(int c = starting; c < end + 1; c++) {
                    tweets[x] = tweets[x] + status.charAt(c);
                }
                tweets[x] = tweets[x].replace("null", "");
            }
        }
        return tweets;
    }

    public static boolean hasPermission(long longId, String permission) {
        JsonObject jsonObject = null;
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader("permissions.json"));
            jsonObject = (JsonObject) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(jsonObject != null && jsonObject.get(permission) != null) {
            for(int x = 0; x < jsonObject.get(permission).getAsJsonArray().size(); x++) {
                if(jsonObject.get(permission).getAsJsonArray().get(x).getAsJsonObject().get("id").getAsLong() == longId) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addRespondedToTweet(long tweetId) {
        JsonArray jsonArray = null;
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader("tweets.json"));
            jsonArray = (JsonArray) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        TweetFormat tweetFormat = new TweetFormat(tweetId);
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(tweetFormat);
        try {
            if(jsonArray != null) {
                jsonArray.add(jsonElement);
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tweets.json"));
                bufferedWriter.write(jsonArray.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean hasRespondedToTweet(long tweetId) {
        JsonArray jsonArray = null;
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader("tweets.json"));
            jsonArray = (JsonArray) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(jsonArray != null) {
            for(int x = 0; x < jsonArray.size(); x++) {
                if(jsonArray.get(x).getAsJsonObject().get("id").getAsLong() == tweetId) {
                    return true;
                }
            }
        }
        return false;
    }

    public class TweetFormat {
        private long id;
        public TweetFormat(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
