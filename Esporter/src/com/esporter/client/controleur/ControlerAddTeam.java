package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.esporter.both.socket.Response;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesRegisterPlayer;
import com.esporter.both.types.TypesRegisterTeam;
import com.esporter.both.types.TypesStable;
import com.esporter.both.types.exception.ExceptionTeamNotFull;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.ContainerPlayer;
import com.esporter.client.vue.stable.AddPlayer;
import com.esporter.client.vue.stable.management.AddTeam;

public class ControlerAddTeam extends MasterControler implements ActionListener, MouseListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "ADD_TEAM_COMBOGAME":
			MasterFrame.getInstance().setPanel(AddTeam.class, ((AddTeam)MasterFrame.getCurrentPanel()).getComboBox().getSelectedItem());
			break;
		case "ADD_TEAM_VALIDATE":
			JComboBox<TypesGame> jcombo = ((AddTeam)MasterFrame.getCurrentPanel()).getComboBox();
			for (ContainerPlayer c : ((AddTeam)MasterFrame.getCurrentPanel()).getPlayerList()) {
				if (c.getPlayer()==null) {
					fireError(new ExceptionTeamNotFull("Erreur de creation de l'équipe"),false,  false);
					return;
				}
			}
			ArrayList<TypesRegisterPlayer> players = new ArrayList<>();
			for (ContainerPlayer c : ((AddTeam)MasterFrame.getCurrentPanel()).getPlayerList()) {
				players.add(c.getPlayer());
			}
			
			TypesRegisterTeam team = new TypesRegisterTeam((TypesGame)jcombo.getSelectedItem(), ((TypesStable)MasterControler.getUser().getInfo()).getId(), players);
			MasterFrame.getInstance().getUser().addTeam(team);
			if (MasterFrame.getInstance().getUser().getWaiting().getActualState()==Response.UPDATE_TEAM) {
				MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.TeamManagement.class, null);
				JOptionPane.showMessageDialog(null, "L'équipe a bien été ajoutée", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		AddPlayer ajout = new AddPlayer((ContainerPlayer)e.getSource());
		ajout.setVisible(true);
		ajout.setAlwaysOnTop(true);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

}
