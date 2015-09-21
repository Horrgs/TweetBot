package org.horrgs.twitterbot.io;

import java.net.URL;

/**
 * Created by horrg on 9/19/2015.
 */
public interface NewsJSON {
    public String getTitle(int x);

    public String getStringURL(int x);

    public String getPublisher(int x);

    public void setTitle(String title);

    public String setURL(String url);

    public String setURL(URL url);
}
