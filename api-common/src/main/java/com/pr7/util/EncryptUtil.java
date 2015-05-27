/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 *
 * @author Admin
 */
public class EncryptUtil {
    private static final Logger _logger = LogManager.getLogger(EncryptUtil.class);
    
    private static EncryptUtil instance;

    public static synchronized EncryptUtil getInstance() //step 1
    {
        if (instance == null) {
            instance = new EncryptUtil();
        }
        return instance;
    }

    public synchronized String hash(String plaintext, String algorithm) throws Exception {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm); //step 2 : We are asking Java security API to obtain an instance of a message digest object using the algorithm supplied
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e.getMessage());
        }
        try {
            md.update(plaintext.getBytes("UTF-8")); //step 3 : convert the plaintext password into a byte-representation using UTF-8 encoding format
            //         apply this array to the message digest object created earlier. This array will be used as a source for the message digest object to operate on
            //         Do the transformation: generate an array of bytes that represent the digested (encrypted) password value
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        }

        byte raw[] = md.digest(); //step 4 : Create a String representation of the byte array representing the digested password value. This is needed to be able to store the password in the database
        String hash = Hex.encodeHexString(raw); //step 5 : Return the String representation of the newly generated hash back to our servlet so that it can be stored in the database
        //         Tester can also use this website to verify the value (for SHA-1) : http://hash.online-convert.com/sha1-generator

        //String hash = Base64.encodeBase64String(raw); 
        //String hash = (new sun.misc.BASE64Encoder()).encode(raw);

        return hash; //step 6
    }
    
    public String encrypt(String raw, String key) {
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        
        return encryptor.encrypt(raw);
    }
    
    public String decrypt(String encryptTxt, String key) {
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        
        return encryptor.decrypt(encryptTxt);
    }
}
