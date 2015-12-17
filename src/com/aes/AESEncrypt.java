package com.aes;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.innova.MainPrincipal;

public class AESEncrypt 
{
	/**
	 * Define el algoritmo para la operación de la
	 * encriptación. 
	 **/
	private static String ALGORITMO_CIPHER	= "AES";
	/**
	 * Define el algoritmo de generación de números 
	 * seudoaleatorios utilizados por SHA-1.
	 **/ 
	private static String ALGORITMO_SHA1 	= "SHA1PRNG";
	/**
	 * Define tamanio de bits de cifrado. 
	 **/
	private static int TAMANIO_BITS 		= 0x80;
	/**
	 * Define password de desarrollo. 
	 **/
	private static String KEY_DESARROLLO = "abcdefghehehehehehee";
	
	
  public static byte[] encrypt(String plainText) throws Exception 
  {
	/**
	 * getInstance(NOMBRE_TRANSFORMACIÓN_SOLICITADA, PROVEEDOR)
	 * TRANSFORMACION = ES UNA CADENA QUE DESCRIBRE EL FUNCIONAMIENTO... 
	 * TRANSFORMACION : ALGORITMO/MÓDO/PADDING 
	 * 
	 * init (int OPMODE, clave  fundamental, AlgorithmParameterSpec  params)
	 * Inicializa esta cifra con una clave y un conjunto de parámetros del algoritmo o VECTOR DE INICIALIZACIÓN. 
	 **/  
	  
	Cipher cipher 		= Cipher.getInstance(ALGORITMO_CIPHER);
	SecretKeySpec skey 	= new SecretKeySpec(getKey(), ALGORITMO_CIPHER);   
	
	cipher.init(Cipher.ENCRYPT_MODE, skey); 
	return cipher.doFinal(plainText.getBytes());
  }
 
  /**
   * Simple función que desencriptará una arreglo de bytes 
   * y devolverá una cadena ...
   * @param cipherText byte que contiene la tarjeta.
   **/
  public static String decrypt(byte[] cipherText) throws Exception
  {
	Cipher cipher 		= Cipher.getInstance(ALGORITMO_CIPHER);  
	SecretKeySpec skey 	= new SecretKeySpec(getKey(), ALGORITMO_CIPHER); 
	cipher.init(Cipher.DECRYPT_MODE, skey); 
    return new String(cipher.doFinal(cipherText));  
  }

  /**
   * Simple función que generará un cifrado de keys 
   * en formato byte[]
   * @return {@link Key} que se utilizará en {@link SecretKeySpec}
   **/
  private static byte[] getKey() throws Exception
  {
		SecureRandom  secureRandom	= SecureRandom.getInstance(ALGORITMO_SHA1);
		KeyGenerator generadorKey 	= KeyGenerator.getInstance(ALGORITMO_CIPHER);
		secureRandom.setSeed(KEY_DESARROLLO.getBytes()); 
		generadorKey.init(TAMANIO_BITS, secureRandom);
		SecretKey keySecreta 		= generadorKey.generateKey();
		byte[] key 	= keySecreta.getEncoded();
		return key;
  }
  

	public void cifrado()
	{

    	//String datosCifrado = "123456789012345";
    	String datosCifrado   = "ABCDJHFBHKDBNFK";
        SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("abcdefghehehehehehee".getBytes());
            //sr.setSeed("12345678901234561234".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
        }
        // Encode the original data with AES
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(datosCifrado.getBytes());
        } catch (Exception e) { 
        }
        MainPrincipal.printLog( "DatosCifrados Size\n" + datosCifrado.length() + "\n[ENCODED]:\n" + encodedBytes.length);

        // Decode the encoded data with AES
        byte[] decodedBytes = null;
        try {
            SecretKeySpec sks2 = null;
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("abcdefghehehehehehee".getBytes());
            //sr.setSeed("12345678901234561234".getBytes());
            KeyGenerator kgs = KeyGenerator.getInstance("AES");
            kgs.init(128, sr);
            sks2 = new SecretKeySpec((kgs.generateKey()).getEncoded(), "AES");
        	
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks2);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) { 
        }
        if (decodedBytes != null)
        {
        	MainPrincipal.printLog("[DECODED]:\n" + new String(decodedBytes) + "\n");	
        } else
        {
        	MainPrincipal.printLog("BYTESDECODIFICADOS NULL ");
        }
    
	}
}
