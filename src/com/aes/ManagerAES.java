package com.aes;

import com.interfaces.IAES;
 

/**
 * Created by innovaapps on 16/12/2015.
 */
public class ManagerAES implements IAES
{
    IAES iAES;

    public ManagerAES()
    {
        iAES    = new AES();
    }

    @Override
    public byte[] encrypt(String plainText) throws Exception {
        return iAES.encrypt(plainText);
    }

    @Override
    public String decrypt(byte[] cipherText) throws Exception {
        return iAES.decrypt(cipherText);
    }
}
