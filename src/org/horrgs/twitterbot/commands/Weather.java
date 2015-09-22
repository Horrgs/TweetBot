package org.horrgs.twitterbot.commands;

import org.horrgs.twitterbot.HorrgsTwitter;
import org.horrgs.twitterbot.api.FileManager;
import org.horrgs.twitterbot.api.Site;
import org.horrgs.twitterbot.io.WeatherJSON;
import org.json.JSONException;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

/**
 * Created by horrg on 9/19/2015.
 */
public class Weather implements SubCommand, WeatherJSON.Alerts, WeatherJSON.Conditions, WeatherJSON.Forecast {
    //TODO: add day support for forecast. There are 4 jObjects under forecastday array.
    String city, state, weathertype;
    Site site = new Site("http://api.wunderground.com/api/" + FileManager.getInstance().getWeatherApiKey() + "/" + weathertype
            + "/q/" + state + "/" + city.replace(" ", "%20") + ".json");
    @Override
    public boolean onCommand(Status status, String[] args) {
        StatusUpdate statusUpdate = new StatusUpdate("");
        long r1 = status.getId();
        if(status.getUser().getScreenName().equals("Horrgs")) {
            try {
                if(args.length != 3) {
                    statusUpdate = new StatusUpdate("You gave an invalid argument.\n%weather <city> <state initials> <weathertype>");
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                } else {
                    city = args[0];
                    state = args[1];
                    weathertype = args[2].toLowerCase();
                    String[] allowedWeatherTypes = {"alert", "condition", "forecast"};
                    boolean isGood = false;
                    for(int x = 0; x < allowedWeatherTypes.length; x++) {
                        if(allowedWeatherTypes[x].contains(weathertype.replace("s", ""))) {
                            isGood = true;
                        }
                    }
                    if(isGood) {
                        site.openURL(site.getURL(null));
                        try {

                            switch(weathertype.replace("s", "")) {
                                case "alert":
                                    /**
                                     * //TODO: maybe have this instead tweet hourly, as no one is going to tweet to during
                                     * a storm to see what the weather alerts are.
                                     */
                                    statusUpdate = new StatusUpdate("WEATHER ALERT\n" +
                                            "Description: " + getDescription() + "\n" +
                                            "Began At: " + dateSet() +  "\n" +
                                            "Expires At: " + dateExpires());
                                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                    break;
                                case "condition":
                                    statusUpdate = new StatusUpdate(
                                            "Temp: " + getFTemp() + "F\n" +
                                                    "Feels Like: " + getFeelsLike() + "F\n" +
                                                    "Humidity: " + getHumidity() + "\n" +
                                                    "Forecast: " + getForecast() + "\n" +
                                                    "Precip: " + getPrecipitation() + " inches\n" +
                                                    "Wind: " + getWind() + "MPH\n" +
                                                    "Wind Gusts: " + getWindGusts() + "MPH");
                                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                    break;
                                case "forecast":

                                    statusUpdate = new StatusUpdate("[1/3]" +
                                            "Temp: " + getAccuHighFahrenheit() + "F/" + getAccuLowFahrenheit() + "F\n" +
                                            "Outlook: " + getAccuConditions() + "\n");
                                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                    //TODO: might need to make it sleep for 5s each tweet.
                                    statusUpdate = new StatusUpdate("[2/3] " +
                                            "Chance of Precipitation: " + getAccuPrecipPossibility() + "%\n" +
                                            "Wind: " + getMaxWind() + "MPH max / " + getAvgWind() + "MPH avg\n");
                                    //TODO: might need to make it sleep for 5s each tweet.
                                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                    statusUpdate = new StatusUpdate("[3/3] " +
                                            "Humidity: " + getAvgHumidity() + "%");
                                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                                    break;
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                    //example link: http://api.wunderground.com/api/790ff85a4325e40c/conditions/q/CA/San_Francisco.json
                }
            } catch (TwitterException ex) {
                ex.printStackTrace();
            }
        }
        return false;
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