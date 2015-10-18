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
        if (!keys.exists()) {
            try {
                keys.createNewFile();
                keysWriter = new BufferedWriter(new FileWriter(keys));
                keysReader = new BufferedReader(new FileReader(keys));
                KeysLayout keysLayout = new KeysLayout("", "", "", "", "", "", "", "");
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
                oAuthAccessToken, oAuthAccessTokenSecret, weatherApiKey, pasteBinKey, bitlyApiUsername, bitlyApiKey;
        public KeysLayout(String oAuthConsumerKey, String oAuthConsumerSecret, String oAuthAccessToken, String oAuthAccessTokenSecret,
                          String weatherApiKey, String pasteBinKey, String bitlyApiUsername, String bitlyApiKey) {
            this.oAuthConsumerKey = oAuthConsumerKey;
            this.oAuthConsumerSecret = oAuthConsumerSecret;
            this.oAuthAccessToken = oAuthAccessToken;
            this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
            this.weatherApiKey = weatherApiKey;
            this.pasteBinKey = pasteBinKey;
            this.bitlyApiUsername = bitlyApiUsername;
            this.bitlyApiKey = bitlyApiKey;
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

        public String getBitlyApiUsername() {
            return bitlyApiUsername;
        }

        public String getBitlyApiKey() {
            return bitlyApiKey;
        }
    }


}
