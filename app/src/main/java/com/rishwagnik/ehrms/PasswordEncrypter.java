package com.rishwagnik.ehrms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypter {

    public String encryptPassword(String pass, String pKey) throws NoSuchAlgorithmException {

        String hash = generateSHA(pass);
        int id = pKey.length()/2;
        Character mid = pKey.charAt(id);
        String intermediatePass = hash + "" + mid + "" + pKey;
        String encryptedPass = generateMD5(intermediatePass);
        return encryptedPass;

    }

    public String generateSHA(String password) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes());
        String hash = bytesToHexString(messageDigest.digest());
        return hash;

    }

    public static String bytesToHexString(byte[] bytes){

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();

    }

    public static String generateMD5(String pass) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(pass.getBytes());
        byte byteData[] = messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer
                    .toString((byteData[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        pass = sb.toString();
        return pass;

    }

}
