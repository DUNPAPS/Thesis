package com.sap.on.ibm.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.sap.on.ibm.i.model.ScriptModel;

/**
 * Read a representation of the script model from the input stream
 *
 * @see DataInputStream
 */
public class ScriptReader {

	private BufferedReader in;

	/**
	 * @param compressedFile
	 *            the stream to read the script model from
	 * 
	 * @throws FileNotFoundException
	 *             if file doesn't exist
	 * @throws IOException
	 * 
	 * @see DataInputStream
	 */
	public ScriptReader(File compressedFile) throws FileNotFoundException,
			IOException {
		this.in = new BufferedReader(new FileReader(compressedFile));
	}

	
	/**
	 * Read a representation of the script model from the input stream
	 * 
	 * @return the script model as read from the compressed file
	 * 
	 * @throws ClassNotFoundException
	 *             if the compressed file is corrupt
	 * @throws IOException
	 * 
	 * @see scriptModel
	 */
	public ScriptModel read() throws ClassNotFoundException, IOException {
		ScriptModel scriptModel = new ScriptModel();
		String sCurrentLine;
		while ((sCurrentLine = in.readLine()) != null) {
			if (sCurrentLine.contains(":")) {
				String[] split = sCurrentLine.split(":");
				String firstSubString = split[0];
				String secondSubString = split[1];
				
				if (firstSubString.trim().equals("SAP_SID")) {
					scriptModel.setSid(secondSubString);
				}
				
				if (firstSubString.trim().equals("SAP_User")) {
					scriptModel.setSid(secondSubString);
				}
				
				if (firstSubString.trim().equals("SAP_Password")) {
					scriptModel.setPassword(secondSubString);
				}
				
				if (firstSubString.trim().equals("SAP_Global_Kernel")) {
					scriptModel.setGlobal_Kernel(secondSubString);
				}
				
				
				if (firstSubString.trim().equals("SAP_NightlyMake")) {
					scriptModel.setNightlyMake(secondSubString);
				}
				
				if (firstSubString.trim().equals("Step0")
						 ) {
					scriptModel.setStopSAP(secondSubString);
				}
					
				if (firstSubString.trim().equals("Step1")
						 ) {
					scriptModel.setApplyKernel(secondSubString);
				}
				
				if (firstSubString.trim().equals("Step2")
						 ) {
					scriptModel.setStartSAP(secondSubString);
				}
				
				if (firstSubString.trim().equals("Step3")
						 ) {
					scriptModel.setRunHATest(secondSubString);
				}
				
				}
			}
		return scriptModel;
		}
 
	/**
	 * end the stream
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.in.close();
	}
}
