package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.exception.ExceptionInvalidPermission;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.player.RegisterTournament;

public class ControlerRegistration extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		RegisterTournament reg = (RegisterTournament)getLastDialog();
		switch(e.getActionCommand()) {
		case "INSCRIPTION_YES":
			if(!reg.getTournament().getRegistered().contains(((TypesPlayer)MasterFrame.getInstance().getUser().getInfo()).getIdTeam())){
				try {
					MasterFrame.getInstance().getUser().registerTournament(reg.getTournament().getId());
					reg.dispose();
				} catch (ExceptionInvalidPermission e1) {
					
				}
			}else {
				try {
					MasterFrame.getInstance().getUser().unregisterTournament(reg.getTournament().getId(), TypesGame.gameToInt(reg.getTournament().getGame()));
				} catch (ExceptionInvalidPermission e1) {
				};
				reg.dispose();
			}
			break;
		case "INSCRIPTION_NO":
			reg.dispose();
			break;
		}
		
	}
	
}
