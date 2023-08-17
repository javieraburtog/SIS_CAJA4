package com.casapellas.rpg;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sFecha = new SimpleDateFormat("MMyyyy").format(new Date());
		System.out.println("Mes: "+Integer.parseInt(sFecha));
		
		
		int uno = 1082012;
		int dos = 31082012;
		int tres= 3052012;
		
		System.out.println(tres +" entre  "+uno +" y "+ dos);
		System.out.println(  tres >= uno && tres <= dos  );
		
		
		

	}

}
