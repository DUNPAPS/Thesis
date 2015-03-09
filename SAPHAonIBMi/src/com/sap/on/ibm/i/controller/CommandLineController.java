package com.sap.on.ibm.i.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.model.User;
import com.sap.on.ibm.i.tasks.ApplyKernel;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;
import com.sap.on.ibm.i.view.ProgressBar;

public class CommandLineController extends Observable implements ItemListener,
		Runnable, IController, Observer {
	private User user;
	private Logging logger;
	private ProgressBar progressbar;
	private int currentStep = 0;
	private int maxSteps = 0;
	private LinkedBlockingQueue<ActionEvent> ActionEventQueue;

	public CommandLineController() {
		this.user = new User();
		this.user.setUser("qsecofr@as0013");
		this.user.setPassword("bigboss");
		this.progressbar = new ProgressBar();
		this.ActionEventQueue = new LinkedBlockingQueue<ActionEvent>();
		addObserver(this);
	}

	@Override
	public void sendDoneEvent(ActionEvent e) {
		// TODO Auto-generated method stub
		ActionEventQueue.add(e);
	}

	/**
	 * Notifies observers that the launcher state or progress has changed
	 */
	private void observableChanged() {
		setChanged();
		notifyObservers();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	private void stopSAP() {
		ExecuteSAPControl sapControl = new ExecuteSAPControl(this);
		sapControl.setLogger(getLogger());
		sapControl.setFunction("GetProcessList");
		sapControl.setInstance("00");
		sapControl.setHost("as0013");
		try {
			sapControl.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void applyKernel() {
		ApplyKernel applylKernel = new ApplyKernel(this);
		applylKernel.setLogger(getLogger());
		applylKernel.setCommand("STEP0", "cd /FSIASP/sapmnt/DCN/exe/uc");
		// applylKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; rm -R as400_pase_64.backup");
		// applylKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; cp -R as400_pase_64 as400_pase_64.backup");
		try {
			applylKernel.execute();
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

	/**
	 * Update the state of the progress bar. The argument is an array of int
	 * with two elements. The first represents the current work done, and the
	 * second the whole work that is needed to be done in order to consider the
	 * task complete.
	 *
	 * @param observableSource
	 *            the observable object that changed state
	 * @param arr_done_all
	 *            array with current completed work
	 */
	@Override
	public void update(final Observable observableSource,
			final Object arr_done_all) {

		updateProgressbar();

	}

	public void setLogger(Logging logger) {
		this.logger = logger;
	}

	public Logging getLogger() {
		return logger;
	}

	@Override
	public void run() {
		try {
			stopSAP();
			ActionEventQueue.take();
			applyKernel();
			ActionEventQueue.take();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void setProgressbarMax(int nSteps) {
		currentStep = 0;
		maxSteps = nSteps;
		observableChanged();
	}

	@Override
	public void updateProgressbar() {
		progressbar.update(currentStep++, maxSteps);
	}

	@Override
	public void setThreadName(String name) {
	}
}
