package cn.org.rapid_framework.util;

import java.io.UnsupportedEncodingException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
/**
 * Crypto utils
 */
public class Crypto {

    static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * Sign a message with a key
     * @param message The message to sign
     * @param key The key to use
     * @return The signed message (in hexadecimal)
     * @throws java.lang.Exception
     */
    public static String sign(String message, byte[] key) {

        if (key.length == 0) {
            return message;
        }

        byte[] messageBytes;
        KeyParameter secretKey = new KeyParameter(key);
        Digest digest = new SHA1Digest();
        HMac hmac = new HMac(digest);
        try {
            messageBytes = message.getBytes("utf-8");
            hmac.init(secretKey);
            hmac.update(messageBytes, 0, messageBytes.length);
            byte[] result = new byte[1000];
            int len = hmac.doFinal(result, 0);
            char[] hexChars = new char[len * 2];


            for (int charIndex = 0, startIndex = 0; charIndex < hexChars.length;) {
                int bite = result[startIndex++] & 0xff;
                hexChars[charIndex++] = HEX_CHARS[bite >> 4];
                hexChars[charIndex++] = HEX_CHARS[bite & 0xf];
            }
            return new String(hexChars);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }

    }


}