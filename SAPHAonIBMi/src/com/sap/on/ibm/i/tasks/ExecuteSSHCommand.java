package com.sap.on.ibm.i.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sap.on.ibm.i.logger.Logging;

public class ExecuteSSHCommand {
	private String user;
	private String password;
	private Logging logging;
	public ExecuteSSHCommand(){
		logging=Logging.getInstance();
		logging.setLogger(ExecuteSSHCommand.class.getName());
		logging.getLogger().info("---------------------------------------------------------------------------");
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
			logging.getLogger().info("Start plink_exe command");
			logging.getLogger().info("Command: " + "  "
					+ plink_exe);
			 
			Process process = Runtime.getRuntime().exec(plink_exe + "qsecofr@as0013 ls /usr/sap/DCN/// SYS/exe/run");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				logging.getLogger().info( line);
			}
			logging.getLogger().info("End plink_exe");
		} catch (IOException e) {
			// logger.log(Level.ERROR, e.getStackTrace().toString());
		}
		
	

	}

}
