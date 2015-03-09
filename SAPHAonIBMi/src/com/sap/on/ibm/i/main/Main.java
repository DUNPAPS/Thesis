package com.sap.on.ibm.i.main;

import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import com.sap.on.ibm.i.controller.CommandLineController;
import com.sap.on.ibm.i.controller.GUIController;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.io.ScriptReader;

/**
 * @author Duncan
 *
 */
public class Main {

	private static File inFile = null;
	private static Logging logger = Logging.getInstance();

	public static void main(String[] args) {

		String mode = args[0];
		if (mode.trim().length() == 0) {
			new IllegalArgumentException(
					"Incorrect number of command line arguments: "
							+ args.length);
			System.exit(8);
		}

		else if (mode.equals("-script")) {
			CommandLineController controller = new CommandLineController();
			logger.setLogger("");
			controller.setLogger(logger);
			controller.run();

			if (0 < args.length) {
				inFile = new File(args[1]);
				try {
					ScriptReader reader = new ScriptReader(inFile);
					reader.read();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else if (mode.equals("-gui")) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					GUIController controller = new GUIController();
					logger.setLogger("");
					controller.setLogger(logger);
					controller.showMainView();
				}
			});
		}
	}
}