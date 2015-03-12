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
import com.sap.on.ibm.i.model.ScriptModel;
import com.sap.on.ibm.i.view.ProgressbarTimedUpdate;

public class ApplyKernel {
	protected String PLINK_EXE = "plink.exe";
	protected Map<String, String> myMap = new ConcurrentHashMap<String, String>();
	private Logging logger;
	@SuppressWarnings("unused")
	private ActionEvent event;
	private IController myController;
	private int MAX_WAIT_TIME_SEC = 30;
	private Process process;
	private ProgressbarTimedUpdate progressbar;
	private ScriptModel scriptModel;
	private Integer exitCode;

	public ApplyKernel(IController myController) {
		this.myController = myController;
		progressbar = new ProgressbarTimedUpdate(this.myController);
	}

	public void setCommand(String commandName, String command) {
		myMap.put(commandName, command);
	}

	public void setLogger(Logging logger) {
		this.logger = logger;
	}

	public void setScriptModel(ScriptModel scriptModel) {
		this.scriptModel = scriptModel;

	}

	public Logging getLogger() {
		return logger;
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

			private BufferedReader stdError;
			private BufferedReader stdInput;

			public void run() {

				String line;
				Iterator<String> commands = myMap.keySet().iterator();

				try {
					while (commands.hasNext()) {

						String command = commands.next();
						String nextCommand = myMap.get(command);
						scriptModel.setUser("qsecofr@as0013");
						scriptModel.setPassword("bigboss");
						PLINK_EXE += " " + scriptModel.getUserData();
						PLINK_EXE += " " + nextCommand;

						System.out.println("COMMAND: " + PLINK_EXE);
						System.out.println(" ");
						getLogger().logMessages(Levels.INFO,
								"Executig COMMAND", null);

						progressbar.start(MAX_WAIT_TIME_SEC);

						System.out.println(" ");

						process = Runtime.getRuntime().exec(PLINK_EXE);
						stdInput = new BufferedReader(new InputStreamReader(
								process.getInputStream()));

						// error
						stdError = new BufferedReader(new InputStreamReader(
								process.getErrorStream()));

						while ((line = stdInput.readLine()) != null) {

							if (!line.equals("") || line.contains("INFO")) {
								getLogger()
										.logMessages(Levels.INFO, line, null);
							} else if (!line.equals("")
									&& line.contains("FAIL")) {
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
						process.waitFor();
						exitCode = process.exitValue();

						if (exitCode == 0) {
							getLogger()
									.logMessages(
											Levels.INFO,
											" "
													+ " EXITCODE: "
													+ exitCode
													+ " Last webmethod call successful",
											null);
							getLogger().logMessages(Levels.INFO,
									" " + " Finished", null);
							ActionEvent e = new TaskEvent(this, 0,
									"SAP CONTROL DONE");
							myController.sendDoneEvent(e);
						}
						if (exitCode == 1) {
							throw new RuntimeException(
									" EXITCODE: "
											+ exitCode
											+ "Last webmethod call failed, invalid parameter.");
						}
						if (exitCode == 2) {
							throw new RuntimeException(
									" EXITCODE: "
											+ exitCode
											+ "StartWait, StopWait, WaitforStarted, WaitforStopped, RestartServiceWait timed out.");
						}

						if (exitCode == 3) {
							getLogger()
									.logMessages(
											Levels.INFO,
											" "
													+ " EXITCODE: "
													+ exitCode
													+ " GetProcessList succeeded, all processes running correctly",
											null);
							ActionEvent e = new TaskEvent(this, 0,
									"SAP CONTROL DONE");
							myController.sendDoneEvent(e);
						}

						if (exitCode == 4) {
							getLogger()
									.logMessages(
											Levels.INFO,
											" "
													+ " EXITCODE: "
													+ exitCode
													+ " GetProcessList succeeded, all processes stopped",
											null);
							ActionEvent e = new TaskEvent(this, 0,
									"SAP CONTROL DONE");
							myController.sendDoneEvent(e);
						}
					}
				} catch (Exception e) {
					try {
						getLogger().logMessages(Levels.ERROR, null,
								new Exception(e));
						process.destroy();
						stdError.close();
						stdInput.close();
						System.out.println("Exit Value: " + exitCode);
						System.exit(1);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		}, "Execute Apply Kernel");
		t2.start();
		myController.setThreadName(t2.getName());
	}

	public void stopProcess() {
		if (process != null) {
			process.destroy();
			progressbar.Stop();
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

