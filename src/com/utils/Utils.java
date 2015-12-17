package com.utils;

import java.util.Random;

import com.constantes.Constantes;
import com.interfaces.IUtils;

public class Utils implements IUtils
{ 
	/**
	 * @param min int valor mínimo del rango.
	 * @param max int valor máximo del rango.
	 * @return String hex al azar. 
	 **/ 
	@Override
	public String randHex(int min, int max, int capacity) 
	{ 
	    Random rand = new Random(); 
	    //	nextInt normalmente es exclusiva del valor superior,
	    // por lo que añadir 1 para que sea incluido
	    // Por encima de fórmula voluntad genera un entero aleatorio en un rango entre el mínimo (inclusive) y máximo (inclusive).
	    int num = rand.nextInt((max - min) + 1) + min;  
	    StringBuilder hexRandom = new StringBuilder(Integer.toHexString(num));
	    
	    if (hexRandom.length() != 6)
	    	return Constantes.VACIO;	// HEXADECIMAL NO PERMITIDO 
	    
	    hexRandom.setLength(capacity);
	    return hexRandom.toString();
	}
	
	/**
	 * Simple función que parseará un flotante
	 * a decimal.
	 * @param f float 
	 * @return String parseado 
	 **/ 
	@Override
	public String floatToDecimal(float f) 
	{ 
		return String.format(Constantes.MON_FORMAT, f).replace(",", "");
	}
	
	/**
	 * Simple función que parseará un entero
	 * a Hexadecimal.
	 * @param i int 
	 * @return String Hex 
	 **/ 
	@Override
	public String decimalToHex(int i) 
	{
		return Integer.toHexString(i); 
	} 
	 
	
	
	
	
}
