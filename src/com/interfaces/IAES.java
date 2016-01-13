package com.interfaces;

/**
 * Created by innovaapps on 16/12/2015.
 */
public interface IAES
{
    public byte[] encrypt(byte[] data) throws Exception;
    public byte[] decrypt(byte[] encrypted) throws Exception;

}
