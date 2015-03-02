package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.SwingWorker;

import com.sap.on.ibm.i.editor.controller.Controller;
import com.sap.on.ibm.i.logger.Levels;

public class ApplyKernel extends SwingWorker<String, Integer> {
	protected String PLINK_EXE = "plink.exe";
	protected Controller controller;
	protected Map<String, String> myMap = new ConcurrentHashMap<String, String>();
	private int i = 0;
	private long DELAY = 200;

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

	@Override
	protected String doInBackground() throws Exception {
		String line;
		Iterator<String> commands = myMap.keySet().iterator();

		while (commands.hasNext()) {
			String command = commands.next();
			String nextCommand = myMap.get(command);
			try {

				PLINK_EXE += " " + controller.get_user().getUserData();
				PLINK_EXE += " " + nextCommand;
				controller.logMessages(Levels.INFO, "Command:    " + PLINK_EXE,
						null);
				controller
						.logMessages(Levels.INFO, "Executig command...", null);
				controller.get_outputTestEditor().getStatusBarJLabel().setText(" ");
				controller.get_outputTestEditor().getStatusBarJLabel().setText("Applying kernel .....");
				Process p = Runtime.getRuntime().exec(PLINK_EXE);
				BufferedReader stdInput = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

				// error
				BufferedReader stdError = new BufferedReader(
						new InputStreamReader(p.getErrorStream()));
				
				int progress = 0;

				while (!isCancelled() && progress < 50) {
					setProgress(++progress);
					Thread.sleep(DELAY);

				}
				
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
				 
				while (!isCancelled() && progress < 100) {
					setProgress(++progress);
					Thread.sleep(50);

				}
				

			} catch (Exception e) {
				try {
					controller
							.logMessages(Levels.ERROR, null, new Exception(e));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			try {
				controller
						.logMessages(Levels.INFO, " " + " Finished ...", null);
				ActionEvent e = new TaskDoneEvent(this, 1234, "ApplyKernelDONE");
				controller.sendDoneEvent(e);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return "Done Applying kernel";
	}

	@Override
	protected void process(List<Integer> chunks) {
		super.process(chunks);
	}

	@Override
	protected void done() {
		if (isCancelled()) {
			controller.get_outputTestEditor().getStatusBarJLabel().setText("Process canceled");
		} else {
 			controller.get_outputTestEditor().getStatusBarJLabel().setText("Apply Kernel Done");
		}
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

