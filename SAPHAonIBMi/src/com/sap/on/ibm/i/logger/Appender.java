package com.sap.on.ibm.i.logger;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;


 
public class Appender extends AppenderSkeleton {

	private JTextArea area;
    private static final int CONSOLE_LINE_BUFFER_SIZE = 1000;

	public Appender(JTextArea a) {
		this.area = a;
	}

	protected void append(LoggingEvent e) {
	      final String msg = e.getRenderedMessage();
          SwingUtilities.invokeLater(new Thread("Agent Log4JJTextAreaAppender Thread") {
              public void run() {
                  area.append(msg + "\n");
                  if (area.getLineCount() > CONSOLE_LINE_BUFFER_SIZE) {
                      // remove old lines
                      try {
                          area.replaceRange("", 0,
                                  area.getLineEndOffset(
                                          area.getLineCount() - CONSOLE_LINE_BUFFER_SIZE
                                  ));
                      } catch (BadLocationException e) {
                          //ignore
                      }
                  }
                  // Make sure the last line is always visible
                  area.setCaretPosition(area.getDocument().getLength());
              }
          });
	}

	public void close() {

	}

	public boolean requiresLayout() {
		return false;
	}
	

}
