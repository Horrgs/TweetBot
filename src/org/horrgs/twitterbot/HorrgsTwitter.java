/*
The MIT License (MIT)

Copyright (c) 2015 Matt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package org.horrgs.twitterbot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.horrgs.twitterbot.api.FileManager;
import org.horrgs.twitterbot.commands.CommandHandler;
import org.horrgs.twitterbot.util.TweetListener;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by horrgs on 9/17/15.
 */
public class HorrgsTwitter implements Runnable {
    public static Twitter twitter;
    static FileManager fileManager = FileManager.getInstance();
    public static void main(String[] args) {
        fileManager.createFiles();
        new CommandHandler();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new TweetListener(), 5, 15, TimeUnit.SECONDS);
        Thread thread = new Thread(new HorrgsTwitter());
        thread.start();
    }

    @Override
    public void run() {
        JsonObject jsonObject = null;
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader("keys.json"));
            jsonObject = (JsonObject) obj;
            System.out.println("Parsing secrets.json ....");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        Gson gson = new Gson();
        FileManager.KeysLayout keysLayout = gson.fromJson(jsonObject, FileManager.KeysLayout.class);

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
                    .setOAuthConsumerKey(keysLayout.getoAuthConsumeyKey())
                    .setOAuthConsumerSecret(keysLayout.getoAuthConsumerSecret())
                    .setOAuthAccessToken(keysLayout.getoAuthAccessToken())
                    .setOAuthAccessTokenSecret(keysLayout.getoAuthAccessTokenSecret());
        TwitterFactory tf = new TwitterFactory(configurationBuilder.build());
        twitter = tf.getInstance();
    }
}
