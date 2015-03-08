package com.sap.on.ibm.i.tasks;

import com.sap.on.ibm.i.controller.IController;

public class ProgressbarTimedUpdate {

	private Thread UpdateThread;
	private IController myController;
	
	ProgressbarTimedUpdate(IController myController)
	{
		this.myController = myController;
	}

	@SuppressWarnings("deprecation")
	void Stop()
	{
		if (UpdateThread != null)
		{
			UpdateThread.stop();
			
		}
		
	}
	
	void Start(int maxWaitTimeSEC)
	{
		UpdateThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				myController.setProgressbarMax(maxWaitTimeSEC*10);
				for (int i=0; i < maxWaitTimeSEC; i++)
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
		UpdateThread.start();	
		
	}
	
}
