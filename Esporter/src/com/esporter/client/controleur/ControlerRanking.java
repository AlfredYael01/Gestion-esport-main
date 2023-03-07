package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.both.types.TypesGame;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.Ranking;

public class ControlerRanking extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		((Ranking)MasterFrame.getCurrentPanel()).createListRenderer((TypesGame)((Ranking)MasterFrame.getCurrentPanel()).getComboBoxGame().getSelectedItem());
	}
	
}
