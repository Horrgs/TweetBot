package org.horrgs.twitterbot.commands;

import com.rosaloves.bitlyj.Bitly;
import com.rosaloves.bitlyj.Url;
import org.horrgs.twitterbot.HorrgsTwitter;
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
    public boolean onCommand(Status status, String[] args) {
        StatusUpdate statusUpdate = new StatusUpdate("");
        long r1 = status.getId();
        if(status.getUser().getScreenName().equals("Horrgs")) {
            String[] topics = {"h", "w", "b", "n", "t", "el", "p", "e", "s", "m"};
            try {
                if(args.length == 0) {
                    statusUpdate = new StatusUpdate("You gave no argument.\nAn argument is required.\n%news <topic> [site]");
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                } else if(args.length == 1 && args[0].length() == 1) {
                    boolean contains = false;
                    char topic = args[0].toLowerCase().charAt(args[0].length());
                    for(int x = 0; x < topics.length; x++) {
                        if(topics[x].equals(String.valueOf(topic))) {
                            contains = true;
                        }
                    }
                    if(contains) {
                        NewsJSON[] newsJSON = getArticles("topic=", String.valueOf(topic));
                        for(int x = 0; x < newsJSON.length; x++) {
                            try {
                                Url url = Bitly.as("bitlyapidemo", "R_0da49e0a9118ff35f52f629d2d71bf07").call(Bitly.shorten(newsJSON[x].getStringURL(x)));
                                statusUpdate = new StatusUpdate(newsJSON[x].getTitle(x)+url.getShortUrl());
                                statusUpdate.setInReplyToStatusId(r1);
                                HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                Thread.sleep(3000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }


                        }
                    } else {
                        statusUpdate = new StatusUpdate("Error in executing \"news\" command.\nYou gave an invalid topic.");
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                    }
                }

            } catch (TwitterException ex) {
                ex.printStackTrace();
            }
        }
        return false;
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
        return site.get(getJSONObject(x), "title");
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
        for(int x = 0; x < 4; x++) {
            try {
                News news = new News();
                site.startJson(args, val);
                setJSONObject(x, new JSONObject(site.getJsonObject().getJSONObject("responseData").getJSONArray("results").getJSONObject(x)));
                newsJSON[x] = news;
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
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