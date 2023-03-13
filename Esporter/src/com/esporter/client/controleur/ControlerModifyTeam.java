package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import com.esporter.both.socket.Response;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesTeam;
import com.esporter.both.types.exception.ExceptionTeamNotFull;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.ContainerModifyPlayer;
import com.esporter.client.vue.stable.ModifyPlayer;
import com.esporter.client.vue.stable.ModifyTeam;

public class ControlerModifyTeam extends MasterControler implements ActionListener, MouseListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "MODIFY_TEAM_VALIDATE":
			for (ContainerModifyPlayer c : ((ModifyTeam)MasterFrame.getCurrentPanel()).getPlayerList()) {
				if (c.getPlayer()==null) {
					fireError(new ExceptionTeamNotFull("Erreur de modification de l'équipe"), false, false);
					return;
				}
			}
			HashMap<Integer,TypesPlayer> players = new HashMap<>();
			for (ContainerModifyPlayer c : ((ModifyTeam)MasterFrame.getCurrentPanel()).getPlayerList()) {
				System.out.println(c.getPlayer().getId());
				players.put(c.getPlayer().getId(),c.getPlayer());
			}
			TypesTeam previousTeam = ((ModifyTeam)MasterFrame.getCurrentPanel()).getTeam();
			TypesTeam newTeam = new TypesTeam(previousTeam.getGame(), previousTeam.getStable(), players, previousTeam.getId());
			MasterFrame.getInstance().getUser().modifyTeam(newTeam);
			if (MasterFrame.getInstance().getUser().getWaiting().getActualState()==Response.UPDATE_TEAM) {
				MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.TeamManagement.class, null);
			}
			break;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		ModifyPlayer ajout = new ModifyPlayer((ContainerModifyPlayer)e.getSource());
		ajout.setVisible(true);
		ajout.setAlwaysOnTop(true);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
