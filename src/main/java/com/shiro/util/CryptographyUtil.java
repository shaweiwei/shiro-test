package com.shiro.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;

public class CryptographyUtil {  
	  
    public static String encBase64(String str) {  
        return Base64.encodeToString(str.getBytes());  
    }  
    public static String decBase64(String str){  
        return Base64.decodeToString(str);  
    }  
    public static String md5(String str,String salt){  
        return new Md5Hash(str,salt).toString();  
    }  
    public static void main(String[] args) {  
        String password="1234";  
        System.out.println(CryptographyUtil.encBase64(password));  
        System.out.println(CryptographyUtil.decBase64("MTIzNA=="));  
        System.out.println(CryptographyUtil.md5(password, "java"));  
    }  
}  
