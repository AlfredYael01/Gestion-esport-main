package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.SeePlayerInfos;

public class ControlerPlayerInfo extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "PLAYER_INFO_STABLE":
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.Stables.class, null);
			break;
		case "PLAYER_INFO_TEAM":
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.StableInfo.class, ((SeePlayerInfos)MasterFrame.getCurrentPanel()).getTeam().getStable());
			break;
		}
		
	}
	
}
