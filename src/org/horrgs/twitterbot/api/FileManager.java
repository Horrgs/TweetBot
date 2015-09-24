package org.horrgs.twitterbot.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.*;

/**
 * Created by horrgs on 9/17/15.
 */
public class FileManager {
    private static FileManager instance = new FileManager();
    public static FileManager getInstance() { return instance; }
    String username1, password1, oAuthConsumerKey1, oAuthConsumerSecret1,
            oAuthAccessToken1, oAuthAccessTokenSecret1, weatherApiKey1, pasteBinKey1;
    private File keys;
    private BufferedWriter keysWriter;

    private BufferedReader keysReader;

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

    public void createFiles() {
        keys = new File("keys.json");
        if(!keys.exists()) {
            try {
                keys.createNewFile();
                keysWriter = new BufferedWriter(new FileWriter(keys));
                keysReader = new BufferedReader(new FileReader(keys));

                FileLayout fileLayout = new FileLayout("", "", "", "", "", "", "", "");
                Gson gson = new Gson();
                String json = gson.toJson(fileLayout);
                keysWriter.write(json);
                keysWriter.flush();
                keysWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public FileManager() {
        keys = new File("keys.json");
        if(keys.exists()) {
            try {
                keysWriter = new BufferedWriter(new FileWriter(keys));
                keysReader = new BufferedReader(new FileReader(keys));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            createFiles();
        }
    }

    public String getWeatherApiKey() {
        return weatherApiKey1;
    }

    public String getUsername() {
        return username1;
    }

    public String getPassword() {
        return password1;
    }

    public String getoAuthConsumeyKey() {
        return oAuthConsumerKey1;
    }

    public String getoAuthConsumerSecret() {
        return oAuthConsumerSecret1;
    }

    public String getoAuthAccessToken() {
        return oAuthAccessToken1;
    }

    public String getoAuthAccessTokenSecret() {
        return oAuthAccessTokenSecret1;
    }

    public String getPasteBinKey() {
        return pasteBinKey1;
    }

    private class FileLayout {

        public FileLayout(String username, String password, String oAuthConsumerKey,
                          String oAuthConsumerSecret, String oAuthAccessToken, String oAuthAccessTokenSecret, String weatherApiKey, String pasteBinKey) {
            username1 = username;
            password1 = password;
            oAuthConsumerKey1 = oAuthConsumerKey;
            oAuthConsumerSecret1 = oAuthConsumerSecret;
            oAuthAccessToken1 = oAuthAccessToken;
            oAuthAccessTokenSecret1 = oAuthAccessTokenSecret;
            weatherApiKey1 = weatherApiKey;
            pasteBinKey1 = pasteBinKey;
        }

        public void setUsername(String username) {
            username1 = username;
        }

        public void setPassword(String password) {
            password1 = password;
        }

        public void setoAuthConsumeyKey(String oAuthConsumeyKey) {
            oAuthConsumerKey1 = oAuthConsumeyKey;
        }

        public void setoAuthConsumerSecret(String oAuthConsumerSecret) {
            oAuthConsumerSecret1 = oAuthConsumerSecret;
        }

        public void setoAuthAccessToken(String oAuthAccessToken) {
            oAuthAccessToken1 = oAuthAccessToken;
        }

        public void setoAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
            oAuthAccessTokenSecret1 = oAuthAccessTokenSecret;
        }

        public void setWeatherApiKey(String weatherApiKey) {
            weatherApiKey1 = weatherApiKey;
        }
    }


}
