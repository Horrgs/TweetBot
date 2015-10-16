package org.horrgs.twitterbot.api;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Date;

/**
 * Created by horrgs on 9/17/15.
 */
public class FileManager {
    private static FileManager instance = new FileManager();
    public static FileManager getInstance() { return instance; }
    private File keys;
    private BufferedReader keysReader;
    private BufferedWriter keysWriter;

    public BufferedWriter getKeysWriter() {
        return keysWriter;
    }

    public void setKeysWriter(BufferedWriter keysWriter) {
        this.keysWriter = keysWriter;
    }

    public BufferedReader getKeysReader() {
        return keysReader;
    }

    public void setKeysReader(BufferedReader keysReader) {
        this.keysReader = keysReader;
    }

    private JsonArray miscArray;

    public void createFiles() {
        keys = new File("keys.json");
        System.out.println("#1");
        if (!keys.exists()) {
            System.out.println("#2");
            try {
                System.out.println("#3");
                keys.createNewFile();
                keysWriter = new BufferedWriter(new FileWriter(keys));
                keysReader = new BufferedReader(new FileReader(keys));
                KeysLayout keysLayout = new KeysLayout("", "", "", "", "", "");
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(gson.toJson(keysLayout));
                keysWriter.write(jsonObject.toString());
                keysWriter.flush();
                keysWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("#3000");
            Gson gson = new Gson();
            JsonObject jsonObject = null;
            JsonParser jsonParser = new JsonParser();
            try {
                Object obj = jsonParser.parse(new FileReader("keys.json"));
                jsonObject = (JsonObject) obj;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            KeysLayout keysLayout = gson.fromJson(jsonObject, KeysLayout.class);
            System.out.println(keysLayout.getoAuthAccessToken());
        }
    }

        public String getKey(String key) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader("keys.json"));
            jsonObject = (JsonObject) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject.get(key).getAsString();
    }

    public class KeysLayout {


        String oAuthConsumerKey, oAuthConsumerSecret,
                oAuthAccessToken, oAuthAccessTokenSecret, weatherApiKey, pasteBinKey;
        public KeysLayout(String oAuthConsumerKey,
                          String oAuthConsumerSecret, String oAuthAccessToken, String oAuthAccessTokenSecret, String weatherApiKey, String pasteBinKey) {
            oAuthConsumerKey = oAuthConsumerKey;
            oAuthConsumerSecret = oAuthConsumerSecret;
            oAuthAccessToken = oAuthAccessToken;
            oAuthAccessTokenSecret = oAuthAccessTokenSecret;
            weatherApiKey = weatherApiKey;
            pasteBinKey = pasteBinKey;
        }

        public String getWeatherApiKey() {
            return weatherApiKey;
        }

        public String getoAuthConsumeyKey() {
            return oAuthConsumerKey;
        }

        public String getoAuthConsumerSecret() {
            return oAuthConsumerSecret;
        }

        public String getoAuthAccessToken() {
            return oAuthAccessToken;
        }

        public String getoAuthAccessTokenSecret() {
            return oAuthAccessTokenSecret;
        }

        public String getPasteBinKey() {
            return pasteBinKey;
        }

        public void setoAuthConsumeyKey(String oAuthConsumeyKey) {
            oAuthConsumerKey = oAuthConsumeyKey;
        }

        public void setoAuthConsumerSecret(String oAuthConsumerSecret) {
            oAuthConsumerSecret = oAuthConsumerSecret;
        }

        public void setoAuthAccessToken(String oAuthAccessToken) {
            oAuthAccessToken = oAuthAccessToken;
        }

        public void setoAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
            oAuthAccessTokenSecret = oAuthAccessTokenSecret;
        }

        public void setWeatherApiKey(String weatherApiKey) {
            weatherApiKey = weatherApiKey;
        }
    }


}
