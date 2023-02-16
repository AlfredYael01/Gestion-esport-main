package com.esporter.client;
import java.awt.EventQueue;

import com.esporter.client.controleur.Controler;
import com.esporter.client.vue.MasterFrame;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MasterFrame frame = MasterFrame.getInstance();
					frame.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
