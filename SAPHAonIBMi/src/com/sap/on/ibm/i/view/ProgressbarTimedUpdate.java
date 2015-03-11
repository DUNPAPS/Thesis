package com.sap.on.ibm.i.view;

import com.sap.on.ibm.i.controller.IController;

public class ProgressbarTimedUpdate {

	private Thread UpdateThread;
	private IController myController;
	private boolean postStop = false;

	public ProgressbarTimedUpdate(IController myController) {
		this.myController = myController;
	}

	public void Stop() {
		postStop = true;
		myController.doneProgressBar();

	}

	public void start(int maxWaitTimeSEC) {
		UpdateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				myController.setProgressbarMax(maxWaitTimeSEC * 10);
				for (int i = 0; i < maxWaitTimeSEC * 10 && !postStop; i++) {
					try {
						// myController.updateProgressbar();
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		}, "progressbar updater.....");
		postStop = false;
		UpdateThread.start();

	}

}
