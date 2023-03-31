package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.esporter.client.vue.LogIn;
import com.esporter.client.vue.MasterFrame;

public class ControlerLogin extends MasterControler implements ActionListener, KeyListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "LOGIN_PROCEED":
			//requestFocus();
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
			break;
		case "LOGIN_CANCEL":
			MasterFrame.getInstance().getLoginPage().setVisible(false);
			MasterFrame.getInstance().getMain().setVisible(true);
			break;
		case "LOGIN_REGISTER_STABLE":
			MasterFrame.getInstance().getLoginPage().setVisible(false);
			MasterFrame.getInstance().getMain().setVisible(true);
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.RegisterStable.class, null);
			break;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!MasterFrame.getInstance().getError().isVisible() && MasterFrame.getInstance().getLoginPage().isVisible()) {
			if (e.getKeyCode()==KeyEvent.VK_ENTER){
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
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}

}
