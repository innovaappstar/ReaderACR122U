package com.aes; 

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.constantes.Constantes;
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
    private static String KEY_DESARROLLO    = "1234567890123456"; 
    private byte[] keyDesarrollo 			= KEY_DESARROLLO.getBytes();
 
    /*****************************************************************************************************/
    
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
    public byte[] encrypt(byte[] data) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(keyDesarrollo, ALGORITMO_CIPHER);
        Cipher cipher = Cipher.getInstance(ALGORITMO_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(data);
        return encrypted;
    }
    /**
     * Simple funci�n que desencriptar� una arreglo de bytes
     * y devolver� una cadena ...
     * @param cipherText byte que contiene la tarjeta.
     * @return String desencriptado.
     **/
    @Override
    public byte[] decrypt(byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(keyDesarrollo, ALGORITMO_CIPHER);
        Cipher cipher = Cipher.getInstance(ALGORITMO_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
    
    
    
    /**
     * Simple funci�n que generar� un cifrado de keys
     * en formato byte[]
     * @return key que se utilizar� en {@link SecretKeySpec}
     **/
    private byte[] getKey() throws Exception
    {
        //SecureRandom secureRandom	= SecureRandom.getInstance(ALGORITMO_SHA1, "Crypto");
        SecureRandom secureRandom	= SecureRandom.getInstance(ALGORITMO_SHA1);
        KeyGenerator generadorKey 	= KeyGenerator.getInstance(ALGORITMO_CIPHER);
        secureRandom.setSeed(KEY_DESARROLLO.getBytes());
        generadorKey.init(TAMANIO_BITS, secureRandom);
        SecretKey keySecreta 		= generadorKey.generateKey();
        byte[] key 	= keySecreta.getEncoded();
        return key;
    }
    
}
