package com.sap.on.ibm.i.logger;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import com.sap.on.ibm.i.editor.controller.Controller;

public class Appender extends AppenderSkeleton {

	private JTextArea textArea;
	private Controller controller;
	private static final int CONSOLE_LINE_BUFFER_SIZE = 1000;
	private String cache;

	public Appender(Controller controller, JTextArea a) {
		this.textArea = a;
		this.controller = controller;
	}

	protected void append(LoggingEvent e) {
	            String[] lines = e.getThrowableStrRep();
	            if (lines != null) {
	                for (String line : lines) {
	                	System.out.println("we are here");
	                    textArea.append(line + System.getProperty("line.separator"));
	                }
	            }

		final String level = e.getLevel().toString();
		final String msg = e.getRenderedMessage();
		if (cache != null && cache.equals(msg)) {
			// Nothing
		} else {
			SwingUtilities.invokeLater(new Thread(
					"Agent Log4JJTextAreaAppender Thread") {
				public void run() {
					textArea.append(msg + "\n");
					if (textArea.getLineCount() > CONSOLE_LINE_BUFFER_SIZE) {
						// remove old lines
						try {
							textArea.replaceRange(
									"",
									0,
									textArea.getLineEndOffset(textArea.getLineCount()
											- CONSOLE_LINE_BUFFER_SIZE));
						} catch (BadLocationException e) {
							// ignore
						}
					}
					// Make sure the last line is always visible
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
			});
			cache = msg;
		}

	}

	public void close() {

	}

	public boolean requiresLayout() {
		return false;
	}

}
