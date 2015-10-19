package org.horrgs.twitterbot.io;

import org.json.JSONException;

public interface WeatherJSON {

    /**
     * Created by horrg on 9/20/2015.
     */
    public interface Alerts {
        public String getDescription() throws JSONException;
        public String dateSet()throws JSONException;
        public String dateExpires()throws JSONException;
    }

    public interface Conditions {
        public double getFTemp()throws JSONException;
        public double getWind()throws JSONException;
        public String getHumidity()throws JSONException;
        public String getFeelsLike()throws JSONException;
        public String getForecast()throws JSONException;
        public int getWindGusts()throws JSONException;
        public String getPrecipitation()throws JSONException;
    }

    public interface Forecast {
        public String getAccuDate()throws JSONException;
        public String getAccuHighFahrenheit()throws JSONException;
        public String getAccuLowFahrenheit()throws JSONException;
        public String getAccuConditions()throws JSONException;
        public int getAccuPrecipPossibility()throws JSONException;
        public int getSnowAllDay()throws JSONException;
        public int getSnowNight()throws JSONException;
        public int getMaxWind()throws JSONException;
        public int getAvgWind()throws JSONException;
        public int getAvgHumidity()throws JSONException;
    }
}
