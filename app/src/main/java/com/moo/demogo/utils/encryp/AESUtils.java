package com.moo.demogo.utils.encryp;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String ALGORITHM = "AES";
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    public static String decode(String decryptKey, String encryptStr) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), ALGORITHM));
        byte[] decryptBytes = cipher.doFinal(Base64.decode(encryptStr, Base64.DEFAULT));
        return new String(decryptBytes);
    }
}
