package com.operaciones;

import com.abstracts.BaseFormato;
import com.beans.Tarjeta;
import com.constantes.Constantes;
import com.innova.MainPrincipal;

public class Operaciones extends BaseFormato
{   
	/**
	 * @param m float es el monto.
	 * @param k String es la key.
	 * @return String de 16 bytes.
	 */
	public String formatSaldo(float m)
	{
		Tarjeta tarjeta = new Tarjeta();
		tarjeta.setAccion(1);
		tarjeta.setSaldo(1110f);
		tarjeta.setCodUsuarioTarjeta(14);
		tarjeta.setDniUsuarioTarjeta(47602603);
		tarjeta.setTipoUsuarioTarjeta(11);
		tarjeta.setFechaVencimientoUsuarioTarjeta("12/12/2020");
		
		MainPrincipal.printLog("FORMATO SALDO\n" + super.generarCadenaTransaccion(tarjeta));
		tarjeta.setAccion(2);
		MainPrincipal.printLog("FORMATO TRANSACCIONES \n" + super.generarCadenaTransaccion(tarjeta));

		tarjeta.setAccion(3);
		MainPrincipal.printLog("FORMATO DETALLE 01 \n" + super.generarCadenaTransaccion(tarjeta));

		tarjeta.setAccion(4);
		MainPrincipal.printLog("FORMATO DETALLE 02 \n" + super.generarCadenaTransaccion(tarjeta));

		
		if (m > 999f) 
			return Constantes.VACIO;	// <<ERROR CODE 15 : FLOAT EXCEDIO DEL LÍMITE>> 
		return super.generarCadenaTransaccion(tarjeta);
	} 
	
	
}
