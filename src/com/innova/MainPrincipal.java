package com.innova;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.smartcardio.CardException;

import org.apache.commons.logging.Log;
import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;

import com.aes.AES;
import com.aes.AES_OLD; 
import com.aes.AESEncrypt;
import com.aes.ManagerAES;
import com.beans.Tarjeta;
import com.operaciones.Operaciones;
import com.utils.ManagerUtils;


public class MainPrincipal {
	
	final static int[] sectores = new int[2];
	final static String[] write = new String[5];
	/**
	 * Texto Plano que se encriptara..
	 * Key del texto encriptado... 
	 **/ 
	//private static String plaintext = "test text 123\0\0\0"; /*Note null padding*/
	private static String plaintext = "1234567890123456"; /*Note null padding*/
	public static String encryptionKey = "0123456789abcdef";
	
	/**
	 * DEFINICIÓN DE SECTORES A TRABAJAR 
	 **/
	public static int SECTOR_L01 = 0x01; 	// ISALDO
	public static int SECTOR_L02 = 0x02; 	// ITRANSACCION
	public static int SECTOR_L03 = 0x03; 	// IPRFM
	public static int SECTOR_L04 = 0x04; 	// DETALLE 
	
	/**
	 * DEFINICIÓN DE BLOQUES A TRABAJAR 
	 **/
	public static int BLOQUE_L01 = 0x02;	// BSALDO
	public static int BLOQUE_L02 = 0x01;	// BTRANSACCION
	public static int BLOQUE_L03 = 0x02;	// BPRFM
	public static int BLOQUE_L04 = 0x00;	// CÓDIGO DE TARJETA - DNI 
	
	/**
	 * LIMITANTES PARA LOS FORMATOS DE VALORES EN LA TARJETA
	 * DECIMAS LUEGO DE LA COMA
	 **/
	//public static String STR_SALDO_FORMAT 		= "000000#000000#00"; //"0000000000000000";
	public static String MON_FORMAT = "%.2f"; 
	
	public static String STR_TRANSACCION_FORMAT	= "00000#0000#00000"; //"0000000000000000";
	//FORMATO PARA DETALLE DE TARJETA (COD#DNI  || TIPO#FECHA)
	public static String STR_DETALLE_TARJETA_FORMAT	= "0000000#00000000"; //"0000000000000000";
	public static String STR_DETALLE_TARJETA_FO	= ""; //"0000000000000000";
	
	 

     
	/**
	 * KEYS DE DESARROLLO 
	 **/ 
	public static byte[] SECTOR_TRAILER_L01 	= new byte[]{	(byte)0x1E15E,(byte)0xAA0E,(byte)0x107CE,(byte)0x2A12D,(byte)0x221456,(byte)0x5B88,
																//(byte)0xFC,(byte)0x37,(byte)0x80,(byte)0xFF,	// ACCESS BITS	( 2 - 2 - 0 - 4) 0 = TRANSPORT CONFIGURATION
																(byte)0xFE,(byte)0x17,(byte)0x80,(byte)0xFF,	// ACCESS BITS	( 2 - 0 - 0 - 4) 0 = TRANSPORT CONFIGURATION
																(byte)0x5A48,(byte)0x4BD,(byte)0x2F64,(byte)0x302D,(byte)0x54B7,(byte)0x15EB0};
	
	
	// KEY AES DESARROLLO
	public static String ENCRYPTION_KEY_AES 	= "6D9223EAA6826000";
	
	/**
	 * CÓDIGOS DE TIPOS DE TARJETAS  
	 **/
	public static int COD_TARJETA_TIPO_GENERAL 			= 11;
	public static int COD_TARJETA_TIPO_ESTUDIANTE 		= 12;
	public static int COD_TARJETA_TIPO_UNIVERSITARIO	= 13;
	public static int COD_TARJETA_TIPO_CONDUCTOR 		= 14;
	
	/**
	 * FECHAS DE VENCIMIENTO REFERENCIA 
	 **/
	public static String FECHA_VENCIMIENTO_TARJETA_GENERAL 			= "12/12/2020"; // SEGUN BD 2020-09-18 12:00:00.000
	public static String FECHA_VENCIMIENTO_TARJETA_ESTUDIANTE 		= "12/12/2016"; // SEGUN BD -----------------------
	public static String FECHA_VENCIMIENTO_TARJETA_UNIVERSITARIO 	= "12/12/2016"; // SEGUN BD -----------------------
	
	
	
	
	
	/**
	 * RETORNO VACIO 
	 **/
	public static String VACIO = ""; 

	
	
	
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
        sb.append("\t-(3), --Random\tDevolver BYTES DE INTEGRIDAD\n"); 
        sb.append("\t-Ingresar #Función"); 
        System.out.println(sb.toString());
         
        int accion = scanner.nextInt();
        
        if (accion == 1)	// LECTURA	DE BLOQUES
        {
        	sb = new StringBuilder();
        	sb.append("Ingresa # de sector DESDE :\n");
        	System.out.println(sb.toString());
        	sectores[0] = scanner.nextInt();
        	
        	sb = new StringBuilder();
        	sb.append("Ingresa # de sector HASTA :\n");
        	System.out.println(sb.toString());
        	sectores[1] = scanner.nextInt();
        	
        	leerCards(args);	 
        }else if (accion == 2)	// ESCRITURA DE BLOQUES
        {
        	//String[] params = new String[]{"3","3", "2", "FFFFFFFFFFFF", "HolamundodesdeJava".substring(0, 16)};  
        	write[0] = "0";
        	write[3] = "FFFFFFFFFFFF";
        	
        	sb = new StringBuilder();
        	sb.append("Ingresa # de Sector : ");
        	System.out.println(sb.toString());
        	write[1] = scanner.next();
        	
        	sb = new StringBuilder();
        	sb.append("Ingresa # de Bloque : ");
        	System.out.println(sb.toString());
        	write[2] = scanner.next();
        	
        	sb = new StringBuilder();
        	sb.append("Ingresa Datos :");
        	System.out.println(sb.toString());
        	/**
        	 * PASOS PARA ESCRIBIR :
        	 * REVISAR EL TIPO DE CLAVE A USAR : DEFAULT O KEY.A MODIFICADA
        	 * COMENTAR O DESCOMENTAR LA LINEA QUE ESCRIBIRA UN BLOQUE DE DATO O BLOQUE TRAILER
        	 * EN ORDEN ASCENDENTE IR DESCOMENTANDO UNO A LA VEZ PARA ENVIAR LOS DATOS A ESCRIBIR CON EL LECTOR,
        	 * LOS PARÁMETROS QUE VARIARAN SERÁN : 
        	 * formatDetalleTarjeta
        	 * formatDetalleTarjetaL02
        	 * NOTA : TODA TARJETA TENDRA EL MISMO PROCEDIMIENTO CON LA EXCEPCIÓN DE QUE 
        	 * VARIARÁ EN EL SECTOR 3 AL GRABAR LOS DETALLES DE LA TARJETA:
        	 * COD TARJETA - DNI
        	 * TIPO TARJETA - FECHA VENCIMIENTO
        	 */
        	Tarjeta tarjeta = new Tarjeta();
    		tarjeta.setAccion(2);
    		tarjeta.setSaldo(151.355f);
    		// VARIABLES
    		tarjeta.setCodUsuarioTarjeta(12);
    		tarjeta.setDniUsuarioTarjeta(49502503);
    		tarjeta.setTipoUsuarioTarjeta(11);	// 11 - 12 - 13 - 14  
    		tarjeta.setFechaVencimientoUsuarioTarjeta(FECHA_VENCIMIENTO_TARJETA_GENERAL);
    		
     		Operaciones operaciones = new Operaciones(); 
    		
    		String cadenaTarjeta	= operaciones.formatSaldo(tarjeta);
    		printLog(" Cadena tarjeta --> " + cadenaTarjeta + "|" + cadenaTarjeta.length());
			write[4]	= cadenaTarjeta; 
        	//write[4] = scanner.next().substring(0, 16);
        	//write[4] = formatSaldo(0f);	// INFORMACIÓN DEL SALDO
        	//write[4] = formatTransacciones(0f);	// HISTORIAL TRANSACCIONES
        	// VARIANTES DETALLE TARJETA
        	//write[4] = formatDetalleTarjeta(15, 46858258);	// CODTARJETA-DNI
        	//write[4] = formatDetalleTarjetaL02(COD_TARJETA_TIPO_UNIVERSITARIO, FECHA_VENCIMIENTO_TARJETA_UNIVERSITARIO);	// TIPO TARJETA - FECHA VENCIMIENTO
        	//write[4] = "0000000000000000";
        	
        	
        	writeToCards(write);	// ESCRIBIR
        } else if (accion == 3)
        { 
    		Tarjeta tarjeta = new Tarjeta();
    		tarjeta.setAccion(3);
    		tarjeta.setSaldo(0f);
    		// VARIABLES
    		tarjeta.setCodUsuarioTarjeta(15);
    		tarjeta.setDniUsuarioTarjeta(46858258);
    		tarjeta.setTipoUsuarioTarjeta(11);
    		tarjeta.setFechaVencimientoUsuarioTarjeta(FECHA_VENCIMIENTO_TARJETA_GENERAL);
    		
    		ManagerAES managerAES 	= new ManagerAES();
    		Operaciones operaciones = new Operaciones(); 
    		
    		String cadenaTarjeta	= operaciones.formatSaldo(tarjeta);
    		byte[] cipher = null;
    		String cipherText = "";
			try { 
	    		
			} catch (Exception e) 
			{
				e.printStackTrace();
			} 
        } else if (accion == 4) 	// TRABAJANDO CON LOS OPERADORES DE BITS...
        {} else if (accion == 5)
        {
        	printLog(HexUtils.isIntString("1111112a") + "");
        	
        } else if (accion == 6)
        {
        	  
        	 
        } else if (accion == 7)
        {
    		Tarjeta tarjeta = new Tarjeta();
    		tarjeta.setAccion(3);
    		tarjeta.setSaldo(0f);
    		// VARIABLES
    		tarjeta.setCodUsuarioTarjeta(15);
    		tarjeta.setDniUsuarioTarjeta(46858258);
    		tarjeta.setTipoUsuarioTarjeta(11);
    		tarjeta.setFechaVencimientoUsuarioTarjeta(FECHA_VENCIMIENTO_TARJETA_GENERAL);
    		
    		ManagerAES managerAES 	= new ManagerAES();
    		Operaciones operaciones = new Operaciones(); 
    		
    		String cadenaTarjeta	= operaciones.formatSaldo(tarjeta);
    		  
            //String data     = "";
            String hexCipher = "6BECD0CA87CC9108C86D4CA3AE5C85DD";
            try 
            {
                byte[] cipher 	= managerAES.encrypt(cadenaTarjeta.getBytes());
                printLog("Tamaño cipher : " + cipher.length + 
                			"\nHex Cipher : \n" + HexUtils.bytesToHexString(cipher)); 
            }catch (Exception e)
            {
            	printLog(e.getMessage());
            }
        }
        
    }  
  
    
    /**
     * @param idkey IDKEY enum identificador...
     * Simple función que devolverá un byte array 
     * con el tipo de clave que se soliscito. 
     * @return byte[] A or B
     **/
    public static byte[] KEY(IDKEY idkey)
    {
    	// KEY A - DEFAULT
    	int start 	= 0;
    	int end		= 6;
    	switch (idkey) 
    	{
		case KEY_A:
			start 	= 0;
			end		= 6;
			break;
		case KEY_B:
			start 	= 10;
			end		= 16;  
			break;
		}  
    	return Arrays.copyOfRange(SECTOR_TRAILER_L01, start, end);
    }
    
    /**
     * Simple enum para devolver identificador de key 
     **/
    public enum IDKEY
    {
    	KEY_A	(1),
    	KEY_B	(2);
    	
    	private int n;
    	private IDKEY(int n) 
    	{
			// TODO Auto-generated constructor stub
    		this.n = n;
		}
    	public int getInt()
    	{
    		return n;
    	}
    }
    
    
    
    public static void printLog(String data)
    {
    	System.out.println(data + "\n");
    }
    
    public static void ingresarNuevaData()
    {
    	StringBuilder sb = new StringBuilder();  
        Scanner scanner = new Scanner(System.in); 
    	sb.append("---------------------\nIngresa Datos Nuevos:");
    	System.out.println(sb.toString());
    	write[4] = scanner.next().substring(0, 16);
    }
    public static void ingresarNuevoSector()
    {
    	StringBuilder sb = new StringBuilder();  
        Scanner scanner = new Scanner(System.in); 
    	sb.append("---------------------\nIngresa Sector :");
    	System.out.println(sb.toString());
    	write[1] = scanner.next();
    }
    
     
    
    /**
     * Writes to cards.
     * @param args the arguments of the write command
     */
    private static void writeToCards(String... args) throws IOException 
    {
        // Checking arguments
        if (args.length != 5) { 
        }
        
        final String sector = args[1];
        final String block = args[2];
        final String key = args[3].toUpperCase();
        final String data = args[4].toUpperCase();
        if (!MifareUtils.isValidMifareClassic1KSectorIndex(sector)
                || !MifareUtils.isValidMifareClassic1KBlockIndex(block)
                || !MifareUtils.isValidMifareClassic1KKey(key)
                || !HexUtils.isHexString(data)) {
        	
        }
        
        final int sectorId = Integer.parseInt(sector);
        final int blockId = Integer.parseInt(block);
        
        // Card listener for writing
        MfCardListener listener = new MfCardListener() {
            @Override
            public void cardDetected(MfCard mfCard, MfReaderWriter mfReaderWriter) throws IOException {
                printCardInfo(mfCard);
                try 
                {  
                     MifareUtils.writeToMifareClassic1KCard(mfReaderWriter, mfCard, Integer.valueOf(write[1]), blockId, key, write[4].toUpperCase()); 
                    //MifareUtils.writeToMifareClassic1KCard(mfReaderWriter, mfCard, sectorId, blockId, key, write[4].toUpperCase());
                } catch (CardException ce) 
                {
                    System.out.println("Card removed or not present.");
                }
            }
        };

        // Start listening
        listen(listener);
    }
    
    /**
     * Listens PARA CARDS USANDO EL LISTENER PROPORCIONADO.
     * @param listener a listener
     */
    private static void listen(MfCardListener listener) throws IOException 
    {
        Acr122Device acr122;
        
        try 
        {
            acr122 = new Acr122Device();	// Busca Terminal ACR 122
        } catch (RuntimeException re) {
            System.out.println("No ACR122 reader found.");
            return;
        }
        acr122.open();	// Iniciamos comunicación con Terminal
        	
        acr122.listen(listener);		// 
        /*
        System.out.println("Press ENTER to exit");
        System.in.read();
        
        acr122.close();
        */
    }
    
    /**
     * Imprime información de la tarjeta
     * @param card a card
     */
    private static void printCardInfo(MfCard card) 
    { 
    	String linea = "-----------------------------------------------------------------------------";
        System.out.println(linea + "\nTarjeta detectada: " + card.getTagType().toString() + " " + " <Date> " + fechaHora() + "\n" + linea);
    }
    
    /**
     * Leer cards.
     * @param args the arguments of the dump command
     */
    private static void leerCards(String... args) throws IOException 
    {
        // Building the list of keys
    	//System.out.println("Size : " + args.length);
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i < args.length; i++) 
        { 
	        String k = args[i].toUpperCase();
	        if (MifareUtils.isValidMifareClassic1KKey(k)) 
	        {
	            keys.add(k);
	        }
        } 

        // Adding the common keys
        keys.addAll(MifareUtils.Claves1K);
        
        // Card listener for dump
        MfCardListener listener = new MfCardListener() 
        {
            @Override
            public void cardDetected(MfCard mfCard, MfReaderWriter mfReaderWriter) throws IOException 
            {
                printCardInfo(mfCard); 
                
                try 
                {
                    MifareUtils.dumpMifareClassic1KCards(mfReaderWriter, mfCard, keys, sectores);
                } catch (CardException ce) 
                {
                    System.out.println("Tarjeta removida o no esta presente.");
                }
                
            }
        }; 
        
        // Iniciar listening
        listen(listener);
    }
     
    
    /**
     *	@return String  FECHA HORA FORMATEADA
     */
    public static String fechaHora()
    { 
    	return new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss:S").format(new Date());
    }
	
}
