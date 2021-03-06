package it.units.businesscardwallet.utils;


import android.annotation.SuppressLint;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import it.units.businesscardwallet.BuildConfig;

// https://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa
// https://www.apriorit.com/dev-blog/612-mobile-cybersecurity-encryption-in-android

// Encryption Library https://stackoverflow.com/questions/5220761/fast-and-simple-string-encrypt-decrypt-in-java
// Cipher https://developer.android.com/reference/javax/crypto/Cipher

@SuppressLint("GetInstance")
public abstract class AESHelper {

    private static final String ALGORITHM = "AES";
    private static final byte[] KEY_CONTENT = Base64.getDecoder().decode(BuildConfig.APP_KEY);
    private static final SecretKey SECRET_KEY = new SecretKeySpec(KEY_CONTENT, 0, KEY_CONTENT.length, "AES");

    public static String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

}

