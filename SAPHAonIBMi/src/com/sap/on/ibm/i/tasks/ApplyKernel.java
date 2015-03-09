package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sap.on.ibm.i.controller.IController;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.model.User;

public class ApplyKernel {
	protected String PLINK_EXE = "plink.exe";
	protected Map<String, String> myMap = new ConcurrentHashMap<String, String>();
	private Logging logger;
	@SuppressWarnings("unused")
	private ActionEvent event;
	private User user;
	private IController myController;
	private int MAX_WAIT_TIME_SEC = 30;

	public ApplyKernel(IController myController) {
		this.user = new User();
		this.user.setUser("qsecofr@as0013");
		this.user.setPassword("bigboss");
		this.myController = myController;
	}

	public void setCommand(String commandName, String command) {
		myMap.put(commandName, command);
	}

	public void setLogger(Logging logger) {
		this.logger = logger;
	}

	public Logging getLogger() {
		return logger;
	}

	public User getUser() {
		return user;
	}

	@SuppressWarnings("rawtypes")
	public static Object getKeyFromValue(Map hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o.toString();
			}
		}
		return null;
	}

	public void execute() {

		Thread t2 = new Thread(new Runnable() {

			public void run() {

				String line;
				ProgressbarTimedUpdate progressbar = new ProgressbarTimedUpdate(
						myController);
				Iterator<String> commands = myMap.keySet().iterator();

				try {
					while (commands.hasNext()) {

						String command = commands.next();
						String nextCommand = myMap.get(command);

						PLINK_EXE += " " + getUser().getUserData();
						PLINK_EXE += " " + nextCommand;
						getLogger().logMessages(Levels.INFO,
								"Command:    " + PLINK_EXE, null);
						getLogger().logMessages(Levels.INFO,
								"Executig command...", null);

						progressbar.start(MAX_WAIT_TIME_SEC);
						Process p = Runtime.getRuntime().exec(PLINK_EXE);
						BufferedReader stdInput = new BufferedReader(
								new InputStreamReader(p.getInputStream()));

						// error
						BufferedReader stdError = new BufferedReader(
								new InputStreamReader(p.getErrorStream()));

						while ((line = stdInput.readLine()) != null) {

							if (!line.equals("") || line.contains("INFO")) {
								getLogger()
										.logMessages(Levels.INFO, line, null);
							}
							if (!line.equals("") && line.contains("FAIL")) {
								getLogger().logMessages(Levels.ERROR, null,
										new Exception(line));
							} else {
								getLogger()
										.logMessages(Levels.INFO, line, null);
							}
						}
						while ((line = stdError.readLine()) != null) {

							if (!line.equals("")) {
								getLogger().logMessages(Levels.ERROR, null,
										new Exception(line));
							}
						}
						p.waitFor();

					}
				} catch (Exception e) {
					try {
						getLogger().logMessages(Levels.ERROR, null,
								new Exception(e));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				try {
					getLogger().logMessages(Levels.INFO, " " + " Finished ...",
							null);
					ActionEvent e = new TaskDoneEvent(this, 0,
							"ApplyKernelDONE");
					myController.sendDoneEvent(e);
					progressbar.Stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, "Execute Apply Kernel");
		t2.start();
		myController.setThreadName(t2.getName());
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

