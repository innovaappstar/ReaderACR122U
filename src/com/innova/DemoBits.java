package com.innova;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.smartcardio.CardException;

import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;

import com.aes.AES;


public class DemoBits {
	
	final static int[] sectores = new int[2];
	final static String[] write = new String[5];
	/**
	 * Texto Plano que se encriptara..
	 * Key del texto encriptado... 
	 **/ 
	//private static String plaintext = "test text 123\0\0\0"; /*Note null padding*/
	private static String plaintext = "1234567890123456"; /*Note null padding*/
	private static String encryptionKey = "0123456789abcdef";
	/**
	 * Tamanio : 16 bytes...
	 * 10.20
	 * 1020.2500502520 
	 **/
	private static byte[] KEY_B_SECTOR_0 = new byte[]{(byte)0x1F,(byte)0xC2,(byte)0x35,(byte)0xAC,(byte)0x13,(byte)0x09};
	private static byte[] KEY_B_SECTOR_8 = new byte[]{(byte)0x64,(byte)0xE3,(byte)0xC1,(byte)0x03,(byte)0x94,(byte)0xC2};
	private static byte[] DATA_CARGA_B21 = new byte[]{(byte)0x10,(byte)0x27,(byte)0x00,(byte)0x00,(byte)0xef,(byte)0xd8,(byte)0xff,(byte)0xff,(byte)0x10,(byte)0x27,(byte)0x00,(byte)0x00,(byte)0x21,(byte)0xde,(byte)0x21,(byte)0xde};
	private static byte[] DATA_CARGA_B22 = new byte[]{(byte)0x10,(byte)0x27,(byte)0x00,(byte)0x00,(byte)0xef,(byte)0xd8,(byte)0xff,(byte)0xff,(byte)0x10,(byte)0x27,(byte)0x00,(byte)0x00,(byte)0x22,(byte)0xdd,(byte)0x22,(byte)0xdd};
	
	 
	public static void main(String[] args)
	{ 
		try {
			mostrarOpciones(args);
		} catch (IOException e) {}
	}
	
    /**
     * Prints help and exits.
     * @throws IOException 
     */
    private static void mostrarOpciones(String[] args) throws IOException 
    { 
        
        StringBuilder sb = new StringBuilder("Opciones:\n");  
        Scanner scanner = new Scanner(System.in);
         
        sb.append("\t-(1), --Leer [KEYS...]\t\t Mifare Classic 1K cards\n");
        sb.append("\t-(2), --Escribir\tMifare Classic 1K cards using\n");
        sb.append("\t-(3), --Encrypt(AES)\tEncryption AES\n");
        sb.append("\t-(4), --Operadores\tDesplazamiento de bits\n");
        sb.append("\t-Ingresar #Función"); 
        System.out.println(sb.toString());
        
        
        int accion = scanner.nextInt();
        
        if (accion == 1)
        {  
        }else if (accion == 2)
        { 
        } else if (accion == 3)
        { 

        } else if (accion == 4) 	// TRABAJANDO CON LOS OPERADORES DE BITS...
        {
        	sb = new StringBuilder();  
        	printLog((8738 & 15) + " << -- CM"); 
        	 
        }
    }  
    
    /*
    private int getValorHex(int i)
    {
    	int bitmask = 0x000F;
    	//int valor 	= (byte)()
    	int bits = Float.floatToIntBits(inData);
    	
    	return 
    }
    */ 
    
    private static void printLog(String data)
    {
    	System.out.println(data + "\n");
    }
    /**
     *	@return String  FECHA HORA FORMATEADA
     */
    public static String fechaHora()
    { 
    	return new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss:S").format(new Date());
    }
	
}
