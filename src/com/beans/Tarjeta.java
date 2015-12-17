package com.beans;

public class Tarjeta 
{ 
	public int Accion								= 0; 
	public float Saldo 								= 0f; 
	public int CodUsuarioTarjeta 					= 0;
	public int DniUsuarioTarjeta 					= 0;
	public int TipoUsuarioTarjeta 					= 0;
	public String FechaVencimientoUsuarioTarjeta 	= "";
	 
	public int getAccion() {
		return Accion;
	}
	public void setAccion(int accion) {
		Accion = accion;
	}
	public float getSaldo() {
		return Saldo;
	}
	public void setSaldo(float saldo) {
		Saldo = saldo;
	}
	public int getCodUsuarioTarjeta() {
		return CodUsuarioTarjeta;
	}
	public void setCodUsuarioTarjeta(int codUsuarioTarjeta) {
		CodUsuarioTarjeta = codUsuarioTarjeta;
	}
	public int getDniUsuarioTarjeta() {
		return DniUsuarioTarjeta;
	}
	public void setDniUsuarioTarjeta(int dniUsuarioTarjeta) {
		DniUsuarioTarjeta = dniUsuarioTarjeta;
	}
	public int getTipoUsuarioTarjeta() {
		return TipoUsuarioTarjeta;
	}
	public void setTipoUsuarioTarjeta(int tipoUsuarioTarjeta) {
		TipoUsuarioTarjeta = tipoUsuarioTarjeta;
	}
	public String getFechaVencimientoUsuarioTarjeta() {
		return FechaVencimientoUsuarioTarjeta;
	}
	public void setFechaVencimientoUsuarioTarjeta(
			String fechaVencimientoUsuarioTarjeta) {
		FechaVencimientoUsuarioTarjeta = fechaVencimientoUsuarioTarjeta;
	}
	
	/**
	 * Simple conversor de String fecha a int decimal 
	 **/
	public int getFechaVencimientoUsuarioTarjetaHex() 
	{
		int fechaDecimal = 0;
		try 
		{
			if (getFechaVencimientoUsuarioTarjeta().length() > 0) 
				fechaDecimal = Integer.valueOf(getFechaVencimientoUsuarioTarjeta().replace("/", "")); 
		} catch (Exception e) {} 
		return fechaDecimal;
	}
	
	
	
	
	
}
