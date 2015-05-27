/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

import junit.framework.Assert;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import com.pr7.constant.Constants;

/**
 * 
 * @author Admin
 */
public class EncryptUtilTest {
	
	@Test
	public void testHashSHA1() throws Exception {
		String str = "password123";
		String encryptStr = EncryptUtil.getInstance().hash(str, Constants.CRYPTO_SHA1);

		Assert.assertEquals("cbfdac6008f9cab4083784cbd1874f76618d2a97", encryptStr);
	}

	@Test
	public void testEncryptDecrypt() throws Exception {
		String str = "password123";
		String encryptStr = EncryptUtil.getInstance().encrypt(str, "mykey");
		System.out.println("EncryptUtil.getInstance:: sh1 = " + EncryptUtil.getInstance().hash(str, Constants.CRYPTO_SHA1));
		System.out.println("EncryptUtil.getInstance:: md5 = " + EncryptUtil.getInstance().hash(str, Constants.CRYPTO_MD5));		
		System.out.println("RandomStringUtils.random(10, true, true) = " + RandomStringUtils.random(10, true, true));
		
		Assert.assertEquals("password123", EncryptUtil.getInstance().decrypt(encryptStr, "mykey"));
	}
	
	@Test
	public void testDecrypt() throws Exception {
		String key = EncryptUtil.getInstance().hash("pwd219131", Constants.CRYPTO_MD5);
		key = "e6a52c828d56b46129fbf85c4cd164b3";
		String decryptedTmpPwd = EncryptUtil.getInstance().decrypt("2tkALs5mtgy6iIlGWcyyMNN6ZC3xtq2O", key);
		System.out.println("decryptedTmpPwd = " + decryptedTmpPwd);
		Assert.assertEquals("mlklx21292", decryptedTmpPwd);
	}
        
        @Test
	public void testEncrypt() throws Exception {
        String shapwd = EncryptUtil.getInstance().hash("password123", Constants.CRYPTO_SHA1);
		String key = EncryptUtil.getInstance().hash("password123", Constants.CRYPTO_MD5);
		String corePwd = "123qwe";
		String encryptedTmpPwd = EncryptUtil.getInstance().encrypt(corePwd, key);
		System.out.println("encryptedTmpPwd = " + encryptedTmpPwd);
	}
        
    @Test
    public void testBase64() throws Exception {
//    	 customer-web.log.9-1575932-[2013-01-13 04:32:28,057] DEBUG [http-thread-pool-20081(15)] - http.DefaultHttpClientHelper - params:data=%3C%3Fxml+version%3D%221.0%22+encoding%3D%22UTF-8%22%3F%3E%3Cverifymember%3E%3Ccredential%3EMTk5NzA1Mjhucm5y%3C%2Fcredential%3E%3C%2Fverifymember%3E
    	

    	String password64 = "MTk5NzA1Mjhucm5y";
    	String actual = new String(Base64.decodeBase64(password64.getBytes()));
    	System.out.println(actual);
    	System.out.println(EncryptUtil.getInstance().hash(actual, Constants.CRYPTO_SHA1));
    }
}
