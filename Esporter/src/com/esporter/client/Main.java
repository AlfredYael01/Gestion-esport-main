package com.esporter.client;
import java.awt.EventQueue;

import com.esporter.client.controleur.Controler;
import com.esporter.client.vue.MasterFrame;

public class Main {

	public static void main(String[] args) {
		Controler control = Controler.getInstance();
		MasterFrame.getInstance().getFrame().setVisible(true);
	}
}
