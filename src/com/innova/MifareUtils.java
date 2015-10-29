/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Marc de Verdelhan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.innova;

import static com.innova.HexUtils.bytesToHexString;
import static com.innova.HexUtils.hexStringToBytes;
import static com.innova.HexUtils.isHexString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.smartcardio.CardException;
import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfException;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.BlockResolver;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.classic.Key;
import org.nfctools.mf.classic.MemoryLayout;

/**
 * Mifare utility class.
 */
public final class MifareUtils {

    /** Mifare Classic 1K sector count */
    public static final int MIFARE_1K_SECTOR_COUNT = 16;
    
    /** Mifare Classic 1K block count (per sector) */
    public static final int MIFARE_1K_PER_SECTOR_BLOCK_COUNT = 4;
    
    /** Common Mifare Classic 1K keys */
    public static final List<String> COMMON_MIFARE_CLASSIC_1K_KEYS = Arrays.asList(
        "001122334455",
        "000102030405",
        "A0A1A2A3A4A5",
        "B0B1B2B3B4B5",
        "AAAAAAAAAAAA",
        "BBBBBBBBBBBB",
        "AABBCCDDEEFF",
        "FFFFFFFFFFFF"
    );
    /** Keys Personalizadas */
    public static final List<String> Claves1K = Arrays.asList("FFFFFFFFFFFF");
    
    
    static List<String> keysMap = null;

    
    private MifareUtils() {
    }
    
    /**
     * @param s a string
     * @return true if the provided string is a valid Mifare Classic 1K key, false otherwise
     */
    public static boolean isValidMifareClassic1KKey(String s) {
        return isHexString(s) && (s.length() == 12);
    }
    
    /**
     * @param s a string
     * @return true if the provided string is a valid Mifare Classic 1K sector index, false otherwise
     */
    public static boolean isValidMifareClassic1KSectorIndex(String s) {
        try {
            int sectorIndex = Integer.parseInt(s);
            return sectorIndex >= 0 && sectorIndex < MIFARE_1K_SECTOR_COUNT;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    /**
     * @param s a string
     * @return true if the provided string is a valid Mifare Classic 1K block index, false otherwise
     */
    public static boolean isValidMifareClassic1KBlockIndex(String s) {
        try {
            int sectorIndex = Integer.parseInt(s);
            return sectorIndex >= 0 && sectorIndex < MIFARE_1K_PER_SECTOR_BLOCK_COUNT;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    /**
     * Dumps a Mifare Classic 1K card.
     * @param reader the reader
     * @param card the card
     * @param keys the keys to be tested for reading
     */
    public static void dumpMifareClassic1KCard(MfReaderWriter reader, MfCard card, List<String> keys)
            throws CardException 
    {
    	keysMap = new ArrayList<>();
    	keysMap.addAll(keys);
        for (int sectorIndex = 0; sectorIndex < MIFARE_1K_SECTOR_COUNT; sectorIndex++) 
        {
            // For each sector...
            for (int blockIndex = 0; blockIndex < MIFARE_1K_PER_SECTOR_BLOCK_COUNT; blockIndex++) {
                // For each block...
                dumpMifareClassic1KBlock(reader, card, sectorIndex, blockIndex, keysMap);
            }
        }
    }
    
    public static void dumpMifareClassic1KCards(MfReaderWriter readerWriter, MfCard card, List<String> keys, int[] sectores) throws CardException
    {
    	keysMap = new ArrayList<>();
    	keysMap.addAll(keys);
    	int mDesde	= sectores[0];
    	int mHasta	= sectores[1];
    	for (int iSector = mDesde; iSector < mHasta; iSector++)
    	{
    		for (int iBloque = 0; iBloque < MIFARE_1K_PER_SECTOR_BLOCK_COUNT; iBloque++) 
    		{
    			dumpMifareClassic1KBlock(readerWriter, card, iSector, iBloque, keysMap);
			}
    	}
    	
    }
    
    /**
     * Write data to a Mifare Classic 1K card.
     * @param reader the reader
     * @param card the card
     * @param sectorId the sector to be written
     * @param blockId the block to be written
     * @param key the key to be used for writing
     * @param dataString the data hex string to be written
     */
    public static void writeToMifareClassic1KCard(MfReaderWriter reader, MfCard card, int sectorId, int blockId, String key, String dataString)
            throws CardException {
        if (!isValidMifareClassic1KKey(key)) {
            System.out.println("The key " + key + "is not valid.");
            return;
        }
        /**SIMPLE CONVERSIÓN*/
        byte[] b = dataString.getBytes();
        dataString = bytesToHexString(b);
        
        if (!isHexString(dataString)) {
            System.out.println(dataString + " is not an hex string.");
            return;
        }
        
        byte[] keyBytes = hexStringToBytes(key);
        // Reading with key A
        MfAccess access = new MfAccess(card, sectorId, blockId, Key.A, keyBytes);
        String blockData = readMifareClassic1KBlock(reader, access);
        if (blockData == null) {
            // Reading with key B
            access = new MfAccess(card, sectorId, blockId, Key.B, keyBytes);
            blockData = readMifareClassic1KBlock(reader, access);
        }
        
        System.out.print(Main.fechaHora() + " -- Data pasada del bloque: ");
        if (blockData == null) {
            // Failed to read block
            System.out.println("<Failed to read block>");
        } else {
            // Block read
            System.out.println(blockData + " (Key " + access.getKey() + ": " + key + ")");

            // Writing with same key
            boolean written = false;
            try {
                byte[] data = hexStringToBytes(dataString);
                MfBlock block = BlockResolver.resolveBlock(MemoryLayout.CLASSIC_1K, sectorId, blockId, data);
                written = writeMifareClassic1KBlock(reader, access, block);
            } catch (MfException me) {
                System.out.println(me.getMessage());
            }
            if (written) {
                blockData = readMifareClassic1KBlock(reader, access);
                System.out.print(Main.fechaHora() + " -- Nueva data del bloque: ");
                if (blockData == null) {
                    // Failed to read block
                    System.out.println("<Failed to read block>");
                } else {
                    // Block read
                    System.out.println(blockData + " (Key " + access.getKey() + ": " + key + ")");
                }
            }
        }
    }
    
    /**
     * Reads a Mifare Classic 1K block.
     * @param reader the reader
     * @param access the access
     * @return a string representation of the block data, null if the block can't be read
     */
    private static String readMifareClassic1KBlock(MfReaderWriter reader, MfAccess access)
            throws CardException {
        String data = null;
        try {
            MfBlock block = reader.readBlock(access)[0];
            data = bytesToHexString(block.getData());
            //System.out.println("SIZE --> " + reader.readBlock(access).length);
        } catch (IOException ioe) {   
        	String linea = "-------------------------------------------------------------------------------\n";
            //System.out.println(linea + ioe.getMessage().toString() + "\n" + ioe.getCause().getMessage().toString());
            System.out.println("error : " + ioe.getMessage());
            if (ioe.getCause() instanceof CardException) { 
                throw (CardException) ioe.getCause();
            } 
            
        }
        return data;
    }
    
    /**
     * Writes a Mifare Classic 1K block.
     * @param reader the reader
     * @param access the access
     * @param block the block to be written
     * @return true if the block has been written, false otherwise
     */
    private static boolean writeMifareClassic1KBlock(MfReaderWriter reader, MfAccess access, MfBlock block) throws CardException {
        boolean written = false;
        try {
            reader.writeBlock(access, block);
            written = true;
        } catch (IOException ioe) {
            if (ioe.getCause() instanceof CardException) {
                throw (CardException) ioe.getCause();
            }
        }
        return written;
    }
    
    /**
     * Dumps Mifare Classic 1K block data.
     * @param reader the reader
     * @param card the card
     * @param sectorId the sector to be read
     * @param blockId the block to be read
     * @param keys the keys to be tested for reading
     */ 
    private static void dumpMifareClassic1KBlock(MfReaderWriter reader, MfCard card, int sectorId, int blockId, List<String> keysNOUSO) throws CardException {
        System.out.printf("Sector %02d block %02d: ", sectorId, blockId);
         
        for (String key : keysMap) {
            // For each provided key...
        	//key = "AABBCCDDEEFF";
            if (isValidMifareClassic1KKey(key)) {
                byte[] keyBytes = hexStringToBytes(key); 
                // Reading with key A
                MfAccess access = new MfAccess(card, sectorId, blockId, Key.A, keyBytes); 
                String blockData = readMifareClassic1KBlock(reader, access);
                if (blockData == null) {
                    // Reading with key B
                    access = new MfAccess(card, sectorId, blockId, Key.B, keyBytes);
                    blockData = readMifareClassic1KBlock(reader, access);
                }
                if (blockData != null) {
                    // Block read
                    System.out.println(blockData + " (Key " + access.getKey() + ": " + key + ")");
                    keysMap.set(0, key);	// MOVEMOS EL KEY AL PRINCIPIO PARA EL SIGUIENTE ESCANEO 
                    return;
                }
            }
        } 
        // All keys tested, failed to read block
        System.out.println("<Failed to read block>");
    }
}