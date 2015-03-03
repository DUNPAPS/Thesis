package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ApplyKernel;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;

public class CommandLineScriptController implements ActionListener,
		PropertyChangeListener, ItemListener, IScriptController {
	private OutputTestEditor _outputTestEditor;
	private User _user;

	private Logging logger;

	public CommandLineScriptController() {
		this._outputTestEditor = new OutputTestEditor();
		this._user = new User();
		this._user.setUser("qsecofr@as0013");
		this._user.setPassword("bigboss");

	}

	public void stopSAP() {
		ExecuteSAPControl sapControl = new ExecuteSAPControl();
		sapControl.setFunction("GetProcessList");
		sapControl.setInstance("00");
		sapControl.setHost("as0013");
		sapControl.addPropertyChangeListener(this);
		try {
			getLogger().logMessages(Levels.INFO,
					"Running Apply Kernel command...", null);
			sapControl.execute();
			sendDoneEvent(sapControl.getEvent());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void applyKernel() {

		ApplyKernel applylKernel = new ApplyKernel();
		applylKernel.setCommand("STEP0", "cd /FSIASP/sapmnt/DCN/exe/uc");
		// applylKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; rm -R as400_pase_64.backup");
		// applylKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; cp -R as400_pase_64 as400_pase_64.backup");
		applylKernel.addPropertyChangeListener(this);
		applylKernel.execute();
		sendDoneEvent(applylKernel.getEvent());
		// LastNightSAPKernel lastNightSAPKernel = new LastNightSAPKernel(this);
		// lastNightSAPKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXEDB_DB4.SAR .");
		// lastNightSAPKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXE.SAR  .");
		// lastNightSAPKernel.exe();
		// getKernelBuildl.setCommand("STEP2",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; ");
		// getKernelBuildl.setCommand("STEP3", "cd /FSIASP/sapmnt/DCN/exe/uc");
		// getKernelBuildl.exe();

		// LastNightSAPKernel lastNightSAPKernel = new LastNightSAPKernel(this);
		// lastNightSAPKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXEDB_DB4.SAR .");
		// lastNightSAPKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXE.SAR  .");
		// lastNightSAPKernel.exe();
	}

	public void setLogger(Logging logger) {
		this.logger = logger;
	}

	public Logging getLogger() {
		return logger;
	}

	@Override
	public void progress(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			_outputTestEditor.getjProgressBar().setStringPainted(true);
			_outputTestEditor.getjProgressBar().setValue(progress);
			_outputTestEditor.getStatusBarJLabel().setText(
					"In " + evt.getPropertyName().toString() + " ....");
			_outputTestEditor.getPlayButton().setEnabled(false);
		} else {
			_outputTestEditor.getStatusBarJLabel().setText(
					"Task(s) " + evt.getNewValue().toString());
			_outputTestEditor.getPlayButton().setEnabled(true);

		}

	}

	@Override
	public void sendDoneEvent(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		progress(evt);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
