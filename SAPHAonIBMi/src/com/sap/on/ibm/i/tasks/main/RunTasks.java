package com.sap.on.ibm.i.tasks.main;

import com.sap.on.ibm.i.editor.controller.Controller;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;

public class RunTasks {
	private static Logging logging = Logging.getInstance();
	private static Controller controller = new Controller();

	public static void main(String[] args) {
		logging.setLogger(RunTasks.class.getName());

		try {
			ExecuteSAPControl sapControl = new ExecuteSAPControl(controller);

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
				// sapControl.exe();
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

	}
}
