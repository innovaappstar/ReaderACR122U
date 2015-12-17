package com.constantes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constantes 
{  
	// VACIO..
	public static String VACIO = ""; 
	//public static String STR_SALDO_FORMAT 		= "000000#000000#00"; //"0000000000000000";
	public static String MON_FORMAT = "%.2f"; 
	
	public static String STR_TRANSACCION_FORMAT	= "00000#00000#000"; //"0000000000000000";
	//FORMATO PARA DETALLE DE TARJETA (COD#DNI  || TIPO#FECHA)
	public static String STR_DETALLE_TARJETA_FORMAT	= "0000000#0000000"; //"0000000000000000";
	public static String STR_DETALLE_TARJETA_FO	= ""; //"0000000000000000";
	
	
	private static final String REGEX = "^([0-9A-Fa-f]{2})+$"; 
    private static Pattern pattern;
    private static Matcher matcher;
}
