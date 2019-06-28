package hbs.com.snackb.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Base64Utils{
    /**
     * Encode txt
     *
     * @param txt
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String txt) throws UnsupportedEncodingException {
        byte[] data = txt.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * Decode txt
     *
     * @param txt
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String txt) throws UnsupportedEncodingException {
        return new String(Base64.decode(txt, Base64.DEFAULT), StandardCharsets.UTF_8);
    }
}