package com.sap.on.ibm.i.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.SwingUtilities;

import com.sap.on.ibm.i.editor.controller.CommandLineScriptController;
import com.sap.on.ibm.i.editor.controller.GUIScriptController;
import com.sap.on.ibm.i.logger.Logging;

;

/**
 * @author Duncan
 *
 */
public class Main {

	private static BufferedReader br = null;
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
			CommandLineScriptController controller = new CommandLineScriptController();
			logger.setLogger(controller.getClass().getName());
			controller.setLogger(logger);
			
			try {

				if (0 < args.length) {
					inFile = new File(args[1]);
				}

				String sCurrentLine;

				br = new BufferedReader(new FileReader(inFile));

				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} else if (mode.equals("-gui")) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					GUIScriptController controller = new GUIScriptController();
					logger.setLogger(controller.getClass().getName());
					controller.setLogger(logger);
					controller.showMainView();
				}
			});
		}
	}
}