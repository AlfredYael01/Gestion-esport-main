package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.both.types.TypesTeam;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.StableInfo;

public class ControlerStableInfo extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().contains("RENDERER")) {
			String model = "STABLE_INFO RENDERER ";
			int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
			TypesTeam team = ((StableInfo)MasterFrame.getCurrentPanel()).getRenderer(id).getTeam();
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.SeePlayerInfos.class, team);
		}else {
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.Stables.class, null);
		}
		
	}
	
}
