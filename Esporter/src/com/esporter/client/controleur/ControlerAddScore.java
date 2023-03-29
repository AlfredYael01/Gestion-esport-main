package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.esporter.both.socket.Response;
import com.esporter.both.types.TypesMatch;
import com.esporter.client.vue.referee.SetScore;

public class ControlerAddScore extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		SetScore s = (SetScore)getLastDialog();
		if(e.getActionCommand().equals("ADD_SCORE_VALIDATE")) {
			TypesMatch match = s.getMatch();
			if(s.getRdbTeam1().isSelected()) {
				match.setPoint(1, 0);
				JOptionPane.showMessageDialog(null, "Choix enregistré","", JOptionPane.INFORMATION_MESSAGE);
				s.dispose();
				closeDialog();
				MasterControler.getUser().changeScore(match, match.getIdTournament(), match.getIdPool());
				MasterControler.getUser().getWaiting().waitFor(Response.ERROR, Response.ERROR_PERMISSION, Response.UPDATE_TOURNAMENT);
			} else if (s.getRdbTeam2().isSelected()) {
				match.setPoint(0, 1);
				JOptionPane.showMessageDialog(null, "Choix enregistré","", JOptionPane.INFORMATION_MESSAGE);
				s.dispose();
				closeDialog();
				MasterControler.getUser().changeScore(match, match.getIdTournament(), match.getIdPool());
				MasterControler.getUser().getWaiting().waitFor(Response.ERROR, Response.ERROR_PERMISSION, Response.UPDATE_TOURNAMENT);
			} else {
				JOptionPane.showMessageDialog(null, "Aucun choix selectionné","Erreur", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getActionCommand().equals("ADD_SCORE_CANCEL")) {
			s.dispose();
		}
		
	}
	
}
