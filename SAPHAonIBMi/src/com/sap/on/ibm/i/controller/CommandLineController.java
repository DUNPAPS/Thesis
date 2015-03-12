package com.sap.on.ibm.i.controller;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.model.ScriptModel;
import com.sap.on.ibm.i.tasks.ApplyKernel;
import com.sap.on.ibm.i.tasks.SAPControl;
import com.sap.on.ibm.i.view.ProgressBar;

public class CommandLineController extends Observable implements Runnable,
		IController, Observer {
	private Logging logger;
	private ProgressBar progressbar;
	@SuppressWarnings("unused")
	private int currentStep = 0;
	@SuppressWarnings("unused")
	private int maxSteps = 0;
	private LinkedBlockingQueue<ActionEvent> ActionEventQueue;
	private ScriptModel scriptModel;

	public CommandLineController() {
		this.progressbar = new ProgressBar();
		this.ActionEventQueue = new LinkedBlockingQueue<ActionEvent>();
		addObserver(this);
	}

	@Override
	public void sendDoneEvent(ActionEvent e) {
		ActionEventQueue.add(e);
	}

	/**
	 * Notifies observers that the launcher state or progress has changed
	 */
	private void observableChanged() {
		setChanged();
		notifyObservers();
	}

	private void stopSAP() {
		SAPControl sapControl = new SAPControl(this);
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
		applylKernel.setScriptModel(this.scriptModel);
		applylKernel.setCommand("STEP0", "cd /FSIASP/sapmnt/DCN/exe/uc");
		try {
			applylKernel.execute();
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
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

			// new command
			System.out.println("");
			System.out.printf(" ");
			getLogger().logMessages(Levels.INFO, "SAPControl", null);
			stopSAP();
			ActionEventQueue.take();

			// new command
			getLogger().logMessages(Levels.INFO, "ApplyKernel", null);
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
	}

	@Override
	public void setThreadName(String name) {
	}

	@Override
	public void doneProgressBar() {
		progressbar.finish();
	}

	public void setScriptModel(ScriptModel scriptModel) {
		this.scriptModel = scriptModel;
	}

}
