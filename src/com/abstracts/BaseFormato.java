package com.abstracts;

import com.beans.Tarjeta;
import com.constantes.Constantes;
import com.utils.ManagerUtils;

/**
 * Clase abstracta que contendra los atributos, detalles 
 * de todas las operaciones. 
 **/

public abstract class BaseFormato 
{ 
	//public static String STR_SALDO_FORMAT 		= "000000#000000#00"; //"0000000000000000"; 
    // Dependencia Utils...
    ManagerUtils managerUtils;
    // Referencia de rangos a grabar...
    int[] capacidad = new int[2];
    // Limitantes de transacción de tarjeta...
    int isBlockTransaccion01 	= 5; 	// PARA INFORMACION DE SALDO ACTUAL
    int isBlockTransaccion02	= 11;	// PARA BYTES DE INTEGRIDAD DE SALDO
    int isBlockTransaccion03	= 12;	// SÓLO PARA BYTES DE INTEGRIDAD DE HISTORIAL DE TRANSACCIONES
    // Limitantes de detalle de tarjeta...
    int isBlockDetalle01		= 7;
    int isBlockDetalle02		= 15;
    // Limitantes de capacidad para los bytes de integridad
    int isLimitBytesSaldo		= 0x05;
    int isLimitBytesTransaccion = 0x03; 
    
    
    // Limitante general..
    int isLimite				= 15; 
    
    
    
    StringBuilder str ;
    String result = "";

    /**
     * Simple función que se encargara de generar una cadena 
     * con un formato de acuerdo a lo que se solicita y 
     * valores como el : Saldo, Transacciones, Bytes de integridad.
     * @param f flotante que simbolizará al saldo.
     * @param acción int que ayudará a identificar que tipo de formato
     * utilizar .
     * <ul>
     * 		<li>	1 : FORMATO SALDO E HISTORIAL	</li>
     * 		<li>	2 : FORMATO DETALLE				</li>
     * </ul>  
     **/
    
	public String generarCadenaTransaccion(Tarjeta tarjeta)
	{
		managerUtils 	= new ManagerUtils(); 
		
		switch (tarjeta.getAccion()) 
		{
		case 1:	// RETORNA CADENA TRANSACCIÓN SALDO ACTUAL 
			if (tarjeta.getSaldo() > 999f) 
				break;	
			result			= ConvertToHex(tarjeta.getSaldo());
			capacidad[0] 	= isBlockTransaccion01 - result.length();
			capacidad[1]	= isBlockTransaccion01;
			
			str				= new StringBuilder(Constantes.STR_TRANSACCION_FORMAT);
			str				= getStringBuilder(capacidad, str.toString(), result); 
			// Inicio de valores de integridad... Se separa un valor como separador
			result			= getRandHex(isLimitBytesSaldo);	// Bytes de integridad... 
			capacidad[0] 	= isBlockTransaccion02 - result.length();
			capacidad[1]	= isBlockTransaccion02;
			
			if(result.length() == 0)
				break;
			
			str				= getStringBuilder(capacidad, str.toString(), result); 
			str.setLength(isLimite);
			return str.toString();  
		case 2:	// RETORNA CADENA DE TRANSACCIÓN HISTORIAL SALDO
			if (tarjeta.getSaldo() > 999f) 
				break;	
			result			= ConvertToHex(0f);	// El primer valor no se toca..
			capacidad[0] 	= isBlockTransaccion01 - result.length();
			capacidad[1]	= isBlockTransaccion01;
			
			str				= new StringBuilder(Constantes.STR_TRANSACCION_FORMAT);
			str				= getStringBuilder(capacidad, str.toString(), result); 
			
			// Segundo valor por default de saldo. 
			result			= ConvertToHex(tarjeta.getSaldo());	// El segundo valor será igual q el saldo
			capacidad[0] 	= isBlockTransaccion02 - result.length();
			capacidad[1]	= isBlockTransaccion02;
			//result			= managerUtils.decimalToHex(Integer.valueOf(result));	// Segundo saldo... 
			
			if(result.length() == 0)
				break; 
			str				= getStringBuilder(capacidad, str.toString(), result); 
			// Tercer valor Insertando bytes de integridad 
			result			= getRandHex(isLimitBytesTransaccion);			// Bytes de integridad... 
			capacidad[0] 	= isLimite - result.length();
			capacidad[1]	= isLimite;
			
			if(result.length() == 0)
				break; 
			
			str				= getStringBuilder(capacidad, str.toString(), result); 
			str.setLength(isLimite);
			return str.toString(); 
		case 3:	// RETORNA CADENA DE DETALLE DE TARJETA 01 - COD TARJETA DNI
			if (tarjeta.getCodUsuarioTarjeta() < 1 || String.valueOf(tarjeta.getDniUsuarioTarjeta()).length() != 8) 
				break;	// <<ERROR CODE 15 : FLOAT EXCEDIO DEL LÍMITE>>
			
			result 			= managerUtils.decimalToHex(tarjeta.getCodUsuarioTarjeta());
			 
			capacidad[0] 	= isBlockDetalle01 - result.length();
			capacidad[1]	= isBlockDetalle01; 
			str				= new StringBuilder(Constantes.STR_DETALLE_TARJETA_FORMAT);
			str				= getStringBuilder(capacidad, str.toString(), result); 
			
			// Segundo valor de dni.
			result 			= managerUtils.decimalToHex(tarjeta.getDniUsuarioTarjeta());
			capacidad[0] 	= isBlockDetalle02 - result.length();
			capacidad[1]	= isBlockDetalle02;
 
			if(result.length() == 0)
				break; 
			
			str				= getStringBuilder(capacidad, str.toString(), result);  
			str.setLength(isLimite);
			return str.toString(); 
		case 4:	// RETORNA CADENA DE DETALLE DE TARJETA 02 - TIPO TARJETA _ FECHA EXPIRACION
			if (tarjeta.getTipoUsuarioTarjeta() < 1 || tarjeta.getFechaVencimientoUsuarioTarjeta().length() != 10) 
				break;	// <<ERROR CODE 15 : VALOR MUY CORTO>>
			
			result 			= managerUtils.decimalToHex(tarjeta.getTipoUsuarioTarjeta());
			 
			capacidad[0] 	= isBlockDetalle01 - result.length();
			capacidad[1]	= isBlockDetalle01; 
			str				= new StringBuilder(Constantes.STR_DETALLE_TARJETA_FORMAT);
			str				= getStringBuilder(capacidad, str.toString(), result); 
			  

			// Operación de fechas 
			int fExpiracion = tarjeta.getFechaVencimientoUsuarioTarjetaHex();
			if(fExpiracion == 0)
				break;  
			result			= managerUtils.decimalToHex(fExpiracion);
			// Segundo valor FECHA DE EXPIRACION
			capacidad[0] 	= isBlockDetalle02 - result.length();
			capacidad[1]	= isBlockDetalle02;
			
			
			
			str				= getStringBuilder(capacidad, str.toString(), result);  
			str.setLength(isLimite);
			return str.toString(); 
		}
		return Constantes.VACIO;
	}
	
	/**
	 * Simple función que convertira un flotante en hex
	 * para los saldos.. 
	 **/
	public String ConvertToHex(float f)
	{
		String hex 	= managerUtils.floatToDecimal(f);
		hex			= managerUtils.decimalToHex(Integer.valueOf(hex));
		return hex;
	}
	
	
	
	 
	
	public String formatDetalleTarjetaL02(int tipoTarjeta, String fechaExpiracion)
	{
		if (tipoTarjeta < 1 || fechaExpiracion.length() != 10) 
			return Constantes.VACIO;	// <<ERROR CODE 15 : VALOR MUY CORTO>>
		
		// CONVERTIMOS DECIMAL A HEX 
		String tipTarjeta 			= Integer.toHexString(tipoTarjeta);
		fechaExpiracion = fechaExpiracion.replace("/", "");
		String fechaExpiracionTarjeta 	= Integer.toHexString(Integer.valueOf(fechaExpiracion));
		
		
		//printLog((Integer.decode("0x" + fechaExpiracionTarjeta)) + " << decimal convertido.."); 

		StringBuilder strb = new StringBuilder(Constantes.STR_DETALLE_TARJETA_FORMAT);
		int start 	= 7 - tipTarjeta.length();	// 1 	--> 0000001
		int end		= 7;
		String str	= tipTarjeta;
		 
		// BSALDO-01 SALDO
		strb.replace(start, end, str); 
		
		// BSALDO-02 BYTES DE INTEGRIDAD
		start 		= 16 - fechaExpiracionTarjeta.length();
		end			= 16;
		str			= fechaExpiracionTarjeta; 
		if (str.length() == 0)
		{
			return Constantes.VACIO;
		}
		
		strb.replace(start, end, str);
		
		strb.setLength(16);
		
		return strb.toString();
	}
	
	 
	/**
	 * Retorna un StringBuilder modificado con un formato personalizado..
	 * @param capacidades int[] que contendra los rangos
	 * @param dataPasada String que será modificado.
	 * @param dataNueva String que se utilizará para sustituir 
	 * @return {@link StringBuilder} valor con formato de trabajo.
	 **/
	public StringBuilder getStringBuilder(int[] capacidad, String dataPasada, String dataNueva)
	{
		return (new StringBuilder(dataPasada)).replace(capacidad[0], capacidad[1], dataNueva); 
	} 
	
	/**
	 * Genera un valor hexadecimal con capacidad 
	 * de 4 valores hexadecimales.
	 * @param capacidad int capacidad límite de la
	 * cadena hexadecimal.
	 * @return String Hexadecimal. 
	 **/
	public String getRandHex(int capacidad)
	{
		return managerUtils.randHex(2222222, 9999999, capacidad);
	}
	
 
	
	
	
}
