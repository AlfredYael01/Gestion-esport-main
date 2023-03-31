package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.esporter.both.types.TypesFame;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesTournament;
import com.esporter.both.types.exception.ExceptionInvalidPermission;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.DatePicker;
import com.esporter.client.vue.organizer.EditTournament;

public class ControlerModifyTournament extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "MODIFY_TOURNAMENT_DATE":
			//create frame new object  f
			final JFrame f = new JFrame();
			//set text which is collected by date picker i.e. set date 
			((EditTournament)MasterFrame.getCurrentPanel()).getTxtDateStartTournament().setText(new DatePicker(f, ((EditTournament)MasterFrame.getCurrentPanel()).getTxtDateStartTournament().getText()).setPickedDate());
			break;
		case "MODIFY_TOURNAMENT_VALIDATE":
			try {
				EditTournament editPage = ((EditTournament)MasterFrame.getCurrentPanel());
				if(editPage.getTxtDateStartTournament().getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Veuillez préciser la date de début du tournoi","Error", JOptionPane.ERROR_MESSAGE);
				}else if(editPage.getTxtTournamentName().getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Veuillez préciser le nom du tournoi","Error", JOptionPane.ERROR_MESSAGE);
				}else {
					Timestamp tournamentStart = null;
					try {
						tournamentStart = Timestamp.valueOf(editPage.getTxtDateStartTournament().getText() + " 00:00:00");
					} catch (IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null, "Format de date invalide","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					int n = JOptionPane.showConfirmDialog(null, "Confirmez vous la modification du tournoi ?","Confirmation", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						TypesTournament t = new TypesTournament(tournamentStart, editPage.getTxtTournamentName().getText(), 
								(TypesFame) editPage.getComboBoxFame().getSelectedItem(), (TypesGame) editPage.getComboBoxGame().getSelectedItem(),editPage.getTournament().getId());
						TypesTournament dummyDate = new TypesTournament(tournamentStart, null, null,null, -1);
						if (MasterFrame.getInstance().getUser().getData().listSortedTournaments().contains(t) && !(MasterFrame.getInstance().getUser().getData().listSortedTournaments().get(MasterFrame.getInstance().getUser().getData().listSortedTournaments().indexOf(dummyDate)).getId() == t.getId())) { 
							JOptionPane.showMessageDialog(null, "Un tournoi à cette date existe déjà","Error", JOptionPane.ERROR_MESSAGE);
						}else{
							MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
							MasterFrame.getInstance().getUser().modifyTournament(t);
						}
					}
				}
			} catch (ExceptionInvalidPermission e1) {

			}
			break;
		case "MODIFY_TOURNAMENT_CANCEL":
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
			break;
		}
		
	}
	
}
