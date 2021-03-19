package com.seminar.easyCookWeb.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

@Component
public class AESEncoder {
    @Value("${aes.key}")
    private String key;
    @Value("${aes.size}")
    private int ivSize;
    @Value("${aes.size}")
    private int keySize;

    private static final String charset = "UTF-8";

    //產生長度為16的IV值
    public byte[] IVGenerator(){
        // Generating IV.
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv); //直接改變iv值
        return iv;
    }

    @SneakyThrows
    public byte[] hashKey(String key){
        byte[] keyBytes = new byte[keySize];
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getBytes(charset));
        System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
        return keyBytes;
    }

    public String encrypt(String plainText) throws Exception {
        byte[] plainTextBytes = plainText.getBytes();
        byte[] IV_VALUE = IVGenerator();
        // Generating IV. which was random
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV_VALUE);

        // Hashing key.
        SecretKeySpec secretKeySpec = new SecretKeySpec(hashKey(key), "AES");


        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(plainTextBytes);

        // Combine IV and encrypted part. 16+encrypted.length的長度(假設:20) = 36
        //產生出36個空間的bytes
        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        //把byte[] iv_value從第0個開始複製 => 複製到encryptedIVAndText byte[36] 的位置 0 to 16這個位置
        System.arraycopy(IV_VALUE, 0, encryptedIVAndText, 0, ivSize);
        //然後encrypted這個byte[]的第0個位置到最後 => 複製到encryptedIVAndText byte[36] 的位置 17 to 36這個位置
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

        return Base64.getUrlEncoder().encodeToString(encryptedIVAndText);
    }

    public String decrypt(String encryptedIvTextString) throws Exception {
        byte[] encryptedIvTextBytes = Base64.getUrlDecoder().decode(encryptedIvTextString);

        // Extract IV.
        byte[] iv = new byte[ivSize];
        //抽取IV的部分出來 => encryptedIvTextBytes整個部分複製到 iv bytes[16]裡面
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // 抽取密文的部分出來 => encryptedIvTextBytes 17之後的部分 抽取至 encryptedBytes bytes[36-17]裡面
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);

        // Hash key.
        SecretKeySpec secretKeySpec = new SecretKeySpec(hashKey(key), "AES");

        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

        return new String(decrypted);
    }

    public String encode(Long id) throws Exception {
        return encrypt(id.toString());
    }
}
