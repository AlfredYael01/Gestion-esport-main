package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesTeam;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.stable.TeamManagement;
import com.esporter.client.vue.stable.management.AddTeam;

public class ControlerStableManagement extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "STABLE_MANAGEMENT_ADDTEAM":
			TypesGame game = (TypesGame)((TeamManagement)MasterFrame.getCurrentPanel()).getComboBoxFilterGame().getSelectedItem();
			if (game == null) {
				game = TypesGame.intToGame(1); 
			}
			MasterFrame.getInstance().setPanel(AddTeam.class, game);
			break;
		case "STABLE_MANAGEMENT_COMBO":
			((TeamManagement)MasterFrame.getCurrentPanel()).setFilterGame((TypesGame)((TeamManagement)MasterFrame.getCurrentPanel()).getComboBoxFilterGame().getSelectedItem());
			break;
		default:
			if(e.getActionCommand().contains("RENDERER")) {
				String model = null;
				if (e.getActionCommand().contains("INFO")) {
					model = "STABLE_MANAGEMENT RENDERER INFO ";
				}else {
					model = "STABLE_MANAGEMENT RENDERER MODIFY ";
				}
				int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
				TypesTeam team = ((TeamManagement)MasterFrame.getCurrentPanel()).getRenderer(id).getTeam();
				if (e.getActionCommand().contains("INFO")) {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.SeePlayerInfos.class, team);
				}else {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.ModifyTeam.class, team);
				}
			}
			break;
		}
		
	}
	
}
