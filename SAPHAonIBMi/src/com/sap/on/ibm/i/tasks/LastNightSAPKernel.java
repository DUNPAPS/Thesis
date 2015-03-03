package com.sap.on.ibm.i.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.sap.on.ibm.i.editor.controller.GUIScriptController;
import com.sap.on.ibm.i.logger.Levels;

public class LastNightSAPKernel extends ApplyKernel implements ICommandRunner {
	private String line;

	public LastNightSAPKernel(GUIScriptController controller) {
		super(controller);
	}

	@Override
	public void setCommand(String commandName, String command) {

		super.setCommand(commandName, command);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exe() {
		Thread t = new Thread(new Runnable() {
			public void run() {

				Iterator<String> commands = myMap.keySet().iterator();

				while (commands.hasNext()) {
					controller.getOutputTestEditor().getjProgressBar()
							.setIndeterminate(true);
					controller.getOutputTestEditor().getjProgressBar()
							.setBorderPainted(true);
					controller.getOutputTestEditor().getjProgressBar()
							.repaint();
					String command = commands.next();
					String nextCommand = myMap.get(command);
					try {

						PLINK_EXE += " " + controller.get_user().getUserData();
						PLINK_EXE += " " + nextCommand;
						controller.getLogger().logMessages(Levels.INFO,
								"Command:    " + PLINK_EXE, null);
						controller.getLogger().logMessages(Levels.INFO,
								"Executig command...", null);

						Process p = Runtime.getRuntime().exec(PLINK_EXE);
						BufferedReader stdInput = new BufferedReader(
								new InputStreamReader(p.getInputStream()));
						BufferedReader stdError = new BufferedReader(
								new InputStreamReader(p.getErrorStream()));
						while ((line = stdInput.readLine()) != null) {

							if (!line.equals("") || line.contains("INFO")) {
								controller.getLogger().logMessages(Levels.INFO,
										line, null);
							}
							if (!line.equals("") && line.contains("FAIL")) {
								controller.getLogger()
										.logMessages(Levels.ERROR, null,
												new Exception(line));
							} else {
								controller.getLogger().logMessages(Levels.INFO,
										line, null);
							}
						}
						while ((line = stdError.readLine()) != null) {

							if (!line.equals("")) {
								controller.getLogger()
										.logMessages(Levels.ERROR, null,
												new Exception(line));
							}
						}
						p.waitFor();
					} catch (Exception e) {
						try {
							controller.getLogger().logMessages(Levels.ERROR,
									null, new Exception(line));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					try {
						controller.getLogger().logMessages(Levels.INFO,
								" " + " Finished ...", null);
						controller.getOutputTestEditor().getjProgressBar()
								.setIndeterminate(false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}, "GET LAST NIGHT SAP Kernel .....");
		t.start();

	}

}
