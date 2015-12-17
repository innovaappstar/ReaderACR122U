package com.interfaces;

/**
 * Created by innovaapps on 16/12/2015.
 */
public interface IAES
{
    public byte[] encrypt(String plainText) throws Exception;
    public String decrypt(byte[] cipherText) throws Exception;
}
