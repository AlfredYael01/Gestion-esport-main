package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesTournament;
import com.esporter.client.vue.Calendar;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.DatePicker;
import com.esporter.client.vue.player.RegisterTournament;

public class ControlerCalendar extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		com.esporter.client.vue.component.Calendar cal;
		if(MasterFrame.getCurrentPanel() instanceof com.esporter.client.vue.organizer.Calendar) {
			cal = ((com.esporter.client.vue.organizer.Calendar)MasterFrame.getCurrentPanel());
		} else {
			cal = ((Calendar)MasterFrame.getCurrentPanel());
		}
		
		switch(e.getActionCommand()) {
		case "CALENDAR_DATE":
			//create frame new object  f
			final JFrame f = new JFrame();
			//set text which is collected by date picker i.e. set date 
			cal.getTxtDate().setText(new DatePicker(f, cal.getTxtDate().getText()).setPickedDate());
			MasterFrame.getInstance().getMain().repaint();
            MasterFrame.getInstance().getMain().revalidate();
            if(!cal.getTxtDate().getText().equals("")) {
            	try {
            		Timestamp t = Timestamp.valueOf(cal.getTxtDate().getText() + " 00:00:00");
            		System.out.println(t.toString());
    				cal.setChosenDate(t);
    				cal.createListTournament(t,cal.getGame() );
            	} catch (Exception e1) {
            		cal.createListTournament(null,cal.getGame() );
            	}
				
            } else {
            	cal.createListTournament(null,cal.getGame() );
            }
            MasterFrame.getInstance().getMain().repaint();
            MasterFrame.getInstance().getMain().revalidate();
			break;
		case "CALENDAR_GAMECOMBO":
            cal.createListTournament(cal.getChosenDate(),(TypesGame)cal.getComboBoxFilterGame().getSelectedItem());
            ((JPanel)cal).repaint();
            ((JPanel)cal).revalidate();
			break;
		case "CALENDAR_ADD":
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.CreateTournament.class,null);
			break;
		default:
			if(e.getActionCommand().contains("RENDERER")) {
				String model = null;
				if(e.getActionCommand().contains("MODIFY")) {
					model = "CALENDAR RENDERER MODIFY ";
				}else if(e.getActionCommand().contains("REMOVE")) {
					model = "CALENDAR RENDERER REMOVE ";
				} else if(e.getActionCommand().contains("INSCRIPTION")) {
					model = "CALENDAR RENDERER INSCRIPTION ";
				} else if(e.getActionCommand().contains("SCORE")) {
					model = "CALENDAR RENDERER SCORE ";
				} else if(e.getActionCommand().contains("POOL")) {
					model = "CALENDAR RENDERER POOL ";
				}
				int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
				TypesTournament tournament;
				if(MasterFrame.getCurrentPanel() instanceof com.esporter.client.vue.organizer.Calendar) {
					tournament = ((com.esporter.client.vue.organizer.Calendar)MasterFrame.getCurrentPanel()).getRenderer(id).getTournament();
				} else {
					tournament = ((Calendar)MasterFrame.getCurrentPanel()).getRenderer(id).getTournament();
				}
				 
				//MasterFrame.getInstance().setPanel(vue.SeePlayerInfos.class, team);
				
				if(e.getActionCommand().contains("MODIFY")) {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.EditTournament.class, tournament);
				}else if(e.getActionCommand().contains("REMOVE")) {
					int n = JOptionPane.showConfirmDialog (null, "Etes vous sur de vouloir supprimer le tournoi?","WARNING", JOptionPane.YES_NO_OPTION);
					if(n== JOptionPane.YES_OPTION) {
						MasterFrame.getInstance().getUser().deleteTournament(tournament);
					}
				} else if(e.getActionCommand().contains("INSCRIPTION")) {
					JDialog confirmation = new RegisterTournament(tournament.getId(), tournament);
					
				}else if(e.getActionCommand().contains("POOL")) {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.ProgramMatchs.class, tournament);
				}
			}
			break;
		}
		
	}
	
}
