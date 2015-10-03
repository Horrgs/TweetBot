package org.horrgs.twitterbot.io;

import org.json.JSONException;

/**
 * Created by Horrgs on 10/2/2015.
 */
public interface Word {

    public String getSourceDictionary() throws JSONException;

    public String[] getExampleUses() throws JSONException;

    public String[] getRelatedWords() throws JSONException;

    public String getWord() throws JSONException;

    public String getPartOfSpeech() throws JSONException;

    public String getDefinition() throws JSONException;

    public interface WordOfTheDay {
        public String getWDefinition() throws JSONException;
        public String getWSource() throws JSONException;
        public String getWPartOfSpeech() throws JSONException;
    }
}
