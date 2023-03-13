package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.ProgramMatchs;

public class ControlerPool extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.CalendarAndScoreMatch.class, ((ProgramMatchs)MasterFrame.getCurrentPanel()).getTournament());
	}
	
}
