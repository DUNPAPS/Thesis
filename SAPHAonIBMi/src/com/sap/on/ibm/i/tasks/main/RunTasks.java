package com.sap.on.ibm.i.tasks.main;

import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;

public class RunTasks {
	private static Logging logging = Logging.getInstance();

	public static void main(String[] args) {
		logging.setLogger(RunTasks.class.getName());
		logging.getLogger().info("---------------------------------------------------------------------------");
		logging.getLogger().info("                          Main                                             ");
		logging.getLogger().info("---------------------------------------------------------------------------");

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
			} else {
				Throwable t = new IllegalArgumentException(
						"Incorrect number of command line arguments: "
								+ args.length);
				logging.getLogger().fatal(t.getLocalizedMessage());
				System.exit(8);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// GetProcessList 00 as0013
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
