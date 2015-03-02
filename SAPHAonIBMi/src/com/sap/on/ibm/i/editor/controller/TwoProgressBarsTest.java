package com.sap.on.ibm.i.editor.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class TwoProgressBarsTest {
	private final JTextArea area = new JTextArea();
	private final JPanel statusPanel = new JPanel(new BorderLayout(0, 2));
	private final JButton runButton = new JButton(new RunAction());
	private SwingWorker<String, DownloadProgress> worker;

	public JComponent makeUI() {
		area.setEditable(false);
		JPanel p = new JPanel(new BorderLayout(5, 5));
		p.add(new JScrollPane(area));
		p.add(runButton, BorderLayout.NORTH);
		p.add(statusPanel, BorderLayout.SOUTH);
		p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return p;
	}

	class RunAction extends AbstractAction {
		public RunAction() {
			super("run");
		}

		@Override
		public void actionPerformed(ActionEvent evt) {
			final JProgressBar bar1 = new JProgressBar();
			final JProgressBar bar2 = new JProgressBar();
			runButton.setEnabled(false);
			statusPanel.add(bar1, BorderLayout.NORTH);
			statusPanel.add(bar2, BorderLayout.SOUTH);
			statusPanel.revalidate();
			worker = new SwingWorker<String, DownloadProgress>() {
				@Override
				public String doInBackground() {
					int current = 0;
					int lengthOfTask = 12; // filelist.size();
					publish(new DownloadProgress(Target.LOG, "Length Of Task: "
							+ lengthOfTask));
					publish(new DownloadProgress(Target.LOG,
							"\n-------------------------\n"));
					while (current < lengthOfTask && !isCancelled()) {
						if (!bar1.isDisplayable()) {
							return "Disposed";
						}
						try {
							convertFileToSomething();
						} catch (InterruptedException ie) {
							return "Interrupted";
						}
						publish(new DownloadProgress(Target.LOG, "*"));
						publish(new DownloadProgress(Target.TOTAL, 100
								* current / lengthOfTask));
						current++;
					}
					publish(new DownloadProgress(Target.LOG, "\n"));
					return "Done";
				}

				private final Random r = new Random();

				private void convertFileToSomething()
						throws InterruptedException {
					int current = 0;
					// long lengthOfTask = file.length();
					int lengthOfTask = 10 + r.nextInt(50);
					while (current <= lengthOfTask && !isCancelled()) {
						int iv = 100 * current / lengthOfTask;
						Thread.sleep(20); // dummy
						publish(new DownloadProgress(Target.FILE, iv + 1));
						current++;
					}
				}

				@Override
				protected void process(List<DownloadProgress> chunks) {
					for (DownloadProgress s : chunks) {
						switch (s.component) {
						case TOTAL:
							bar1.setValue((Integer) s.value);
							break;
						case FILE:
							bar2.setValue((Integer) s.value);
							break;
						case LOG:
							area.append((String) s.value);
							break;
						}
					}
				}

				@Override
				public void done() {
					runButton.setEnabled(true);
					statusPanel.removeAll();
					statusPanel.revalidate();
					String text = null;
					if (isCancelled()) {
						text = "Cancelled";
					} else {
						try {
							text = get();
						} catch (Exception ex) {
							ex.printStackTrace();
							text = "Exception";
						}
					}
					area.append(text);
					area.setCaretPosition(area.getDocument().getLength());
				}
			};
			worker.execute();
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.getContentPane().add(new TwoProgressBarsTest().makeUI());
		f.setSize(320, 240);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}

enum Target {
	TOTAL, FILE, LOG
}

class DownloadProgress {
	public final Object value;
	public final Target component;

	public DownloadProgress(Target component, Object value) {
		this.component = component;
		this.value = value;
	}
}
