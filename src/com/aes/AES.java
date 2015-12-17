package com.aes; 

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.interfaces.IAES;
 

/**
 * Created by innovaapps on 15/12/2015.
 * Simple clase que se encargar� de
 * la encriptaci�n de los datos...
 */
public class AES implements IAES
{
    /**
     * Define el algoritmo para la operaci�n de la
     * encriptaci�n.
     **/
    private static String ALGORITMO_CIPHER	= "AES";
    /**
     * Define el algoritmo de generaci�n de n�meros
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
    private static String KEY_DESARROLLO    = "abcdefghehehehehehee";

    /**
     * Simple funci�n que se encargar� de encriptar textos planos
     * y regresar un arreglo de bytes.
     * @param plainText String que se tendr� que cifrar.
     * @return #cipher byte[] cifrado...
     * getInstance(NOMBRE_TRANSFORMACI�N_SOLICITADA, PROVEEDOR)
     * TRANSFORMACION = ES UNA CADENA QUE DESCRIBRE EL FUNCIONAMIENTO...
     * TRANSFORMACION : ALGORITMO/M�DO/PADDING
     *
     * init (int OPMODE, clave  fundamental, AlgorithmParameterSpec  params)
     * Inicializa esta cifra con una clave y un conjunto de par�metros del algoritmo o VECTOR DE INICIALIZACI�N.
     **/
    @Override
    public byte[] encrypt(String plainText) throws Exception
    {
        Cipher cipher 		= Cipher.getInstance(ALGORITMO_CIPHER);
        SecretKeySpec skey 	= new SecretKeySpec(getKey(), ALGORITMO_CIPHER);

        cipher.init(Cipher.ENCRYPT_MODE, skey);
        return cipher.doFinal(plainText.getBytes());
    }

    /**
     * Simple funci�n que desencriptar� una arreglo de bytes
     * y devolver� una cadena ...
     * @param cipherText byte que contiene la tarjeta.
     * @return String desencriptado.
     **/
    @Override
    public String decrypt(byte[] cipherText) throws Exception
    {
        Cipher cipher 		= Cipher.getInstance(ALGORITMO_CIPHER);
        SecretKeySpec skey 	= new SecretKeySpec(getKey(), ALGORITMO_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, skey);
        return new String(cipher.doFinal(cipherText));
    }

    /**
     * Simple funci�n que generar� un cifrado de keys
     * en formato byte[]
     * @return key que se utilizar� en {@link SecretKeySpec}
     **/
    private byte[] getKey() throws Exception
    {
        SecureRandom secureRandom	= SecureRandom.getInstance(ALGORITMO_SHA1);
        KeyGenerator generadorKey 	= KeyGenerator.getInstance(ALGORITMO_CIPHER);
        secureRandom.setSeed(KEY_DESARROLLO.getBytes());
        generadorKey.init(TAMANIO_BITS, secureRandom);
        SecretKey keySecreta 		= generadorKey.generateKey();
        byte[] key 	= keySecreta.getEncoded();
        return key;
    }
}
