package com.esporter.client.vue.error;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.esporter.both.types.exception.ExceptionInvalidPermission;
import com.esporter.client.vue.MasterFrame;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JTextPane;
import javax.swing.JProgressBar;

public class Error extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1996527475653186651L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblTitle;
	private JTextPane textPaneError;
	private JProgressBar progressBar;
	private static Error instance = null;


	/**
	 * Create the dialog.
	 */
	public Error(String title, String message, int progress) {
		super();
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(1);
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
				
			}
		});
		
		setTitle("Error");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(400,600));
		this.setAlwaysOnTop(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		panelNorth.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		getContentPane().add(panelNorth, BorderLayout.NORTH);
		
		lblTitle = new JLabel(title);
		panelNorth.add(lblTitle);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		
		textPaneError = new JTextPane();
		textPaneError.setText(message);
		textPaneError.setEditable(false);
		textPaneError.setMinimumSize(new Dimension(100,200));
		panelCenter.add(textPaneError);
		
		
		JPanel panelSouth = new JPanel();
		panelSouth.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(MasterFrame.COLOR_MASTER);
		progressBar.setValue(progress);
		progressBar.setIndeterminate(true);
		panelSouth.add(progressBar);
		setVisible(true);
		
	}
	
	
	public void addProgress(int progress) {
		progressBar.setValue(progress);
		
		progressBar.paintImmediately(progressBar.getVisibleRect());
		
	}
	
	public void addMessage(String message) {
		String existingMessage = textPaneError.getText();
		textPaneError.setText(existingMessage+"\n"+message);
		this.revalidate();
		this.repaint();
		
	}
	
	public int getProgress() {
		return progressBar.getValue();
	}
	
	public void disappear() {
		this.setVisible(false);
	}

}
