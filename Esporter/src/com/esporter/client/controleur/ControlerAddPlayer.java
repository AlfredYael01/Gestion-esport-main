package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Timestamp;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.esporter.both.types.TypesImage;
import com.esporter.both.types.TypesLogin;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesRegisterPlayer;
import com.esporter.both.types.TypesStable;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.DatePicker.FilterDate;
import com.esporter.client.vue.stable.AddPlayer;

public class ControlerAddPlayer extends MasterControler implements ActionListener, MouseListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ok1");
		AddPlayer jd = (AddPlayer)getLastDialog();
		switch(e.getActionCommand()) {
		case "ADD_PLAYER_VALIDATE":
			String name = jd.getTxtName().getText();
			String firstname = jd.getTxtFirstname().getText();
			Timestamp contractStartDate = null;
			Timestamp birthDate = null;
			Timestamp contractEndDate = null;
			BufferedImage image = jd.getImage();
			try {
			contractStartDate= Timestamp.valueOf(jd.getTxtStartContractDate().getText() + " 00:00:00");
			birthDate= Timestamp.valueOf(jd.getTxtBirthDate().getText() + " 00:00:00");
			contractEndDate= Timestamp.valueOf(jd.getTxtEndContractDate().getText() + " 00:00:00");
			} catch (IllegalArgumentException e1){
				
			}

			if (name == null) {
				JOptionPane.showMessageDialog(null, "Le champ nom n'est pas specifié","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(firstname==null){
				JOptionPane.showMessageDialog(null, "Le champ prenom n'est pas specifié","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(contractStartDate==null){
				JOptionPane.showMessageDialog(null, "Le champ debut du contrat n'est pas specifié","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(birthDate==null) {
				JOptionPane.showMessageDialog(null, "Le champ date de naissance n'est pas specifié","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(contractEndDate== null) {
				JOptionPane.showMessageDialog(null, "Le champ date de fin du contrat n'est pas specifié","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(image == null) {
				JOptionPane.showMessageDialog(null, "Il y a une erreur avec la photo","Erreur", JOptionPane.ERROR_MESSAGE);	
			}else if(getUser().chckUsernameUsed(jd.getTxtUsername().getText()).equals("1")){
				JOptionPane.showMessageDialog(null, "Le nom d'utilisateur est déjà utilisé", "Erreur", JOptionPane.ERROR_MESSAGE);
			}else {
				image = TypesImage.resize(image, 200, 300);
				
				com.esporter.both.types.TypesImage im = new com.esporter.both.types.TypesImage(image, "png");
				TypesPlayer joueur = new TypesPlayer(-1,name, firstname,im,birthDate,contractStartDate,contractEndDate,1,-1,((TypesStable)MasterFrame.getInstance().getUser().getInfo()).getId(), jd.getTxtUsername().getText());
				TypesLogin l = new TypesLogin(jd.getTxtUsername().getText(), jd.getTxtPassword().getText());
				jd.getContainer().setPlayer(new TypesRegisterPlayer(joueur, l));
				jd.dispose();
				closeDialog();
			}
			break;
		case "ADD_PLAYER_MORE1":
			jd.getTxtBirthDate().setText(openDatePicker(jd.getTxtBirthDate().getText(), FilterDate.BEFORE_TODAY));
			break;
		case "ADD_PLAYER_MORE2":
			jd.getTxtEndContractDate().setText(openDatePicker(jd.getTxtEndContractDate().getText()));
			break;
		case "ADD_PLAYER_MORE3":
			jd.getTxtStartContractDate().setText(openDatePicker(jd.getTxtStartContractDate().getText()));
			break;
			
		}
		
	}

	@Override
	//if we click on the JLabel to open the file chooser
	public void mouseClicked(MouseEvent e) {
		System.out.println("ok2");
		AddPlayer jd = (AddPlayer)getLastDialog();
		JFileChooser file = jd.getFileExplorer();
		int res = file.showOpenDialog(null);
        if(res == JFileChooser.APPROVE_OPTION){
        	File selFile = file.getSelectedFile();
        	jd.setFile(selFile);
        }
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
