package org.horrgs.twitterbot.util;

import org.horrgs.twitterbot.api.Site;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by horrg on 9/23/2015.
 */
public class Words {
    private String wordOfTheDay;

    public String[] getRandomWords(int wordsWanted) {
        String[] string = new String[wordsWanted + 3];
        Site site = new Site("http://api.wordnik.com:80/v4/words.json/" +
                "randomWords?hasDictionaryDef=true&includePartOfSpeech=noun&minCorpusCount=0&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=5&maxLength=-" +
                "1&limit=10&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
        site.startJson(true, null, null);

        for(int x = 0; x < wordsWanted; x++) {
            try {
                string[x] = site.get(site.getJsonArray().getJSONObject(x), "word");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return string;
    }

    public String getWordOfTheDay() {
        return wordOfTheDay;
    }

    public void setWordOfTheDay(String wordOfTheDay) {
        this.wordOfTheDay = wordOfTheDay;
    }

    public String defineWord(String word) {
        String[] k = {"all", ""};
        try {
            Site site = new Site("http://api.wordnik.com:80/v4/word.json/" + word
                    + "/definitions?limit=1&includeRelated=true&sourceDictionaries=all&useCanonical=false" +
                    "&includeTags=false&api_key=" + "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
            site.startJson(true, null, null);
            return site.get(site.getJsonArray().getJSONObject(0), "text");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String[] getWordInfo(String word, int limit, boolean includeRelated, String[] sourceDictionaries, boolean useCanonical) {
        try {
            //TODO: sourceDictionaries.
            Site site = new Site("http://api.wordnik.com:80/v4/word.json/" + word
                    + "/definitions?limit=" + limit + "&includeRelated=" + includeRelated + "&sourceDictionaries=all&useCanonical=" +
                    useCanonical + "&includeTags=false&api_key=" + "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
            site.startJson(true, null, null);
            //WIP. This is wrong.
            return site.get(site.getJsonArray().getJSONObject(0), "text");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
