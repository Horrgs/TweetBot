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

import com.google.gson.JsonParser;
import org.horrgs.twitterbot.api.FileManager;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileReader;

/**
 * Created by horrgs on 9/17/15.
 */
public class HorrgsTwitter implements Runnable {
    static FileManager fileManager;
    public static Twitter twitter;

    public static void main(String[] args) {
        fileManager = new FileManager();
    }

    @Override
    public void run() {
        JSONObject jsonObject = null;
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader("secrets.json"));
            jsonObject = (JSONObject) obj;
            System.out.println("Parsing secrets.json ....");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        try {
            configurationBuilder.setDebugEnabled(true)
                    .setOAuthConsumerKey(jsonObject.get("OAuthConsumerKey").toString())
                    .setOAuthConsumerSecret(jsonObject.get("OAuthConsumerSecret").toString())
                    .setOAuthAccessToken(jsonObject.get("OAuthAccessToken").toString())
                    .setOAuthAccessTokenSecret(jsonObject.get("OAuthAccessTokenSecret").toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        TwitterFactory tf = new TwitterFactory(configurationBuilder.build());
        twitter = tf.getInstance();


    }
}
