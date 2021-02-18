package com.shemyakin.spring.dbrunner2_0.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;

@Service
@Scope("singleton")
public class Crypta {
    private static final Logger logger = LoggerFactory.getLogger(Crypta.class);
    private static SecretKey secretKey;
    private static byte[] key;

    public Crypta() {
    }

    @PostConstruct
    private static void setKey() {
        try {
            String myKey = "Ti_4e_ne_smotri2312";
            key = myKey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = Arrays.copyOf(sha.digest(key), 16);
            secretKey = new SecretKeySpec(key, "AES");
            logger.info("Успешно сделали ключик");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Ошибка создания ключа шифрования " + ex.getMessage());
        }

    }

    public static String encrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        } catch (Exception e) {
            logger.error("Ошибка кодирования " + e.getMessage());
            return null;
        }
    }

    public static String decrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(str)));
        } catch (Exception e) {
            logger.error("Ошибка декодирования " + e.getMessage());
            return null;
        }
    }
}
