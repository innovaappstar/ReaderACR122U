package com.innova;

import java.util.Scanner;


public class Main {
	
	public static void main(String[] args)
	{
		System.out.println("Inputs-Calculadora");
		
		Scanner scan = new Scanner(System.in);  
		int n1, n2, r;
		
		/**Calculadora Simple**/
		System.out.println("Valor n1:");
		n1 = scan.nextInt();  
		System.out.println("-------------------");
		
		for (int i = 1; i < 13; i++) 
		{
			r = n1 * i;
			System.out.println(n1 + " * " + i + " = " + r);
		}
	}
}
