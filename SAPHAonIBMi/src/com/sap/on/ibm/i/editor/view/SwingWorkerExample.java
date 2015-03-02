package com.sap.on.ibm.i.editor.view;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.sap.on.ibm.i.editor.controller.Controller;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SwingWorkerExample extends SwingWorker<String, Double> {
	private static JProgressBar PROGRESS_BAR;
	private static JLabel OUTPUT_LABEL;
	private Controller controller;

	public SwingWorkerExample(Controller controller) {
		this.controller = controller;
		PROGRESS_BAR = this.controller.get_outputTestEditor().getjProgressBar();
		PROGRESS_BAR.setMinimum(0);
		PROGRESS_BAR.setMaximum(100);
		OUTPUT_LABEL = new JLabel("Processing");

	}

	@Override
	protected String doInBackground() throws Exception {
		int maxNumber = 10;
		for (int i = 0; i < maxNumber; i++) {
			Thread.sleep(2000);// simulate long running process
			double factor = ((double) (i + 1) / maxNumber);
			  
			
			publish(factor);// publish the progress
		}
		return "Finished";
	}

	@Override
	protected void process(List<Double> aDoubles) {
		// update the percentage of the progress bar that is done
		int amount = PROGRESS_BAR.getMaximum() - PROGRESS_BAR.getMinimum();
		PROGRESS_BAR
				.setValue((int) (PROGRESS_BAR.getMinimum() + (amount * aDoubles
						.get(aDoubles.size() - 1))));
	}

	@Override
	protected void done() {
		try {
			OUTPUT_LABEL.setText(get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
