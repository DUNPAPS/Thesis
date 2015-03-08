package com.sap.on.ibm.i.controller;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.sap.on.ibm.i.logger.Appender;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.model.ScriptModel;
import com.sap.on.ibm.i.tasks.ApplyKernel;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;
import com.sap.on.ibm.i.tasks.TaskDoneEvent;
import com.sap.on.ibm.i.view.HATestEditor;
import com.sap.on.ibm.i.view.HATestScriptEditor;

/**
 * @author Duncan
 *
 */
public class GUIController implements ActionListener, ItemListener, IController {
	private HATestEditor outputTestEditor;
	private Levels levels;
	private boolean stopSAPCheckBox;
	private boolean applyKernelCheckBox;
	private boolean allChecked;
	private Logging logger;
	private int currentStep;
	private int maxSteps;
	private File file;
	private HATestScriptEditor haTestScriptEditor;
	private long DELAY = 200;
	private JFileChooser fileChooser;

	public GUIController() {
		this.outputTestEditor = new HATestEditor();
		haTestScriptEditor = new HATestScriptEditor();
	}

	public void showMainView() {
		PatternLayout layout = new PatternLayout("%m%n");
		Appender appender = new Appender(this);
		appender.setLayout(layout);
		Logger.getRootLogger().addAppender(appender);
		this.outputTestEditor.setVisible(true);
		this.addListener();

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
		ExecuteSAPControl sapControl = new ExecuteSAPControl(this);
		sapControl.setLogger(getLogger());
		sapControl.setFunction("GetProcessList");
		sapControl.setInstance("00");
		sapControl.setHost("as0013");
		sapControl.execute();

	}

	private void applyKernel() {

		ApplyKernel applylKernel = new ApplyKernel(this);
		applylKernel.setLogger(getLogger());
		applylKernel.setCommand("STEP0", "cd /FSIASP/sapmnt/DCN/exe/uc");
		applylKernel.execute();
		// applylKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; rm -R as400_pase_64.backup");
		// applylKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; cp -R as400_pase_64 as400_pase_64.backup");

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

	public void saveScriptFile() {
		// BufferedWriter writer; TODO

		if (haTestScriptEditor.getTextarea().getText().equals("")) {
			Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			haTestScriptEditor.setCursor(defaultCursor);

			JOptionPane.showMessageDialog(new JFrame(),
					"There is no script to save", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			saveScriptAs();
		}
	}

	public void saveScriptAs() {
		FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
				"Text File", "txt");
		final JFileChooser saveAsFileChooser = new JFileChooser();
		saveAsFileChooser.setApproveButtonText("Save");
		saveAsFileChooser.setFileFilter(extensionFilter);
		int actionDialog = saveAsFileChooser.showOpenDialog(haTestScriptEditor);
		if (actionDialog != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = saveAsFileChooser.getSelectedFile();
		if (!file.getName().endsWith(".txt")) {
			file = new File(file.getAbsolutePath() + ".txt");
		}

		BufferedWriter outFile = null;
		try {
			outFile = new BufferedWriter(new FileWriter(file));

			haTestScriptEditor.getTextarea().write(outFile);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (outFile != null) {
				try {
					outFile.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Read a representation of the script model from the input stream
	 * 
	 * @return the script model as read from the compressed file
	 * 
	 * @throws ClassNotFoundException
	 *             if the compressed file is corrupt
	 * @throws IOException
	 * 
	 * @see scriptModel
	 */
	public ScriptModel readScriptToModel(String guiscript)
			throws ClassNotFoundException, IOException {
		ScriptModel scriptModel = new ScriptModel();
		try {
			  
				int totalLines = haTestScriptEditor.getTextarea()
						.getLineCount();
				for (int i = 0; i < totalLines; i++) {
					int start = haTestScriptEditor.getTextarea()
							.getLineStartOffset(i);
					int end;
					end = haTestScriptEditor.getTextarea().getLineEndOffset(i);
					String line = guiscript.substring(start, end);
					if (!line.equals("") && line.contains(":")) {
						String[] split = guiscript.split(":");
						String firstSubString = split[0];
						String secondSubString = split[1];

						if (firstSubString.trim().equals("SAP_SID")) {
							scriptModel.setSid(secondSubString);
						}

						if (firstSubString.trim().equals("SAP_User")) {
							scriptModel.setSid(secondSubString);
						}

						if (firstSubString.trim().equals("SAP_Password")) {
							scriptModel.setPassword(secondSubString);
						}

						if (firstSubString.trim().equals("SAP_Global_Kernel")) {
							scriptModel.setGlobal_Kernel(secondSubString);
						}

						if (firstSubString.trim().equals("SAP_NightlyMake")) {
							scriptModel.setNightlyMake(secondSubString);
						}

						if (firstSubString.trim().equals("Step0")) {
							scriptModel.setStopSAP(secondSubString);
						}

						if (firstSubString.trim().equals("Step1")) {
							scriptModel.setApplyKernel(secondSubString);
						}

						if (firstSubString.trim().equals("Step2")) {
							scriptModel.setStartSAP(secondSubString);
						}

						if (firstSubString.trim().equals("Step3")) {
							scriptModel.setRunHATest(secondSubString);
						}

					}
				}
			 
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return scriptModel;
	}

	public void loadScript() {

		if (haTestScriptEditor.getTextarea().equals("")) {
			Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			haTestScriptEditor.setCursor(defaultCursor);

			JOptionPane.showMessageDialog(new JFrame(),
					"There is no script to run", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {

			Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
			try {
				synchronized (waitCursor) {
					haTestScriptEditor.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					waitCursor.wait(900);
				}
				
				String script = haTestScriptEditor.getTextarea().getText();
				ScriptModel readScriptToModel = readScriptToModel(script);

				if (readScriptToModel.getStopSAP().trim().equals("StopSAP")) {
					getOutputTestEditor().getStop_SAP_Checkbox().setSelected(
							true);
				}
				if (readScriptToModel.getApplyKernel().trim()
						.equals("ApplyKernel")) {
					getOutputTestEditor().getApplyKernelCheckbox().setSelected(
							true);
				}
				if (readScriptToModel.getStartSAP().trim().equals("StartSAP")) {
					getOutputTestEditor().getStartSAPCheckBox().setSelected(
							true);
				}
				if (readScriptToModel.getSid().trim().equals("SAP_SID")) {
					getOutputTestEditor().getSap_SID_Field().setText("");
					getOutputTestEditor().getSap_SID_Field().setText("bigboss");
				}
				if (!readScriptToModel.getPassword().trim().equals(" ")) {
					getOutputTestEditor().getSap_PASSWORD_Field().setText(" ");
					getOutputTestEditor().getSap_PASSWORD_Field().setText(
							"qsecofer");
				}
				if (!readScriptToModel.getGlobal_Kernel().equals("")) {
					getOutputTestEditor().getSap_Nigtly_MakeField().setText(
							readScriptToModel.getGlobal_Kernel());
				}

				if (!readScriptToModel.getNightlyMake().equals("")) {
					getOutputTestEditor().getSap_Nigtly_MakeField().setText(
							readScriptToModel.getGlobal_Kernel());
				}

			} catch (ClassNotFoundException | IOException
					| InterruptedException e) {
				e.printStackTrace();
			}

			logger.getLogger().info(
					"Script" + " " + "[ " + file + " ]" + " "
							+ "Successfully loaded....");

			haTestScriptEditor.setCursor(waitCursor);

			Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			haTestScriptEditor.setCursor(defaultCursor);
			haTestScriptEditor.setInVisible();
		}

	}

	public void openScriptFile() {
		String userDirLocation = System.getProperty("user.dir");
		File userDir = new File(userDirLocation);
		fileChooser = new JFileChooser(userDir);

		int result = fileChooser.showOpenDialog(haTestScriptEditor.getGui());
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				file = fileChooser.getSelectedFile();
				FileReader fr = new FileReader(file);
				haTestScriptEditor.getTextarea().read(fr, file);
				fr.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {

			if (e.getSource() == outputTestEditor.getImportScriptJMenuItem()) {

				haTestScriptEditor.setVisible();
			}

			if (e.getSource() == haTestScriptEditor.getOpenButton()) {
				openScriptFile();
			}
			if (e.getSource() == haTestScriptEditor.getRunScriptButton()) {
				loadScript();
			}

			if (e.getSource() == haTestScriptEditor.getSaveButton()) {
				saveScriptFile();
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
	public void progress(PropertyChangeEvent evt, String Taskname) {
		SwingWorker worker = (SwingWorker) evt.getSource();
		Object stateValue = evt.getNewValue();
		String propertyName = evt.getPropertyName();
		if (stateValue.equals(SwingWorker.StateValue.STARTED)) {
			// _progressJDialog.setVisible(true);
			outputTestEditor.getStatusBarJLabel().setText(
					"In " + evt.getPropertyName().toString());
		}

		else if (propertyName.equals("progress")) {
			int progress = (Integer) evt.getNewValue();
			outputTestEditor.getjProgressBar().setString(
					Taskname + " " + progress + "%");
			outputTestEditor.getStatusBarJLabel().setText(
					"In " + evt.getPropertyName().toString());
			outputTestEditor.getjProgressBar().setValue(+progress);
			outputTestEditor.getjProgressBar().setStringPainted(true);
			outputTestEditor.getPlayButton().setEnabled(false);
		}

		else if (stateValue.equals(SwingWorker.StateValue.PENDING)) {
			outputTestEditor.getStatusBarJLabel().setText(
					evt.getPropertyName().toString() + " ....");
		} else if (stateValue.equals(SwingWorker.StateValue.DONE)) {
			try {
				// if (isCancelled()) {
				// getOutputTestEditor().getStatusBarJLabel().setText(
				// "Process canceled");
				// } else {
				getLogger().logMessages(Levels.INFO, " " + worker.get(), null);
				getOutputTestEditor().getStatusBarJLabel().setText(
						worker.get().toString());
				getOutputTestEditor().getPlayButton().setEnabled(true);
				// }
				// _progressJDialog.dispose();
				// if (_progressJDialog.isCancelled()) {
				// _swingWorker.cancel(true);
				// } else {
				// _progressJDialog.setProgress((Integer) evt.getNewValue());
				// }

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	private void addListener() {
		outputTestEditor.setclearLogViewButtonActionListener(this);
		outputTestEditor.setcopyActionListener(this);
		outputTestEditor.setstop_SAP_CheckboxActionListener(this);
		outputTestEditor.setplayButtonActionListener(this);
		outputTestEditor.setstopButtonActionListener(this);
		outputTestEditor.setexitJMenuItemActionListener(this);
		outputTestEditor.setinforJMenuItemActionListener(this);
		outputTestEditor.setImportScriptJMenuItemActionListener(this);

		// scriptEditor
		haTestScriptEditor.getOpenButton().addActionListener(this);
		haTestScriptEditor.getSaveButton().addActionListener(this);
		haTestScriptEditor.getRunScriptButton().addActionListener(this);
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

	@Override
	public void setProgressbarMax(int nSteps) {
		this.currentStep = 0;
		this.maxSteps = nSteps;
	}

	@Override
	public void updateProgressbar() {
		while (currentStep < 30) {
			try {
				outputTestEditor.getjProgressBar().setValue(++this.currentStep);
				outputTestEditor.getjProgressBar().setStringPainted(true);
				outputTestEditor.getPlayButton().setEnabled(false);
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public int getCurrentStep() {
		return currentStep;
	}

	public int getMaxSteps() {
		return maxSteps;
	}

}
