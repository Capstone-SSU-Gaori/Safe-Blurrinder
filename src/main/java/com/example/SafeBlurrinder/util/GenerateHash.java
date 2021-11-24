package com.example.SafeBlurrinder.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class GenerateHash {
    private String hash=null;
    public GenerateHash(String input){


        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes("utf8"));
            byte[] hashByte=digest.digest();
            StringBuilder builder = new StringBuilder();
            for(byte b : hashByte) {
                String hexString = String.format("%02x", b);
                builder.append(hexString);
            }
            hash = builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("hash: "+hash);
    }

    public String out(){
        return hash;
    }
}
