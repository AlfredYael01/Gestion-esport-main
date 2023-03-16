package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.client.vue.MasterFrame;

public class ControlerError extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ERROR_CONTINUE")) {
			if(MasterFrame.getInstance().getError().isCritical()) {
				MasterFrame.getInstance().getFrame().dispose();
				System.exit(-2);
			}
			MasterFrame.getInstance().getError().setVisible(false);
			MasterFrame.getInstance().getError().setException(null);
		}
		
	}
	
}
