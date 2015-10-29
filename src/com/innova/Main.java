package com.innova;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.smartcardio.CardException;

import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;


public class Main {
	
	final static int[] sectores = new int[2];
	
	 
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
        	String[] params = new String[]{"3","3", "2", "FFFFFFFFFFFF", "HolamundodesdeJava".substring(0, 16)}; 
        	writeToCards(params);	// ESCRIBIR
        } 
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
                    MifareUtils.writeToMifareClassic1KCard(mfReaderWriter, mfCard, sectorId, blockId, key, data);
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
        System.out.println("Press ENTER to exit");
        System.in.read();
        
        acr122.close();
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
