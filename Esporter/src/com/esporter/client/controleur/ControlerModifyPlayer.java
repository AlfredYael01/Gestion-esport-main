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
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesStable;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.DatePicker.FilterDate;
import com.esporter.client.vue.stable.ModifyPlayer;

public class ControlerModifyPlayer extends MasterControler implements ActionListener, MouseListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		ModifyPlayer mp = (ModifyPlayer)getLastDialog();
		switch(e.getActionCommand()) {
		case "MODIFY_PLAYER_VALIDATE":
			String name = mp.getTxtName().getText();
			String firstname = mp.getTxtFirstname().getText();
			Timestamp contractStartDate = null;
			Timestamp birthDate = null;
			Timestamp contractEndDate = null;
			BufferedImage image = mp.getImage();
			try {
			contractStartDate = Timestamp.valueOf(mp.getTxtStartContractDate().getText() + " 00:00:00");
			birthDate = Timestamp.valueOf(mp.getTxtBirthDate().getText() + " 00:00:00");
			contractEndDate = Timestamp.valueOf(mp.getTxtEndContractDate().getText() + " 00:00:00");
			} catch (IllegalArgumentException e1){
				e1.printStackTrace();
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
			}else {
				image = TypesImage.resize(image, 200, 300);
				com.esporter.both.types.TypesImage im = new com.esporter.both.types.TypesImage(image, "png");
				TypesPlayer joueur = new TypesPlayer(mp.getContainer().getPlayer().getId(),name, firstname,im,birthDate,contractStartDate,contractEndDate,1,mp.getContainer().getPlayer().getIdTeam(),((TypesStable)MasterFrame.getInstance().getUser().getInfo()).getId(), mp.getContainer().getPlayer().getUsername());
				mp.getContainer().setPlayer(joueur);
				mp.dispose();
			}
			break;
		case "ADD_PLAYER_MORE1":
			mp.getTxtBirthDate().setText(openDatePicker(FilterDate.BEFORE_TODAY));
			break;
		case "ADD_PLAYER_MORE2":
			mp.getTxtEndContractDate().setText(openDatePicker());
			break;
		case "ADD_PLAYER_MORE3":
			mp.getTxtStartContractDate().setText(openDatePicker());
			break;
		}
	}

	@Override
	//if we click on the JLabel to open the file chooser
	public void mouseClicked(MouseEvent e) {
		ModifyPlayer mp = (ModifyPlayer)getLastDialog();
		JFileChooser fileExplorer = mp.getFileExplorer();
		int result = fileExplorer.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
        	File selFile = fileExplorer.getSelectedFile();
        	mp.setFile(selFile);
        }
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
