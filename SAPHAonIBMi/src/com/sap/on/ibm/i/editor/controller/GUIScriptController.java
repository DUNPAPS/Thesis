package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Appender;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ApplyKernel;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;
import com.sap.on.ibm.i.tasks.TaskDoneEvent;

/**
 * @author Duncan
 *
 */
public class GUIScriptController implements ActionListener, ItemListener,
		PropertyChangeListener, IScriptController {
	private OutputTestEditor outputTestEditor;
	private User _user;
	private Levels levels;
	private boolean stopSAPCheckBox;
	private boolean applyKernelCheckBox;
	private boolean allChecked;
	private Logging logger;
	private ScriptViewController scriptViewController;

	public GUIScriptController() {
		this.outputTestEditor = new OutputTestEditor();
		this._user = new User();
		this._user.setUser("qsecofr@as0013");
		this._user.setPassword("bigboss");
		this.addListener();
	}

	public void showMainView() {
		PatternLayout layout = new PatternLayout("%m%n");
		Appender appender = new Appender(this);
		appender.setLayout(layout);
		Logger.getRootLogger().addAppender(appender);

		this.outputTestEditor.setVisible(true);

	}

	public void stopSAP() {
		ExecuteSAPControl sapControl = new ExecuteSAPControl(this);
		sapControl.setFunction("GetProcessList");
		sapControl.setInstance("00");
		sapControl.setHost("as0013");
		sapControl.addPropertyChangeListener(this);
		try {
			getLogger().logMessages(Levels.INFO,
					"Running Apply Kernel command...", null);
			sapControl.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void applyKernel() {

		ApplyKernel applylKernel = new ApplyKernel(this);
		applylKernel.setCommand("STEP0", "cd /FSIASP/sapmnt/DCN/exe/uc");
		// applylKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; rm -R as400_pase_64.backup");
		// applylKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; cp -R as400_pase_64 as400_pase_64.backup");
		applylKernel.addPropertyChangeListener(this);
		applylKernel.execute();

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

	public void addListener() {
		outputTestEditor.setclearLogViewButtonActionListener(this);
		outputTestEditor.setcopyActionListener(this);
		outputTestEditor.setstop_SAP_CheckboxActionListener(this);
		outputTestEditor.setplayButtonActionListener(this);
		outputTestEditor.setstopButtonActionListener(this);
		outputTestEditor.setexitJMenuItemActionListener(this);
		outputTestEditor.setinforJMenuItemActionListener(this);
		outputTestEditor.setImportScriptJMenuItemActionListener(this);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		progress(evt);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == outputTestEditor.getImportScriptJMenuItem()) {

				getScriptViewController().showScriptView();

			}
			if (e.getSource() == outputTestEditor.getPlayButton()
					|| e instanceof TaskDoneEvent) {

				String EventName = e.getActionCommand();
				getLogger().logMessages(Levels.INFO, EventName, null);
				outputTestEditor.getPlayButton().setEnabled(false);
				String user = outputTestEditor.getUSER();
				String password = outputTestEditor.getPassword();
				stopSAPCheckBox = outputTestEditor.getStop_SAP_Checkbox()
						.isSelected();
				applyKernelCheckBox = outputTestEditor.getApplyKernelCheckbox()
						.isSelected();

				if (user.equals("") || password.equals("")
						|| !user.equals("bigboss")
						|| !password.equals("qsecofer")) {
					allChecked = true;
					stopSAPCheckBox = false;
					applyKernelCheckBox = false;
					JOptionPane.showMessageDialog(null,
							"Invalid username and password", "Try again",
							JOptionPane.ERROR_MESSAGE);
					outputTestEditor.getSap_SID_Field().setText("");
					outputTestEditor.getSap_USER_Field().setText("");
					outputTestEditor.getSap_USER_Field().setText("");
				}

				if (stopSAPCheckBox) {
					allChecked = true;
					stopSAP();
					outputTestEditor.getStop_SAP_Checkbox().setSelected(false);

				} else if (applyKernelCheckBox) {
					allChecked = true;
					applyKernel();
					outputTestEditor.getApplyKernelCheckbox()
							.setSelected(false);

				} else if (!allChecked) {
					JOptionPane.showMessageDialog(null,
							" Select a Task to run..", " Run Tasks ",
							JOptionPane.ERROR_MESSAGE);
					outputTestEditor.getPlayButton().setEnabled(true);
				} else {
					allChecked = false;

				}
				outputTestEditor.getjProgressBar().setString(" ");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void progress(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			outputTestEditor.getjProgressBar().setStringPainted(true);
			outputTestEditor.getjProgressBar().setValue(progress);
			outputTestEditor.getStatusBarJLabel().setText(
					"In " + evt.getPropertyName().toString() + " ....");
			outputTestEditor.getPlayButton().setEnabled(false);
		} else {
			outputTestEditor.getStatusBarJLabel().setText(
					"Task(s) " + evt.getNewValue().toString());
			outputTestEditor.getPlayButton().setEnabled(true);

		}

	}

	@Override
	public void sendDoneEvent(ActionEvent e) {
		actionPerformed(e);
	}

	public OutputTestEditor getOutputTestEditor() {
		return outputTestEditor;
	}

	public void setLogger(Logging logger) {
		this.logger = logger;
	}

	public Logging getLogger() {
		return logger;
	}

	public User get_user() {
		return _user;
	}

	public Levels getLevels() {
		return levels;
	}

	public void setLevels(Levels levels) {
		this.levels = levels;
	}

	public ScriptViewController getScriptViewController() {
		if (this.scriptViewController == null) {
			this.scriptViewController = new ScriptViewController(this);
			return this.scriptViewController;
		} else {
			return this.scriptViewController;
		}
	}

}
