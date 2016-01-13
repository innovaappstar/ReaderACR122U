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
	public byte[] encrypt(byte[] data) throws Exception {
		return iAES.encrypt(data);
	}

	@Override
	public byte[] decrypt(byte[] encrypted) throws Exception {
		return iAES.decrypt(encrypted);
	}
}
