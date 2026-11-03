package com.mes.util;// src/main/java/com/pangu/manufacturing/util/KeyGenerator.java


import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class KeyGenerator {

    public static String generateSecureKey() {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static void main(String[] args) {
        String secureKey = generateSecureKey();
        System.out.println("Secure JWT Key: " + secureKey);
        System.out.println("Key length: " + secureKey.length() + " characters");
    }
}