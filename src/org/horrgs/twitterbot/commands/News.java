package org.horrgs.twitterbot.commands;

import com.rosaloves.bitlyj.Bitly;
import com.rosaloves.bitlyj.Url;
import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.api.FileManager;
import org.horrgs.twitterbot.api.Site;
import org.horrgs.twitterbot.io.NewsJSON;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import java.net.URL;

/**
 * Created by horrgs on 9/18/15.
 */
public class News implements SubCommand, NewsJSON {
    Site site = new Site("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&");
    @Override
    public void onCommand(Status status, String[] args) {
        StatusUpdate statusUpdate = new StatusUpdate("");
        long r1 = status.getId();
        String[] topics = {"h", "w", "b", "n", "t", "el", "p", "e", "s", "m"};
        try {
            if (args.length == 1) {
                statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + "@" + status.getUser().getScreenName() + " " + "You gave no argument.\nAn argument is required.\n%news <topic> [site]");
                statusUpdate.setInReplyToStatusId(r1);
                HorrgsTwitter.twitter.updateStatus(statusUpdate);
            } else if (args.length == 2 && args[1].length() <= 2) {
                boolean contains = false;
                char topic = args[1].toLowerCase().charAt(0);
                for (int x = 0; x < topics.length; x++) {
                    if (topics[x].equals(String.valueOf(topic))) {
                        contains = true;
                        break;
                    }
                }
                NewsJSON[] newsJSON = getArticles("topic=", String.valueOf(topic));
                if (contains) {
                    for (int x = 0; x < newsJSON.length; x++) {
                        try {
                            String url = newsJSON[x].getStringURL(x);
                            //Url url = Bitly.as(FileManager.getInstance().getKey("bitlyApiUsername"), FileManager.getInstance().getKey("bitlyApiKey")).call(Bitly.shorten(newsJSON[x].getStringURL(x)));
                            statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + newsJSON[x].getTitle(x) + " " + url);
                            statusUpdate.setInReplyToStatusId(r1);
                            HorrgsTwitter.twitter.updateStatus(statusUpdate);
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }


                    }
                } else {
                    statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + "Error in executing \"news\" command.\nYou gave an invalid topic.");
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                }
            }

        } catch (TwitterException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "Get's the news on the specified topic from the (optional) specified source.";
    }

    @Override
    public String getPermission() {
        return "tweetbot.user.news";
    }

    @Override
    public String getTitle(int x) {
        return site.get(getJSONObject(x), "title").toString().replace("&#39;", "'");
    }

    @Override
    public String getStringURL(int x) {
        return site.get(getJSONObject(x), "unescapedUrl");
    }

    @Override
    public String getPublisher(int x) {
        return site.get(getJSONObject(x), "");
    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public String setURL(String url) {
        return null;
    }

    @Override
    public String setURL(URL url) {
        return null;
    }

    public NewsJSON[] getArticles(String args, String val) {
        NewsJSON[] newsJSON = new NewsJSON[4];
        site.startJson(false, args, val);
        try {
            Thread.sleep(3000);
            for(int x = 0; x < 4; x++) {
                try {
                    setJSONObject(x, site.getJsonObject().getJSONObject("responseData").getJSONArray("results").getJSONObject(x));
                    newsJSON[x] = this;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return newsJSON;
    }

    private JSONObject[] jsonObject = new JSONObject[4];
    public JSONObject getJSONObject(int x) {
        return jsonObject[x];
    }

    public void setJSONObject(int obj, JSONObject jsonObject) {
        this.jsonObject[obj] = jsonObject;
    }
}