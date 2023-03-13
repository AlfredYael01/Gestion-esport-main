package com.esporter.client.controleur;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.esporter.client.vue.LogIn;
import com.esporter.client.vue.MasterFrame;

public class ControlerKeyStroke extends AbstractAction{
	

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!MasterFrame.getInstance().getError().isVisible() && MasterFrame.getInstance().getLoginPage().isVisible()) {
			MasterFrame master = MasterFrame.getInstance();
			LogIn logPage = master.getLoginPage();
	        try {
	            String identifiant = logPage.getTxtUsername().getText();
	            String psw = new String(logPage.getTxtPassword().getPassword());

	            master.getUser().login(identifiant, psw);
	            logPage.setVisible(false);
	            master.getMain().setVisible(true);
	        } catch (Exception e1) {
	            master.fireError(e1, false, false);
	        }
	        logPage.getTxtPassword().setText(null);
		} else if (MasterFrame.getInstance().getError().isVisible()) {
			if(MasterFrame.getInstance().getError().isCritical()) {
				MasterFrame.getInstance().getFrame().dispose();
				System.exit(-2);
			}
			MasterFrame.getInstance().getError().setVisible(false);
			MasterFrame.getInstance().getError().setException(null);
		}
	}

}
