package hbs.com.snackb.utils;

import android.os.Build;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.google.android.gms.common.util.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecretUtils {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getHsKey(String text, String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        final String hmacSHA256 = "HmacSHA256";
        final Mac hasher = Mac.getInstance(hmacSHA256);
        hasher.init(new SecretKeySpec(text.getBytes(), hmacSHA256));
        final byte[] hash = hasher.doFinal(key.getBytes());

//            String resultBase = DatatypeConverter.printBase64Binary(hash); // to base64
        return Base64.encodeToString(hash, Base64.NO_WRAP); // to hex
    }

    public static byte[] sha256(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());

        return md.digest();
    }
}