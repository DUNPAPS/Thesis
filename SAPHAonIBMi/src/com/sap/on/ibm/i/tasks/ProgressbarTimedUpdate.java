package com.sap.on.ibm.i.tasks;

import com.sap.on.ibm.i.controller.IController;

public class ProgressbarTimedUpdate {

	private Thread UpdateThread;
	private IController myController;
	private boolean postStop = false;
	
	ProgressbarTimedUpdate(IController myController)
	{
		this.myController = myController;
	}

	void Stop()
	{
		myController.doneProgressBar();
		postStop = true;
		
	}
	
	void start(int maxWaitTimeSEC)
	{
		UpdateThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				myController.setProgressbarMax(maxWaitTimeSEC*10);
				for (int i=0; i < maxWaitTimeSEC * 10 && !postStop; i++)
				{
					try {
						myController.updateProgressbar();
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
