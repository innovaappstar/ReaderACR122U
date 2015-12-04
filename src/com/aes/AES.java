package com.aes;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * https://gist.github.com/bricef/2436364 
 **/

public class AES 
{ 
	/**
	 * Define formato de codificaci�n de car�cteres..
	 **/
	private static String FORMATO_UNICODE = "UTF-8";
	/**
	 * Define transformaci�n cipher de 128 bits con algoritmo AES..
	 **/
	private static String TRANSFORM_CIPHER = "AES/CBC/NoPadding";
	/**
	 * Define proveedor de la clase cipher..
	 **/
	private static String PROVEEDOR_CIPHER = "SunJCE";
	/**
	 * Define par�metros del vector de inicializaci�n
	 * para la encriptaci�n/desencriptaci�n, debe tener 
	 * longitud de 16 bytes.. 
	 **/
	private static String IV_CIPHER = "BCTEDNEAJDYHUJEJ"; 
	/**
	 * Define el algoritmo para la operaci�n de la
	 * encriptaci�n. 
	 **/
	private static String ALGORITMO_CIPHER	= "AES";
	
	
	static String IV3 = "AAAAGJEJEIWJIIIA";
	static String IV2 = "AAAAAAAAAAAAXAAA"; 
					  //"BBBBBBBBBBBBBBBB"; 
	
	static String plaintext = "test text 123\0\0\0"; /*Note null padding*/
	static String encryptionKey = "0123456789abcdef"; 
	
	  public static byte[] encrypt(String plainText, String encryptionKey) throws Exception 
	  {
		/**
		 * getInstance(NOMBRE_TRANSFORMACI�N_SOLICITADA, PROVEEDOR)
		 * TRANSFORMACION = ES UNA CADENA QUE DESCRIBRE EL FUNCIONAMIENTO... 
		 * TRANSFORMACION : ALGORITMO/M�DO/PADDING
		 **/  
		Cipher cipher 		= Cipher.getInstance(TRANSFORM_CIPHER, PROVEEDOR_CIPHER);
		SecretKeySpec key 	= new SecretKeySpec(encryptionKey.getBytes(FORMATO_UNICODE), ALGORITMO_CIPHER);
		
		/**
		 *  init (int OPMODE, clave  fundamental, AlgorithmParameterSpec  params)
		 *	Inicializa esta cifra con una clave y un conjunto de par�metros del algoritmo o VECTOR DE INICIALIZACI�N. 
		 **/ 
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV_CIPHER.getBytes(FORMATO_UNICODE)));
		//cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(plainText.getBytes(FORMATO_UNICODE));
	  }
	
	  public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception
	  {
		Cipher cipher = Cipher.getInstance(TRANSFORM_CIPHER, PROVEEDOR_CIPHER);
		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(FORMATO_UNICODE), ALGORITMO_CIPHER);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV_CIPHER.getBytes(FORMATO_UNICODE)));
		//cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(cipherText),FORMATO_UNICODE);
	  }

}
