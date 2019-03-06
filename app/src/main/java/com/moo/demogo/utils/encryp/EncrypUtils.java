package com.moo.demogo.utils.encryp;

import java.security.PrivateKey;

public class EncrypUtils {
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN1mPGO8NWAt08eZEP+c6fENX/0bHxsezA3xm4S6ujyB1VcSGbllwOfzgMR468PkRs7NcPao1iGU8fWjYmELLyX6W6Zs3JAVjSn/021xHhoim8iNvSKDS0YUeOt5IXS/SSQYAFDaEB/pXjUHBGBBqTTqZGzJS0igkLiqWxNlWl/pAgMBAAECgYBBbtrAOzqnL0QdBH8CnLSDWomY+3AJCuQR5usooisygMi1kBLsOUd3Bf9zka1UWt4Wdp9BNwr6J9QO3ffQaIbJLOfnHNkctm/4BnqcmGD0Jx8XNR7BeEsW8CSKeOHc75m1o63IeW2VLZIw6yuSKuRoaZGDNmqoRigAotT0EmJA5QJBAO/KRRebb+KXRfmvp/ibnrnGXYxtGSM+ZZ7gAUczs3UMReenzGqWzKUXrHkuQy8WWrZjid8AckQedWU5J+36ZRMCQQDsXbOV73a5NbI+K99WJsaIrvPCM7RtzUKlNjqFuFWXfuxdlPupxfnP4bvv3EFH7688KMqYzE1KRPHGt6SojRKTAkBsyAMuIXrRfbTl/9UgyGBqgN4BjaDAx1bMi2ypA4BfEjFIVWw70quGHcZFt0INQ4PliCSMwSQVWQgC4roFkPl3AkEAmMaX5Tm6i0vhwb2pxY2alY4/kC7LWI1rnk3G1denuxwWuwYpBDsqlOtbBlZe+5g4s7HKAalL681UWRUqIKfOcQJBALJ7HnqEMP1WwdNiU7eLXMv/NCdK12NHIYcSu/L2G4Dt6rvy3vpBq+ctA3f4xvkSmfjVWVxVqgTpF0AIKpf/5lI=";

    private String encodeRSA(String str) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeRSA(String str) {
        String decodeString = null;
        try {
            decodeString = RsaEncryptUtils.RsaDecryptByPrivateKey(str, PRIVATE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodeString;
    }

}
