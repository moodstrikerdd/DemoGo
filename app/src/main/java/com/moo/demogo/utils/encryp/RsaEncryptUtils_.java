package com.moo.demogo.utils.encryp;


import android.util.Base64;
import android.util.Log;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * 加解密自动配置
 *
 * @author mah
 */
public class RsaEncryptUtils_ {
    public static final String PUBLIC_KEY = "PUBLIC_KEY";
    public static final String PRIVATE_KEY = "PRIVATE_KEY";
    private static KeyFactory keyFactory;
    private static final String RSA = "RSA";
    private static final String DEFAULT_CHARSET = "utf-8";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    static {
        //实例化密钥工厂
        try {
            keyFactory = KeyFactory.getInstance(RSA);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String base64Encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static byte[] base64Decode(String base64Code) throws Exception {
        return Base64.decode(base64Code, Base64.DEFAULT);
    }

    /**
     * 公钥加密
     *
     * @param content   待加密数据
     * @param publicKey 密钥
     * @return string 加密后数据
     */
    public static String RsaEncryptByPublicKey(String content, String publicKey) throws Exception {
        //初始化公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return base64Encode(cipher.doFinal(content.trim().getBytes(DEFAULT_CHARSET)));

    }

    /**
     * 私钥解密
     *
     * @param encryptStr 待解密前数据
     * @param privateKey 私钥
     * @return string 解密后数据
     */
    public static String RsaDecryptByPrivateKey(String encryptStr, String privateKey) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKey));
        //生成私钥
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(base64Decode(encryptStr)),"utf-8");
    }


    /**
     * 初始化密钥对，获得一对公钥和私钥
     *
     * @return Map
     */
    public static Map<String, String> initKey() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        //初始化密钥生成器
        keyPairGenerator.initialize(1024);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, String> keyMap = new HashMap<>();

        keyMap.put(PUBLIC_KEY, Base64.encodeToString(publicKey.getEncoded(),Base64.DEFAULT));
        keyMap.put(PRIVATE_KEY, Base64.encodeToString(privateKey.getEncoded(),Base64.DEFAULT));
        return keyMap;

    }


    public static void test() throws Exception {

//        Map<String, String> map = RsaEncryptUtils.initKey();
//        String publickKey = map.get(PUBLIC_KEY);
//        String privateKey = map.get(PRIVATE_KEY);
        String publickKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALxSdetjkbVgtTGMUlbNgTzQWUjsnTqjRUAZDAPuHI6zqc029riVdeZ33uYe+Vr2UpS16b1ut8Z3Wh13NBuzg/ECAwEAAQ==";
        String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAvFJ162ORtWC1MYxSVs2BPNBZSOydOqNFQBkMA+4cjrOpzTb2uJV15nfe5h75WvZSlLXpvW63xndaHXc0G7OD8QIDAQABAkAXkk62LtlPFZK4Z/LqcICLpxx11z/61fLWCgyJ9WTXq4faGBRp411+0pzkFd9skBK/P2s2SyQIGY3TcApl/NINAiEA+cZgIbQw+b3eEszC9Y3gGM/rBZQV+ADF7rG3ymNfUWcCIQDBBABarrO120OEDIpPz3ltMIyF7Ar/5bm5KRdkycxw5wIhAOyJ4SAJ9ReNp3FGWJsS8NaOkJOH2gYzNT8HLnaMBQihAiA8o/Mwil6vrfmiTQZPWNG/eldL5AL4rsYQ5FYTNkti1QIhAI3tGh7YhSOGQJvxDCugkdqatYJfIMDYEQUhZL7AnxGs";
        Log.e("rsa",PUBLIC_KEY + "=>" + publickKey);
        Log.e("rsa",PRIVATE_KEY + "=>" + privateKey);

        String content = "abcdefghijklmnop";
        Log.e("rsa","加密前:" + content);
        String enStr = RsaEncryptUtils_.RsaEncryptByPublicKey(content, publickKey);
        Log.e("rsa","加密后:" + enStr);
        String deStr = RsaEncryptUtils_.RsaDecryptByPrivateKey(enStr, privateKey);
        Log.e("rsa","解密后:" + deStr);

    }

}