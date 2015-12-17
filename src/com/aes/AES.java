package com.aes; 

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.interfaces.IAES;
 

/**
 * Created by innovaapps on 15/12/2015.
 * Simple clase que se encargará de
 * la encriptación de los datos...
 */
public class AES implements IAES
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
    private static String KEY_DESARROLLO    = "abcdefghehehehehehee";

    /**
     * Simple función que se encargará de encriptar textos planos
     * y regresar un arreglo de bytes.
     * @param plainText String que se tendrá que cifrar.
     * @return #cipher byte[] cifrado...
     * getInstance(NOMBRE_TRANSFORMACIÓN_SOLICITADA, PROVEEDOR)
     * TRANSFORMACION = ES UNA CADENA QUE DESCRIBRE EL FUNCIONAMIENTO...
     * TRANSFORMACION : ALGORITMO/MÓDO/PADDING
     *
     * init (int OPMODE, clave  fundamental, AlgorithmParameterSpec  params)
     * Inicializa esta cifra con una clave y un conjunto de parámetros del algoritmo o VECTOR DE INICIALIZACIÓN.
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
     * Simple función que desencriptará una arreglo de bytes
     * y devolverá una cadena ...
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
     * Simple función que generará un cifrado de keys
     * en formato byte[]
     * @return key que se utilizará en {@link SecretKeySpec}
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
