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


public class Main {
	
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
	 * SECTOR 21  
	 **/
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
        	sb = new StringBuilder();
        	sb.append("Ingresa # de sector DESDE :\n");
        	System.out.println(sb.toString());
        	sectores[0] = scanner.nextInt();
        	
        	sb = new StringBuilder();
        	sb.append("Ingresa # de sector HASTA :\n");
        	System.out.println(sb.toString());
        	sectores[1] = scanner.nextInt();
        	
        	leerCards(args);	 
        }else if (accion == 2)
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
        	sb.append("Encriptando.... " + plaintext + plaintext + plaintext + plaintext + plaintext + plaintext);
        	//System.out.println(sb.toString());
        	printLog(sb.toString());
        	try 
        	{
        		AES aes = new AES();
        		plaintext = plaintext + plaintext + plaintext + plaintext + plaintext + plaintext;
        		// ENCRIPTAR 
				byte[] cipher = aes.encrypt(plaintext, encryptionKey);
				String str = new String(cipher, StandardCharsets.UTF_8);
				printLog("Valor de texto (STANDARDCHARSETS.UTF8): " + str + " -- TAMANIO : " + cipher.length); 
				
				// DESENCRIPTANDO... 
				String decrypted = aes.decrypt(cipher, encryptionKey);
				printLog("decrypt: " + decrypted);
			} catch (Exception e) {
				printLog("Error" + e.getMessage().toString());
			}
        	System.out.println("Intruccion finalizada... ");

        } else if (accion == 4) 	// TRABAJANDO CON LOS OPERADORES DE BITS...
        {
        	sb = new StringBuilder(); 
        	//DATOS_BYTE = hexStringToString(DATOS_BYTE);  
        	//sb.append(formatMoneda(leToNumeric(DATA_CARGA_B22, 4)) + "\n");
        	//byte[] b = encode(10000);
        	//sb.append(decode(b) + "\n");
        	//long l = Double.doubleToLongBits(10.00d);
        	//long l = (long) 100200; 
        	
        	//double[] d = new double[]{};
        	//printLog(leToNumericString(DATA_CARGA_B21, 4) + " cantidad");
        	printLog((8738 & 15) + " << -- CM");
        	printLog(float2Byte(10.50f).length + " < -- CANTIDAD\n" + 
        				bytearray2float(float2Byte(10.50f)));
        	
        	
        	//printLog(b.length + " << size \n");
        	//printLog(convertToDouble(b) + " << todouble ..");
			//printLog(sb.toString());
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
    
    
    
    // double2Byte method - writes doubles to byte array
    public static final byte[] float2Byte(float inData) {
    	int bits = Float.floatToIntBits(inData);
    	byte[] bytes = new byte[4];
    	bytes[0] = (byte)(bits & 0xff);
    	bytes[1] = (byte)((bits >> 8) & 0xff);
    	bytes[2] = (byte)((bits >> 16) & 0xff);
    	bytes[3] = (byte)((bits >> 24) & 0xff);
    	return bytes;
    }
    public static float bytearray2float(byte[] b) {
        ByteBuffer buf = ByteBuffer.wrap(b);
        return buf.getFloat();
    }
    
    
    
    
    public static byte[] convertToByteArray(double value) {
        byte[] bytes = new byte[16];
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.putDouble(value);
        return buffer.array();
    }
    public static double convertToDouble(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.getDouble();
    }
    /**
     * FUNCIONES DE ENCODIFICADO
     **/
    public static byte[] putLong(long value, int offset) 
    {
    	byte[] array = new byte[10];
    	
        array[offset]   = (byte)(0xff & (value >> 56));
        array[offset+1] = (byte)(0xff & (value >> 48));
        array[offset+2] = (byte)(0xff & (value >> 40));
        array[offset+3] = (byte)(0xff & (value >> 32));
        array[offset+4] = (byte)(0xff & (value >> 24));
        array[offset+5] = (byte)(0xff & (value >> 16));
        array[offset+6] = (byte)(0xff & (value >> 8));
        array[offset+7] = (byte)(0xff & value);
        array[offset+8] = (byte)(0xff & 0x00);
        array[offset+9] = (byte)(0xff & 0x00);
        
        return array;
      }

      public static long getLong(byte[] array, int offset) {
        return
          ((long)(array[offset]   & 0xff) << 56) |
          ((long)(array[offset+1] & 0xff) << 48) |
          ((long)(array[offset+2] & 0xff) << 40) |
          ((long)(array[offset+3] & 0xff) << 32) |
          ((long)(array[offset+4] & 0xff) << 24) |
          ((long)(array[offset+5] & 0xff) << 16) |
          ((long)(array[offset+6] & 0xff) << 8) |
          ((long)(array[offset+7] & 0xff));
      }
 
    
    
    public static byte[] longToByteArray(long value) {
        return new byte[] {
            (byte) (value >> 56),
            (byte) (value >> 48),
            (byte) (value >> 40),
            (byte) (value >> 32),
            (byte) (value >> 24),
            (byte) (value >> 16),
            (byte) (value >> 8),
            (byte) value
        };
    }
    private static long decodeLong(byte[] bi) {
  	  return 	bi[7] & 0xFF | (bi[6] & 0xFF) << 8 |
		  		(bi[5] & 0xFF) << 16 | (bi[4] & 0xFF) << 24 | 
		  		(bi[3] & 0xFF) << 32 | (bi[2] & 0xFF) << 40 | 
		  		(bi[1] & 0xFF) << 48 | (bi[0] & 0xFF) << 56;
  	}
    
    /**
     * Simple función que convierte un int a byte...
     * @param i int valor...
     * @return {@link Byte}
     **/
    public static byte[] IntToByte(int i)
    { 
    	return new byte[]{(byte)i};
    }
    
    /**
     * Simple función que convierte de byte a int.. 
     * @param b byte[] arreglo que se convertira a decimal..
     *   
     **/
    public static int ByteToInt(byte[] b)
    {
    	long l = 0;
    	int i = 0;
    	for (int j = 0; j < b.length; j++) 
    	{
    		i 	+= b[j] & 0xFF;
    		l	+= ((long) b[j] & 0xffL) << (8 * j); 	// 0 8 16 24
		}
    	l = l;
    	return i;
    }
    private static int decode(byte[] bi) {
    	  return bi[3] & 0xFF | (bi[2] & 0xFF) << 8 |
    	         (bi[1] & 0xFF) << 16 | (bi[0] & 0xFF) << 24;
    	}
    private static byte[] encode(int i) 
    {
    	  return new byte[] { (byte) (i >>> 24), (byte) ((i << 8) >>> 24),
    	                      (byte) ((i << 16) >>> 24), (byte) ((i << 24) >>> 24)};
    }
    
    /**
     * Simple función que recibe un arreglo de tipo byte[] ( 1 Bloque de 1 sector )
     * y lo convierte a un tipo de dato long. 
     * Utilizando desplazamiento de bits. (Fórmula 2^n)
     * 
     **/
    public static long leToNumeric(byte[] buffer) {
    	long value = 0; 
    	int i = 0;
        for (Byte b : buffer) 
        {
            //resultado += String.format("%02X", b.intValue() & 0xFF);
    		value 	  += ((long) b & 0xffL) << (8 * i); 	// 0 8 16 24
    		i ++;
        }  
    	return value;
    }
    public static long leToNumeric(byte[] buffer, int size) {
    	long value = 0;
    	for (int i=0; i < size; i++) 
    	{ 
    		value += ((long) buffer[i] & 0xffL) << (8 * i); 	// 0 8 16 24
    	}
    	return value;
    }
    
    public static String leToNumericString(byte[] buffer, int size) {
    	return String.valueOf(leToNumeric(buffer, size));
    }
    
    public static String formatMoneda(long valor) {
    	return "$"+String.format("%,d", valor);
    }
    
    
 
    
    private static void printLog(String data)
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
    private static void writeToCards(String... args) throws IOException {
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
                try { 
                    MifareUtils.writeToMifareClassic1KCard(mfReaderWriter, mfCard, sectorId, blockId, key, write[4].toUpperCase());
                } catch (CardException ce) {
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
    private static void listen(MfCardListener listener) throws IOException {
        Acr122Device acr122;
        
        try {
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
    private static void leerCards(String... args) throws IOException {
        // Building the list of keys
    	//System.out.println("Size : " + args.length);
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i < args.length; i++) { 

            String k = args[i].toUpperCase();
            if (MifareUtils.isValidMifareClassic1KKey(k)) {
                keys.add(k);
            }
        } 

        // Adding the common keys
        keys.addAll(MifareUtils.Claves1K);
        
        // Card listener for dump
        MfCardListener listener = new MfCardListener() {
            @Override
            public void cardDetected(MfCard mfCard, MfReaderWriter mfReaderWriter) throws IOException 
            {
                printCardInfo(mfCard); 
                
                try {
                    MifareUtils.dumpMifareClassic1KCards(mfReaderWriter, mfCard, keys, sectores);
                } catch (CardException ce) {
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
