package com.esporter.client.test;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.esporter.both.types.TypesGame;
import com.esporter.client.vue.stable.management.AddTeam;

public class testAjouterEquipe {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testAjouterEquipe window = new testAjouterEquipe();
					window.frame.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testAjouterEquipe() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		frame.getContentPane().add(new AddTeam(TypesGame.LEAGUE_OF_LEGENDS));
		
		
	}

}
