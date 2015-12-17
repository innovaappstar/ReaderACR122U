package com.utils;

import com.interfaces.IUtils;

public class ManagerUtils implements IUtils
{
	IUtils iUtils;
	public ManagerUtils()
	{
		iUtils = new Utils();
	}
	
	@Override
	public String randHex(int min, int max, int capacity) 
	{
		return iUtils.randHex(min, max, capacity);
	}

	@Override
	public String floatToDecimal(float f) 
	{
		return iUtils.floatToDecimal(f);
	}

	@Override
	public String decimalToHex(int i) 
	{
		return iUtils.decimalToHex(i);
	}

}
