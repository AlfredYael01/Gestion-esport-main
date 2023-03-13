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
import com.esporter.client.vue.organizer.CreateTournament;

public class ControlerAddTournament extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		CreateTournament create = ((CreateTournament)MasterFrame.getCurrentPanel());
		switch(e.getActionCommand()) {
		case "ADD_TOURNAMENT_YES":
			try {
				if(create.getTxtDateStartTournament().getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Veuillez préciser la date de début du tournoi","Error", JOptionPane.ERROR_MESSAGE);
				}else if(create.getTxtTournamentName().getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Veuillez préciser le nom du tournoi","Error", JOptionPane.ERROR_MESSAGE);
				}else {
					Timestamp tournamentStart = null;
					try {
						tournamentStart = Timestamp.valueOf(create.getTxtDateStartTournament().getText() + " 00:00:00");
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Format de date invalide","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					int n = JOptionPane.showConfirmDialog(null, "Confirmez vous l'ajout du tournoi ?","Confirmation", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						TypesTournament t = new TypesTournament(tournamentStart, create.getTxtTournamentName().getText(), 
								(TypesFame) create.getComboBoxFame().getSelectedItem(), (TypesGame) create.getComboBoxGame().getSelectedItem(), -1);
						if (MasterFrame.getInstance().getUser().getData().listSortedTournaments().contains(t)) { 
							JOptionPane.showMessageDialog(null, "Un tournoi à cette date existe déjà","Error", JOptionPane.ERROR_MESSAGE);
						}else{
							MasterFrame.getInstance().getUser().addTournament(t);
							MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
						}
					}
				}
			} catch (ExceptionInvalidPermission e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "ADD_TOURNAMENT_DATE":
			//create frame new object  f
			final JFrame f = new JFrame();
			//set text which is collected by date picker i.e. set date 
			create.getTxtDateStartTournament().setText(new DatePicker(f).setPickedDate());
			break;
		case "ADD_TOURNAMENT_CANCEL":
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
			break;
		}
		
	}
	
}
