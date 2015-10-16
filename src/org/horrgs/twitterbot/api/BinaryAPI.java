package org.horrgs.twitterbot.api;

/**
 * Created by horrg on 9/23/2015.
 */
public class BinaryAPI {

    public String toText(String binary) {
        String text = "";
        for(int i = 0; i <= binary.length() - 8; i+=8)
        {
            int k = Integer.parseInt(binary.substring(i, i+8), 2);
            text += (char) k;
        }
        return text;
    }

    public String toBinary(String text) {
        byte[] bytes = text.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }
}