package org.horrgs.twitterbot.util;

import org.horrgs.twitterbot.api.Site;
import org.horrgs.twitterbot.io.Word;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by horrg on 9/23/2015.
 */
public class Words implements Word, Word.WordOfTheDay {
    private String wordOfTheDay;

    public String[] getRandomWords(int wordsWanted) {
        //TODO: switch to /words.json/randomWords
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


    Site site = new Site("");
    public Word getWordInfo(String word, int limit, boolean includeRelated, String[] sourceDictionaries, boolean useCanonical) {
        //TODO: sourceDictionaries.
        site = new Site("http://api.wordnik.com:80/v4/word.json/" + word
                + "/definitions?limit=" + limit + "&includeRelated=" + includeRelated + "&sourceDictionaries=all&useCanonical=" +
                useCanonical + "&includeTags=false&api_key=" + "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
        site.startJson(true, null, null);
        //WIP. This is wrong.
        Word word1 = new Words();
        site.startJson(true, null, null);
        return word1;
    }

    /**
     *
     * @param date Date format must be as in YYYY-MM-DD. If it were October 2nd of 2015, it'd be
     *             2015-10-02.
     * @return WordOfTheDay class data.
     */
    public WordOfTheDay getWordOfTheDay(String date) {
        Site site = new Site("http://api.wordnik.com:80/v4/words.json/wordOfTheDay?" +
                "date=2015-10-02&api_key=" +
                "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
        site.startJson(false, null, null);
        WordOfTheDay wordOfTheDay = new Words();
        return wordOfTheDay;
    }


    @Override
    public String getSourceDictionary() throws JSONException {
        return site.get(site.getJsonArray().getJSONObject(0), "sourceDictionary");
    }

    @Override
    public String[] getExampleUses() throws JSONException {
        return site.get(site.getJsonArray().getJSONObject(0), "exapleUses");
    }

    @Override
    public String[] getRelatedWords() throws JSONException {
        return site.get(site.getJsonArray().getJSONObject(0), "relatedWords");
    }

    @Override
    public String getWord() throws JSONException {
        return site.get(site.getJsonArray().getJSONObject(0), "word");
    }

    @Override
    public String getPartOfSpeech() throws JSONException {
        return site.get(site.getJsonArray().getJSONObject(0), "partOfSpeech");
    }

    @Override
    public String getDefinition() throws JSONException {
        return site.get(site.getJsonArray().getJSONObject(0), "text");
    }

    @Override
    public String getWDefinition() throws JSONException {
        return site.get(site.getJsonObject().getJSONArray("definitions").getJSONObject(0), "text");
    }

    @Override
    public String getWSource() throws JSONException {
        return site.get(site.getJsonObject().getJSONArray("definitions").getJSONObject(0), "source");
    }

    @Override
    public String getWPartOfSpeech() throws JSONException {
        return site.get(site.getJsonObject().getJSONArray("definitions").getJSONObject(0), "partOfSpeech");
    }


}
