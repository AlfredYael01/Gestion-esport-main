package com.esporter.client;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import com.esporter.client.controleur.MasterControler;
import com.esporter.client.model.user.User;
import com.esporter.client.vue.MasterFrame;

public class Main {

	public static void main(String[] args) {
		try {
			MasterControler mc = new MasterControler(new User());
			SwingUtilities.invokeLater(new Runnable(){
	            public void run() {
	            	MasterFrame.getInstance();
	            }
			});
			MasterFrame.getInstance().getFrame().setVisible(true);
		} catch (UnknownHostException e) {

		} catch (IOException e) {

		}
		
	}
}
