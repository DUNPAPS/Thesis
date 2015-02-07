package com.sap.on.ibm.i.tasks.main;

import com.sap.on.ibm.i.tasks.ExecuteSAPControl;

public class RunTasks {

	public static void main(String[] args) {

		try {
			ExecuteSAPControl sapControl = new ExecuteSAPControl();

			if (args.length > 2) {
				for (String arg : args) {
					if (arg.equals("GetProcessList")) {
						sapControl.setFunction("GetProcessList");
					}
					if (arg.equals("00")) {
						sapControl.setInstance("00");
					}
					if (arg.equals("as0013")) {
						sapControl.setHost("as0013");
					}
				}
				sapControl.execute();
			}
			else {
				System.err.println("Error in Programm call.");
				System.err.println("Parameter call missing: Instance host");
				System.err.println("Exit Main.");
				System.exit(8);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println(e.getMessage().toString());
		}

		// // ExecuteSSHCommand
		// ExecuteSSHCommand ssh = new ExecuteSSHCommand();
		// ssh.setCommand("GetProcessList");
		// ssh.setUser("qsecofr");
		// ssh.setPassword("bigboss");
		// ssh.execute();

	}
}
