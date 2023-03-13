package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.client.vue.CalendarAndScoreMatch;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.RendererProgramMatch;
import com.esporter.client.vue.referee.SetScore;

public class ControlerMatches extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String model = "MATCHES RENDERER SCORE ";
		int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
		RendererProgramMatch rpm = ((CalendarAndScoreMatch)MasterFrame.getCurrentPanel()).getRenderer(id);
		//Appel du JDialog pour choisir le gagnant
		SetScore scorePage = new SetScore(rpm.getMatch());
		scorePage.setVisible(true);
		scorePage.setAlwaysOnTop(true);
	}
	
}
