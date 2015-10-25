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
        	leerCards(args);	 
        }else if (accion == 2)
        {
        	//writeToCards(args);	// ESCRIBIR
        } 
    }
	
    
    /**
     * Listens PARA CARDS USANDO EL LISTENER PROPORCIONADO.
     * @param listener a listener
     */
    private static void listen(MfCardListener listener) throws IOException {
        Acr122Device acr122;
        
        try {
            acr122 = new Acr122Device();
        } catch (RuntimeException re) {
            System.out.println("No ACR122 reader found.");
            return;
        }
        acr122.open();
        	
        acr122.listen(listener);
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
        	System.out.println("2 : ");

            String k = args[i].toUpperCase();
            if (MifareUtils.isValidMifareClassic1KKey(k)) {
                keys.add(k);
            }
        }
    	System.out.println("3 : ");

        // Adding the common keys
        keys.addAll(MifareUtils.COMMON_MIFARE_CLASSIC_1K_KEYS);
        
        // Card listener for dump
        MfCardListener listener = new MfCardListener() {
            @Override
            public void cardDetected(MfCard mfCard, MfReaderWriter mfReaderWriter) throws IOException 
            {
                printCardInfo(mfCard); 
                
                try {
                    MifareUtils.dumpMifareClassic1KCard(mfReaderWriter, mfCard, keys);
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
    private static String fechaHora()
    { 
    	return new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss").format(new Date());
    }
	
}
