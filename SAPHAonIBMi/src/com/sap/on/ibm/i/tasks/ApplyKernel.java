package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sap.on.ibm.i.editor.controller.Controller;
import com.sap.on.ibm.i.logger.Levels;

public class ApplyKernel {
	protected String PLINK_EXE = "plink.exe";
	protected Controller controller;
	protected Map<String, String> myMap = new ConcurrentHashMap<String, String>();

	public ApplyKernel(Controller controller) {
		this.controller = controller;
	}

	public void setCommand(String commandName, String command) {
		myMap.put(commandName, command);
	}

	public static Object getKeyFromValue(Map hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o.toString();
			}
		}
		return null;
	}
	
	public void exe() {
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				String line;
				Iterator<String> commands = myMap.keySet().iterator();

				while (commands.hasNext()) {
					controller.get_outputTestEditor().getjProgressBar()
							.setIndeterminate(true);
					controller.get_outputTestEditor().getjProgressBar()
							.setBorderPainted(true);
					controller.get_outputTestEditor().getjProgressBar()
							.repaint();
					String command = commands.next();
					String nextCommand = myMap.get(command);
					try {

						PLINK_EXE += " " + controller.get_user().getUserData();
						PLINK_EXE += " " + nextCommand;
						controller.logMessages(Levels.INFO, "Command:    "
								+ PLINK_EXE, null);
						controller.logMessages(Levels.INFO,
								"Executig command...", null);

						Process p = Runtime.getRuntime().exec(PLINK_EXE);
						BufferedReader stdInput = new BufferedReader(
								new InputStreamReader(p.getInputStream()));
						
						//error
						BufferedReader stdError = new BufferedReader(
								new InputStreamReader(p.getErrorStream()));
						while ((line = stdInput.readLine()) != null) {

							if (!line.equals("") || line.contains("INFO")) {
								controller.logMessages(Levels.INFO, line, null);
							}
							if (!line.equals("") && line.contains("FAIL")) {
								controller.logMessages(Levels.ERROR, null,
										new Exception(line));
							} else {
								controller.logMessages(Levels.INFO, line, null);
							}
						}
						while ((line = stdError.readLine()) != null) {

							if (!line.equals("")) {
								controller.logMessages(Levels.ERROR, null,
										new Exception(line));
							}
						}
						p.waitFor();
						
					} catch (Exception e) {
						try {
							controller.logMessages(Levels.ERROR, null,
									new Exception(e));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					try {
						controller.logMessages(Levels.INFO, " "
								+ " Finished ...", null);
						controller.get_outputTestEditor().getjProgressBar()
						.setIndeterminate(false);
						ActionEvent e = new TaskDoneEvent(this,1234,"ApplyKernelDONE");
						controller.sendDoneEvent(e);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		},"Apply Kernel .....");
		t2.start();
	}
	
}
/*
 * // --------------------- // Backup Orignal Kernel // --------------------- cd
 * /FSIASP/sapmnt/DCN/exe/uc; rm -R as400_pase_64.backup cp -R as400_pase_64
 * as400_pase_64.backup
 * 
 * 
 * // --------------------------------------------------------- // Get LAST
 * NIGHT SAP Kernel from Productive Build Machines //
 * --------------------------------------------------------- cd
 * /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64 cp
 * /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXEDB_DB4.SAR . cp
 * /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXE.SAR .
 * 
 * // ---------------------------- // Extract Kernel to SAP System //
 * ----------------------------
 * 
 * /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPCAR -x -f SAPEXEDB_DB4.SAR
 * /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPCAR -x -f SAPEXE.SAR
 * 
 * // ---------------------------------------------------- // ToDo: Update SAP
 * Host Agent with latest Kernel Parts //
 * ----------------------------------------------------
 * 
 * CONFIG FILE:
 * 
 * KernelBuildPath=/bas/742_COR/gen/dbgU/as400_pase_64/_out
 * SAPExePath=/FSIASP/sapmnt/DCN/exe/uc
 * 
 * 
 * For examnple:
 * 
 * // --------------------- // Backup Orignal Kernel // ---------------------
 * plink ... "cd /FSIASP/sapmnt/DCN/exe/uc; rm -R as400_pase_64.backup" plink
 * ... "cd /FSIASP/sapmnt/DCN/exe/uc; cp -R as400_pase_64 as400_pase_64.backup"
 * // --------------------------------------------------------- // Get LAST
 * NIGHT SAP Kernel from Productive Build Machines //
 * --------------------------------------------------------- plink ...
 * "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXEDB_DB4.SAR ."
 * plink ...
 * "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXE.SAR  ."
 * // --------------------------------------------------------- // Get LAST
 * NIGHT SAP Kernel from Productive Build Machines //
 * ---------------------------------------------------------
 * 
 * . "
 */

