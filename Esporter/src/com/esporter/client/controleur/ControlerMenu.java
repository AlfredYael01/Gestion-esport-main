package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.both.types.TypesMenu;
import com.esporter.both.types.TypesPermission;
import com.esporter.client.vue.MasterFrame;

public class ControlerMenu extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contains("menu")) {
			if (getUser().getPermission() == TypesPermission.STABLE && e.getActionCommand().equals("menuStableManagement")) {
				MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.TeamManagement.class, null);
			} else if (e.getActionCommand().equals("menuCalendar")) {
				if(getUser().getPermission() == TypesPermission.ORGANIZER) {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
				} else {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.Calendar.class, getUser().getPermission());
				}
				
				
			} else if (e.getActionCommand().equals("menuRanking")) {
				MasterFrame.getInstance().setPanel(com.esporter.client.vue.Ranking.class, null);
			} else if (e.getActionCommand().equals("menuStable")) {
				MasterFrame.getInstance().setPanel(com.esporter.client.vue.Stables.class, null);
			} else if (e.getActionCommand().equals("menuLogin")) {
				if(getUser().getPermission()!=TypesPermission.VISITOR) {
					//LOGGED IN
					getUser().logout();
					getMasterInstance().setMenu(TypesMenu.VISITOR);
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.Calendar.class, getUser().getPermission());
				} else {
					//NOT LOG IN
					MasterFrame.getInstance().getMain().setVisible(false);
					MasterFrame.getInstance().getError().setVisible(false);
					MasterFrame.getInstance().getLoginPage().setVisible(true);
					MasterFrame.getInstance().getLoginPage().getTxtUsername().requestFocus();
				}
			}
		}
		
	}

}
