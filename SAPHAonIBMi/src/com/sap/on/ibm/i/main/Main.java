package com.sap.on.ibm.i.main;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import com.sap.on.ibm.i.controller.CommandLineController;
import com.sap.on.ibm.i.controller.GUIController;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.model.ScriptModel;
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
			

			if (0 < args.length) {
				inFile = new File(args[1]);
				try {
					CommandLineController controller = new CommandLineController();
					logger.setLogger("");
					controller.setLogger(logger);
					ScriptReader reader = new ScriptReader(inFile);
					ScriptModel scriptModel = reader.read();
					controller.setScriptModel(scriptModel);
					controller.run();
				} catch (IOException e) {
					new IllegalArgumentException(
							"Incorrect number of command line arguments: "
									+ args.length);
				} catch (ClassNotFoundException e) {
					new IllegalArgumentException(
							"Incorrect number of command line arguments: "
									+ args.length);
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