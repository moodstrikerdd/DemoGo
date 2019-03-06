package com.moo.demogo.utils.encryp;


import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

/**
 * 加解密自动配置
 *
 * @author mah
 *
 */
public class AesEncryptUtils {
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    private static final String TAG = "AesEncryptUtils";

    public static String base64Encode(byte[] bytes) {
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    public static byte[] base64Decode(String base64Code) throws Exception {
        return Base64.decode(base64Code,Base64.DEFAULT);
    }

    /**
     * 加密
     *
     * @param content    待加密内容
     * @param encryptKey 密钥
     * @return string
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), ALGORITHM));
        return base64Encode(cipher.doFinal(content.getBytes("utf-8")));
    }

    /**
     * 解密
     *
     * @param encryptStr 待解密内容
     * @param decryptKey 密钥
     * @return 解密后内容
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), ALGORITHM));
        byte[] decryptBytes = cipher.doFinal(base64Decode(encryptStr));
        return new String(decryptBytes);
    }
    
    /**
     * @Author Han.ma
     * @Description //TODO 获取16位的aeskey
     * @Date 16:26 2018/7/16
     * @Param []
     * @return java.lang.String
     **/
    public static String generateAESKEY() {
        String pass = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        return pass;
    }


    public static void test() throws Exception{
        //key要求是16位的
        String KEY = "05a526126eb32e1480e75ca029a0aebc";
        Log.e(TAG, KEY);
        Log.e(TAG, KEY.length() +"");

        String content ="{\"socialCreditCode\":\"123456\"}";
        Log.e(TAG, "加密前：" + content);

        String encrypt = aesEncrypt(content, KEY);
        Log.e(TAG, "加密后：" + encrypt);
//
        String decrypt = aesDecrypt(encrypt, KEY);
        Log.e(TAG, "解密后：" + decrypt);

        String encryptContent = "KPWKS5txIkbaBBHO1NG6Ps/Bmn2o0VId7t76kJJb3uQ=";
        String decryptContent = aesDecrypt(encryptContent, "f6626e0783044204");
        Log.e(TAG, "解密后：" + decryptContent);

    }

}