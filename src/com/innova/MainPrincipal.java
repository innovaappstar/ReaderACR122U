package com.innova;

import static com.innova.HexUtils.hexStringToBytes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.smartcardio.CardException;

import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;

import com.aes.AES;


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
	 * @param min int valor mínimo del rango.
	 * @param max int valor máximo del rango.
	 * @return String hex al azar. 
	 **/
	
	public static String randHex(int min, int max, int capacity) 
	{ 
	    Random rand = new Random(); 
	    //	nextInt normalmente es exclusiva del valor superior,
	    // por lo que añadir 1 para que sea incluido
	    // Por encima de fórmula voluntad genera un entero aleatorio en un rango entre el mínimo (inclusive) y máximo (inclusive).
	    int num = rand.nextInt((max - min) + 1) + min;  
	    StringBuilder hexRandom = new StringBuilder(Integer.toHexString(num));
	    
	    if (hexRandom.length() != 6)
	    	return VACIO;	// HEXADECIMAL NO PERMITIDO 
	    
	    hexRandom.setLength(capacity);
	    return hexRandom.toString();
	}
	
	/**
	 * @param m float es el monto.
	 * @param k String es la key.
	 * @return String de 16 bytes.
	 */
	public static String formatSaldo(float m, String k)
	{
		if (m > 999f) 
			return VACIO;	// <<ERROR CODE 15 : FLOAT EXCEDIO DEL LÍMITE>>
		
		// PARSEAMOS FLOAT A DECIMAL
		String f = String.format(MON_FORMAT, m).replace(",", "");
		// CONVERTIMOS DECIMAL A HEX 
		f	= Integer.toHexString(Integer.valueOf(f));
		
		StringBuilder strb = new StringBuilder(STR_TRANSACCION_FORMAT);
		int start 	= 5 - f.length();	// 4 -> 1.25
		int end		= 5;
		String str	= f;
		
		// simple conversión
		printLog((Integer.decode("0x" + f)) + " << decimal convertido..");
		// BSALDO-01 SALDO
		strb.replace(start, end, str); 
		
		// BSALDO-02 BYTES DE INTEGRIDAD
		start 		= 6;
		end			= 10;
		str			= randHex(2222222, 9999999, 0x04); 
		if (str.length() == 0)
		{
			return VACIO;
		}
		
		strb.replace(start, end, str);
		
		strb.setLength(16);
		
		return strb.toString();
	}
	
	/**
	 *  @param m float que simbolizará la nueva transacción.
	 *  @param k String que contendra la key
	 *  @return String {@link #formatTransacciones(float, String)}
	 **/
	public static String formatTransacciones(float m, String k)
	{
		if (m > 999f) 
			return VACIO;	// <<ERROR CODE 15 : FLOAT EXCEDIO DEL LÍMITE>>
		  
		String f = "";
		// VALIDAR QUE SI NO CONTIENE UNA COMA ","
		f 	= String.format(MON_FORMAT, m).replace(",", "");
		f	= Integer.toHexString(Integer.valueOf(f));

		StringBuilder strb = new StringBuilder(STR_TRANSACCION_FORMAT);
		int start 	= 5 - f.length();	 
		int end		= 5;
		String str	= f;
		
		// simple conversión
		printLog((Integer.decode("0x" + f)) + " << decimal convertido.."); 
		// BSALDO-01 SALDO
		strb.replace(start, end, str); 
		
		// BSALDO-02 BYTES DE INTEGRIDAD
		start 		= 6;
		end			= 10;
		str			= randHex(2222222, 9999999, 0x04); 
		if (str.length() == 0)
		{
			return VACIO;
		}
		
		strb.replace(start, end, str);
		
		// TRANSACCION ACTUAL 
		f 	= String.format(MON_FORMAT, m).replace(",", "");
		f	= Integer.toHexString(Integer.valueOf(f));

		start 	= 16 - f.length();	 
		end		= 16;
		str		= f;
		 
		// BSALDO-01 SALDO
		strb.replace(start, end, str); 
		
		printLog(strb.length() + " << Size StringBuilder");
		strb.setLength(16);
		
		return strb.toString();
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
        	write[4] = scanner.next().substring(0, 16);
        	
        	
        	writeToCards(write);	// ESCRIBIR
        } else if (accion == 3)
        {
        	sb = new StringBuilder();
        	 
        	//printLog(sb.toString());
        	String salida = formatTransacciones(0f, "KEYKEY");
        	if (salida.length() > 0)
        	{
        		printLog(salida);
        	}else
        	{
        		printLog("OCURRIÓ UN ERROR");
        	}
        	  
        } else if (accion == 4) 	// TRABAJANDO CON LOS OPERADORES DE BITS...
        { 
             
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
                    MifareUtils.writeToMifareClassic1KCard(mfReaderWriter, mfCard, sectorId, blockId, key, write[4].toUpperCase());
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
