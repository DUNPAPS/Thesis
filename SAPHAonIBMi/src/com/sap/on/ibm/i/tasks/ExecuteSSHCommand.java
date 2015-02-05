package com.sap.on.ibm.i.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import com.sap.on.ibm.i.logger.Log;

public class ExecuteSSHCommand {
	private String user;
	private String password;
	private Log logger = new Log();
	
	//Constructor
	public ExecuteSSHCommand(){
		logger.setLogger(ExecuteSSHCommand.class.getName());
		logger.log(Level.INFO,"---------------------------------------------------------------------------");
	}
	
	public void setUser(String user) {
		this.user = user;//qsecofr

	}

	public void setPassword(String password) {
		this.password = password; //bigboss
	}

	public void setCommand(String command) {
	}

	
	public void execute() {


			String plink_exe = "plink.exe";
			
			if (user != null) {
				plink_exe += " -l " +user;
			}
			
			if (password != null) {
				plink_exe += " -pw " + password;
			}
			
			
		try {
			logger.log(Level.INFO, "Start plink_exe command");
			logger.log(Level.INFO, "Command: " + "  "
					+ plink_exe);
			 
			Process process = Runtime.getRuntime().exec(plink_exe + "qsecofr@as0013 ls /usr/sap/DCN/// SYS/exe/run");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				logger.log(Level.INFO, line);
			}
			logger.log(Level.INFO, "End plink_exe");
		} catch (IOException e) {
//			logger.log(Level.ERROR, e.getStackTrace().toString());
		}
		
	

	}

}
