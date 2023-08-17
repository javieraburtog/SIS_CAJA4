package com.casapellas.reportes;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class francisco {

	public francisco() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		hola();
	}
 

	public static void hola() {

		try {

			Document document = new Document(PageSize.LETTER);
			
			File file = new File("c:/francisco.pdf");
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.setMargins(10, 10, 50,5);
			
			document.open();
			
			
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
			
			FontFactory.register("c:/windows/fonts/arial.ttf") ;
			Font f = FontFactory.getFont("Arial", 12, Font.NORMAL) ;
			
			String hola = "yo, EMILIA EUGENIA NAVARRO TERAN, mayor de edad, casada, ingeniera industrial, Nicaragüense, del domicilio de mangua, departamento de Managua" ;
			
			paragraph.add(new Chunk(hola, f ));
			document.add( paragraph );
			
			
			document.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
