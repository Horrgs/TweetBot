package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.api.FileManager;
import org.horrgs.twitterbot.api.Site;
import org.horrgs.twitterbot.io.WeatherJSON;
import org.horrgs.twitterbot.util.TweetTools;
import org.json.JSONException;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

/**
 * Created by horrg on 9/19/2015.
 */
public class Weather implements SubCommand, WeatherJSON.Alerts, WeatherJSON.Conditions, WeatherJSON.Forecast {
    //TODO: add day support for forecast. There are 4 jObjects under forecastday array.
    String city = "New York City", state = "New York", weathertype = "conditions";
    Site site = new Site("http://api.wunderground.com/api/" + FileManager.getInstance().getKey("weatherApiKey") + "/" + weathertype
            + "/q/" + state.replace(" ", "%20") + "/" + city.replace(" ", "%20") + ".json");
    @Override
    public void onCommand(Status status, String[] args) {
        StatusUpdate statusUpdate = new StatusUpdate("");
        long r1 = status.getId();
        try {
            if (args.length != 4) {
                statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " " + "You gave an invalid argument.\n%weather <city> <state initials> <weathertype>");
                statusUpdate.setInReplyToStatusId(r1);
                HorrgsTwitter.twitter.updateStatus(statusUpdate);
            } else {
                city = args[1];
                state = args[2];
                weathertype = args[3].toLowerCase();
                site = new Site("http://api.wunderground.com/api/" + FileManager.getInstance().getKey("weatherApiKey") + "/" + weathertype
                        + "/q/" + state.replace(" ", "%20") + "/" + city.replace(" ", "%20") + ".json");
                String[] allowedWeatherTypes = {"alert", "condition", "forecast"};
                boolean isGood = false;
                for (int x = 0; x < allowedWeatherTypes.length; x++) {
                    if (allowedWeatherTypes[x].contains(weathertype.replace("s", ""))) {
                        isGood = true;
                    }
                }
                if (isGood) {
                    site.openURL(site.getURL(null));
                    try {
                        switch (weathertype.replace("s", "")) {
                            case "alert":
                                String tweet = "WEATHER ALERT\n" + "Description: " + getDescription() + "\nBegan At: " + dateSet() + "\nExpires At: " + dateExpires();
                                boolean split = tweet.length() >= 130;
                                if (split) {
                                    String[] tweets = TweetTools.splitTweet(tweet);
                                    for (int x = 0; x < tweets.length; x++) {
                                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + "\n" + tweets[x]);
                                        statusUpdate.setInReplyToStatusId(r1);
                                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                        Thread.sleep(5000);
                                    }
                                } else {
                                    statusUpdate = new StatusUpdate(tweet);
                                    statusUpdate.setInReplyToStatusId(r1);
                                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                }
                                break;
                            case "condition":
                                tweet = "Temp: " + getFTemp() + "F\nFeels Like: " + getFeelsLike() + "F\nForecast: " + getForecast() + "\nPrecipitation: " + getPrecipitation() + " inches\nWind: " +
                                        getWind() + "MPH\nWind Gusts: " + getWindGusts() + "MPH";
                                split = tweet.length() >= 130;
                                if (split) {
                                    String[] tweets = TweetTools.splitTweet(tweet);
                                    for (int x = 0; x < tweets.length; x++) {
                                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + "\n" + tweets[x]);
                                        statusUpdate.setInReplyToStatusId(r1);
                                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                        Thread.sleep(5000);
                                    }
                                } else {
                                    statusUpdate = new StatusUpdate(tweet);
                                    statusUpdate.setInReplyToStatusId(r1);
                                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                }
                                break;
                            case "forecast":
                                tweet = "Temp: " + getAccuHighFahrenheit() + "F/" + getAccuLowFahrenheit() + "F\nOutlook: " + getAccuConditions() + "\nChance of Precip.: " + getAccuPrecipPossibility() + "%\n" +
                                        "Wind: " + getMaxWind() + " MPH max/ " + getAvgWind() + " MPH avg\nHumidity: " + getAvgHumidity() + "%";
                                split = tweet.length() >= 130;
                                if (split) {
                                    String[] tweets = TweetTools.splitTweet(tweet);
                                    for (int x = 0; x < tweets.length; x++) {
                                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + "\n" + tweets[x]);
                                        statusUpdate.setInReplyToStatusId(r1);
                                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                        Thread.sleep(5000);
                                    }
                                } else {
                                    statusUpdate = new StatusUpdate(tweet);
                                    statusUpdate.setInReplyToStatusId(r1);
                                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                }
                                break;
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (TwitterException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "Returns the weather type for the specified city.";
    }

    @Override
    public String getPermission() {
        return "tweetbot.user.weather";
    }

    /*
    ALERTS
     */

    @Override
    public String getDescription() throws JSONException {
        return site.get(site.getJsonObject().getJSONArray("alerts").getJSONObject(0), "description");
    }

    @Override
    public String dateSet() throws JSONException  {
        return site.get(site.getJsonObject().getJSONArray("alerts").getJSONObject(0), "date");
    }

    @Override
    public String dateExpires() throws JSONException {
        return site.get(site.getJsonObject().getJSONArray("alerts").getJSONObject(0), "expires");
    }

    /*
    CONDITIONS
     */

    @Override
    public double getFTemp() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("current_observation"), "temp_f");
    }

    @Override
    public double getWind() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("current_observation"), "wind_mph");
    }

    @Override
    public String getHumidity() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("current_observation"), "relative_humidity");
    }

    @Override
    public double getFeelsLike() throws JSONException {
        /*
        Please refer to https://github.com/Horrgs/WeatherTweets/blob/master/src/org/horrgs/weathertweets/wunderground/WGLookup.java#L141
        if error.
         */
        return site.get(site.getJsonObject().getJSONObject("current_observation"), "feelslike_f");
    }

    @Override
    public String getForecast() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("current_observation"), "weather");
    }

    @Override
    public double getWindGusts() throws JSONException {
        //Please refer to https://github.com/Horrgs/WeatherTweets/blob/master/src/org/horrgs/weathertweets/wunderground/WGLookup.java#L131
        //if error.
        return site.get(site.getJsonObject().getJSONObject("current_observation"), "wind_gust_mph");
    }

    @Override
    public String getPrecipitation() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("current_observation"), "precip_1hr_in");
    }

    /*
    FORECAST
     */

    @Override
    public String getAccuDate() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("date"), "pretty");
    }

    @Override
    public String getAccuHighFahrenheit() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("high"), "fahrenheit");
    }

    @Override
    public String getAccuLowFahrenheit() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("low"), "fahrenheit");
    }

    @Override
    public String getAccuConditions() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0), "conditions");
    }

    @Override
    public int getAccuPrecipPossibility() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0), "pop");
    }

    @Override
    public int getSnowAllDay() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("in"), "snow_allday");
    }

    @Override
    public int getSnowNight() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("in"), "snow_night");
    }

    @Override
    public int getMaxWind() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("mph"), "maxwind");
    }

    @Override
    public int getAvgWind() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("mph"), "avewind");
    }

    @Override
    public int getAvgHumidity() throws JSONException {
        return site.get(site.getJsonObject().getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0), "avehumidity");
    }
}