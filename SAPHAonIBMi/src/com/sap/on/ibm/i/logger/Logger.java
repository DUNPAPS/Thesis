/*
 * EKG Projekt.

 * Elektrokardiogramm,EKG,: Das Elektrokardiogramm ist eine Aufzeichnung der elektrischen Aktivität des Herzens. Das EKG ermöglicht vielfältige Aussagen zu Eigenschaften und Erkrankungen des Herzens.
 */
package com.sap.on.ibm.i.logger;

import java.io.FileWriter;


import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * Logger Klasse nach Singleton-Entwurfsmuster
 *
 * @author dmuasya,hkisakye
 * @version 27.06.2010
 */
 
public class Logger {

// einzige Instanz der Klasse Logger
 
/** Die instance. */
private static Logger instance = null;

// Datum Format
 
/** Die Konstante DATUM_FORMAT_JETZT. */
private static final String DATUM_FORMAT_JETZT = "dd/MM/yyyy HH:mm:ss";

 
/** Die datum format. */
private SimpleDateFormat datumFormat = null;

 
/** Die Log file. */
private String LogFile = "LogDatei.txt";
     
    /**
	 * Instanziiert eine neue logger.
	 */
    private Logger() {
    // TO DO	
    }
 
    /**
     * Statische Methode, liefert die einzige Instanz dieser
     * Klasse zurück
     * @return instance
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    /**
     * Diese Methode liefert die aktuelle Zeit zurück.
	 * 
	 * 
     */
    private String aktuelleZeit()
    	{   		
    		if (datumFormat == null){
    		     datumFormat = new SimpleDateFormat(DATUM_FORMAT_JETZT);
    		}
    		
    	Calendar cal = Calendar.getInstance(); // Calendar Singleton Objekt
        return datumFormat.format(cal.getTime());
  	
    	}
    	
    /**
     *  Diese Methode schreibt in das Protokol Datei und danach das Datei schliessen
     * @param text von type String
     */   
     public void printBenutzeraktion(String text)
    	{  		
    	try
    		{   		
        // Erstellt bzw. Öffnet und schreibt am Ende eines bereit existierenden Datei 
           FileWriter fstream = new FileWriter(LogFile,true);
           BufferedWriter out = new BufferedWriter(fstream);
           
           String zeit = aktuelleZeit(); // gibt die zeit zurück
           zeit = zeit + "::"; // Trenn-String zwischen Zeit und Protokol
           out.write(zeit+text);
           out.newLine(); // geht zur nächsten Zeile
           out.close();   // File schliessen
           }
           catch (Exception e){
               System.err.println("Error: " + e.getMessage());
    		
    	}
    }
        
       /**
        *  Diese Methode schreibt in das Protokol Datei und danach das Datei schliessen
        *
        *  @param fehler
        *            zu Schreibende Exception 
        */ 
        public void printException(Exception fehler)
        {
        	
        	try
    		{   		
        // Erstellt bzw. Öffnet und schreibt am Ende eines bereit existierenden Datei 
           FileWriter fstream = new FileWriter(LogFile,true);
           BufferedWriter out = new BufferedWriter(fstream);
           
           String zeit = aktuelleZeit(); // gibt die zeit zurück
           zeit = zeit + "::"; // Trenn-String zwischen Zeit und Protokol
           out.write(zeit+"ExceptionTrace");
           out.newLine(); // geht zur nächsten Zeile 
           
           out.write(fehler.getMessage()); //letzte Fehler Meldung
           out.newLine(); //neue Zeile
           
       	   StackTraceElement[] elements = fehler.getStackTrace(); //gesamte StackTrace
           int noOfElements = elements.length;
       	   for (int i=0; i<noOfElements;i++)
       	     {
       	     out.write("@ "+ elements[i].toString());  //schreibt die StakTrace Error in LogDatei
       	     out.newLine();       	   
       	     }
       	   
       	   out.close();
          }
           catch (Exception e){
               System.err.println("Error: " + e.getMessage());
    		
    	}
     }  
        

}
