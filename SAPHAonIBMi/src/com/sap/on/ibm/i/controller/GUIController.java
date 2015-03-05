package com.sap.on.ibm.i.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.sap.on.ibm.i.logger.Appender;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ApplyKernel;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;
import com.sap.on.ibm.i.tasks.TaskDoneEvent;
import com.sap.on.ibm.i.view.HATestEditor;

/**
 * @author Duncan
 *
 */
public class GUIController implements ActionListener, ItemListener,
		PropertyChangeListener, IController {
	private HATestEditor outputTestEditor;
	private Levels levels;
	private boolean stopSAPCheckBox;
	private boolean applyKernelCheckBox;
	private boolean allChecked;
	private Logging logger;
	private ScriptViewController scriptViewController;
	private PropertyChangeSupport changeSupport;
	private String name;
	private String oldValue;
	private String newValue;

	public GUIController() {
		changeSupport = new PropertyChangeSupport(this);
		changeSupport.addPropertyChangeListener(this);
		this.outputTestEditor = new HATestEditor();
		this.addListener();
	}

	public void showMainView() {
		PatternLayout layout = new PatternLayout("%m%n");
		Appender appender = new Appender(this);
		appender.setLayout(layout);
		Logger.getRootLogger().addAppender(appender);

		this.outputTestEditor.setVisible(true);

	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDoneEvent(ActionEvent e) {
		actionPerformed(e);
	}

	private void stopSAP() {
		ExecuteSAPControl sapControl = new ExecuteSAPControl();
		sapControl.setOutputTestEditor(outputTestEditor);
		sapControl.setLogger(getLogger());
		sapControl.setFunction("GetProcessList");
		sapControl.setInstance("00");
		sapControl.setHost("as0013");
		try {
			sapControl.execute();
			sapControl.addPropertyChangeListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void applyKernel() {

		ApplyKernel applylKernel = new ApplyKernel();
		applylKernel.setLogger(getLogger());
		applylKernel.setOutputTestEditor(outputTestEditor);
		applylKernel.setCommand("STEP0", "cd /FSIASP/sapmnt/DCN/exe/uc");
		// applylKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; rm -R as400_pase_64.backup");
		// applylKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; cp -R as400_pase_64 as400_pase_64.backup");
		try {
			applylKernel.execute();
			changeSupport.firePropertyChange("Task", "old", "ApplylKernel");
			applylKernel.addPropertyChangeListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == outputTestEditor.getImportScriptJMenuItem()) {

				getScriptViewController().showScriptView();

			}
			if (e.getSource() == outputTestEditor.getPlayButton()
					|| e instanceof TaskDoneEvent) {
				outputTestEditor.getjProgressBar().setEnabled(true);
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
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof GUIController) {
			this.name = evt.getPropertyName();
			this.oldValue = (String) evt.getOldValue();
			this.newValue = this.name + " " + evt.getNewValue();
		}
		if (evt.getSource() instanceof SwingWorker) {
			progress(evt, this.newValue);
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void progress(PropertyChangeEvent evt, String Taskname) {

		SwingWorker worker = (SwingWorker) evt.getSource();
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			outputTestEditor.getjProgressBar().setString(
					Taskname + " " + progress + "%");
			outputTestEditor.getStatusBarJLabel().setText(
					"In " + evt.getPropertyName().toString() + " ....");
			outputTestEditor.getjProgressBar().setValue(+progress);
			outputTestEditor.getjProgressBar().setStringPainted(true);
			outputTestEditor.getPlayButton().setEnabled(false);
		} else if ("state".equalsIgnoreCase(evt.getPropertyName())) {
			if (worker.isDone()) {
				try {
					ActionEvent e = new TaskDoneEvent(this, 1234, worker.get()
							.toString());
					sendDoneEvent(e);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}
		}
	}

	// String propertyName = evt.getPropertyName();
	//
	// if (propertyName.equals("state")) {
	// // StateValue changed
	// SwingWorker.StateValue stateValue = (StateValue) evt.getNewValue();
	// if (stateValue.equals(SwingWorker.StateValue.STARTED)) {
	// _progressJDialog.setVisible(true);
	// } else if (stateValue.equals(SwingWorker.StateValue.DONE)) {
	// _progressJDialog.dispose();
	// }
	// } else if (propertyName.equals("progress")) {
	// // Progress change
	// if (_progressJDialog.isCancelled()) {
	// _swingWorker.cancel(true);
	// } else {
	// _progressJDialog.setProgress((Integer) evt.getNewValue());
	// }
	// }
	// }

	private void addListener() {
		outputTestEditor.setclearLogViewButtonActionListener(this);
		outputTestEditor.setcopyActionListener(this);
		outputTestEditor.setstop_SAP_CheckboxActionListener(this);
		outputTestEditor.setplayButtonActionListener(this);
		outputTestEditor.setstopButtonActionListener(this);
		outputTestEditor.setexitJMenuItemActionListener(this);
		outputTestEditor.setinforJMenuItemActionListener(this);
		outputTestEditor.setImportScriptJMenuItemActionListener(this);
	}

	public HATestEditor getOutputTestEditor() {
		return outputTestEditor;
	}

	public void setLogger(Logging logger) {
		this.logger = logger;
	}

	public Logging getLogger() {
		return logger;
	}

	public Levels getLevels() {
		return levels;
	}

	public void setLevels(Levels levels) {
		this.levels = levels;
	}

	public ScriptViewController getScriptViewController() {
		if (this.scriptViewController != null) {
			return this.scriptViewController;
		} else {
			this.scriptViewController = new ScriptViewController(this);
			return this.scriptViewController;
		}
	}

}
