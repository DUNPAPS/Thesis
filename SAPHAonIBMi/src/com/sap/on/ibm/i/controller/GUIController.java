package com.sap.on.ibm.i.controller;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
public class GUIController implements ActionListener, IController {
	private HATestEditor haTestEditor;
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
	private String threadName;
	private ExecuteSAPControl sapControl;
	private ApplyKernel applylKernel;

	public GUIController() {
		this.haTestEditor = new HATestEditor();
		this.haTestScriptEditor = new HATestScriptEditor();
	}

	public void showMainView() {
		PatternLayout layout = new PatternLayout("%-5p [%t]: %m%n");
		Appender appender = new Appender(this);
		appender.setLayout(layout);
		Logger.getRootLogger().addAppender(appender);
		this.haTestEditor.setVisible(true);
		this.addListener();

	}

	@Override
	public void sendDoneEvent(ActionEvent e) {
		actionPerformed(e);
		this.haTestEditor.getPlayButton().setEnabled(true);
	}

	private void stopSAP() {
		this.sapControl = new ExecuteSAPControl(this);
		this.sapControl.setLogger(getLogger());
		this.sapControl.setFunction("GetProcessList");
		this.sapControl.setInstance("00");
		this.sapControl.setHost("as0013");
		this.sapControl.execute();

	}

	private void applyKernel() {

		this.applylKernel = new ApplyKernel(this);
		this.applylKernel.setLogger(getLogger());
		this.applylKernel.setCommand("STEP0", "cd /FSIASP/sapmnt/DCN/exe/uc");
		this.applylKernel.execute();
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
			int totalLines = haTestScriptEditor.getTextarea().getLineCount();
			String text = haTestScriptEditor.getTextarea().getText();
			for (int i = 0; i < totalLines; i++) {
				int start = haTestScriptEditor.getTextarea()
						.getLineStartOffset(i);
				int end = haTestScriptEditor.getTextarea().getLineEndOffset(i);
				String line = text.substring(start, end);
				if (!line.equals("") && line.contains(":")) {
					String[] split = line.split(":");
					String firstSubString = split[0];
					String secondSubString = split[1];

					if (firstSubString.trim().equals("SAP_SID")) {
						scriptModel.setSid(secondSubString);
					}

					if (firstSubString.trim().equals("SAP_User")) {
						scriptModel.setUser(secondSubString);
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

					if (firstSubString.trim().equals("Step0".trim())) {
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

		if (!haTestScriptEditor.getTextarea().getText().isEmpty()) {

			Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
			try {
				synchronized (waitCursor) {
					haTestScriptEditor.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					waitCursor.wait(900);
				}

				String script = haTestScriptEditor.getTextarea().getText();
				ScriptModel readScriptToModel = readScriptToModel(script);

				if (readScriptToModel.getStopSAP().trim()
						.equals("StopSAP".trim())) {
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
					getOutputTestEditor().getSap_SID_Field().setText(
							readScriptToModel.getSid());
				}
				if (!readScriptToModel.getPassword().trim().equals(" ")) {
					getOutputTestEditor().getSap_PASSWORD_Field().setText(" ");
					getOutputTestEditor().getSap_PASSWORD_Field().setText(
							readScriptToModel.getPassword());
				}

				if (!readScriptToModel.getUser().trim().equals(" ")) {
					getOutputTestEditor().getSap_USER_Field().setText(" ");
					getOutputTestEditor().getSap_USER_Field().setText(
							readScriptToModel.getUser());
				}

				if (!readScriptToModel.getSid().trim().equals(" ")) {
					getOutputTestEditor().getSap_SID_Field().setText(" ");
					getOutputTestEditor().getSap_SID_Field().setText(
							readScriptToModel.getSid());
				}

				if (!readScriptToModel.getGlobal_Kernel().equals("")) {
					getOutputTestEditor().getSap_Nigtly_MakeField().setText(
							readScriptToModel.getGlobal_Kernel().trim());
				}

				if (!readScriptToModel.getNightlyMake().equals("")) {
					getOutputTestEditor().getSap_global_KernelField().setText(
							readScriptToModel.getNightlyMake().trim());
				}

			} catch (ClassNotFoundException | IOException
					| InterruptedException e) {
				e.printStackTrace();
			}

			haTestScriptEditor.setCursor(waitCursor);

			Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			haTestScriptEditor.setCursor(defaultCursor);
			haTestScriptEditor.setInVisible();

			JOptionPane.showMessageDialog(new JFrame(), "Script" + " " + "[ "
					+ file + " ]" + " " + "Successfully loaded");
		} else {
			Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			haTestScriptEditor.setCursor(defaultCursor);

			JOptionPane.showMessageDialog(new JFrame(),
					"There is no script to run", "Error",
					JOptionPane.ERROR_MESSAGE);
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

			if (e.getSource() == haTestEditor.getImportScriptJMenuItem()) {

				haTestScriptEditor.setVisible();
			}
			if (e.getSource() == haTestEditor.getStopButton()) {

				this.sapControl.stopProcess();
				this.applylKernel.stopProcess();
				haTestEditor.getStart_SAP_CheckBox().setSelected(false);
				haTestEditor.getApplyKernelCheckbox().setSelected(false);
				haTestEditor.getStop_SAP_Checkbox().setSelected(false);
				haTestEditor.getStartHATestSAPCheckBox().setSelected(false);
			}

			if (e.getSource() == haTestEditor.getClearLogViewButton()) {

				haTestEditor.getJtextArea().setText("");
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
			if (e.getSource() == haTestEditor.getExitJMenuItem()) {

				System.exit(0);

			}
			if (e.getSource() == haTestEditor.getPlayButton()
					|| e instanceof TaskDoneEvent) {
				haTestEditor.getjProgressBar().setEnabled(true);
				haTestEditor.getPlayButton().setEnabled(false);
				String user = haTestEditor.getUSER();
				String password = haTestEditor.getPassword();
				stopSAPCheckBox = haTestEditor.getStop_SAP_Checkbox()
						.isSelected();
				applyKernelCheckBox = haTestEditor.getApplyKernelCheckbox()
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
					haTestEditor.getSap_SID_Field().setText("");
					haTestEditor.getSap_USER_Field().setText("");
					haTestEditor.getSap_USER_Field().setText("");
				}

				if (stopSAPCheckBox) {
					allChecked = true;
					stopSAP();
					haTestEditor.getStop_SAP_Checkbox().setSelected(false);
				} else if (applyKernelCheckBox) {
					allChecked = true;
					applyKernel();
					haTestEditor.getApplyKernelCheckbox().setSelected(false);

				} else if (!allChecked) {
					JOptionPane.showMessageDialog(null,
							" Select a Task to run..", " Run Tasks ",
							JOptionPane.ERROR_MESSAGE);
					haTestEditor.getPlayButton().setEnabled(true);
				} else {
					allChecked = false;

				}
				haTestEditor.getjProgressBar().setString(" ");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void addListener() {
		// MainEditor
		haTestEditor.getClearLogViewButton().addActionListener(this);
		haTestEditor.getStopButton().addActionListener(this);
		haTestEditor.getCopy().addActionListener(this);
		haTestEditor.getStop_SAP_Checkbox().addActionListener(this);
		haTestEditor.getPlayButton().addActionListener(this);
		haTestEditor.getStopButton().addActionListener(this);
		haTestEditor.getExitJMenuItem().addActionListener(this);
		haTestEditor.getInforJMenuItem().addActionListener(this);
		haTestEditor.getImportScriptJMenuItem().addActionListener(this);
		haTestEditor.getClearLogViewButton().addActionListener(this);

		// scriptEditor
		haTestScriptEditor.getOpenButton().addActionListener(this);
		haTestScriptEditor.getSaveButton().addActionListener(this);
		haTestScriptEditor.getRunScriptButton().addActionListener(this);
	}

	public HATestEditor getOutputTestEditor() {
		return haTestEditor;
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
		try {
			haTestEditor.getjProgressBar().setVisible(true);
			haTestEditor.getjProgressBar().setValue(++this.currentStep);
			haTestEditor.getjProgressBar().setStringPainted(true);
			haTestEditor.getStatusBarJLabel().setText(
					"Task: " + this.threadName);
			Thread.sleep(DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public int getMaxSteps() {
		return maxSteps;
	}

	@Override
	public void setThreadName(String name) {
		this.threadName = name;

	}

	@Override
	public void doneProgressBar() {
		haTestEditor.getjProgressBar().setVisible(false);
		haTestEditor.getStatusBarJLabel().setText("Done");
	}

}