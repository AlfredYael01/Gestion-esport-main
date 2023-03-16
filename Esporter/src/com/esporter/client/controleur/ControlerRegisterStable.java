package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.esporter.both.types.TypesImage;
import com.esporter.both.types.TypesLogin;
import com.esporter.both.types.TypesStable;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.stable.RegisterStable;

public class ControlerRegisterStable extends MasterControler implements ActionListener, MouseListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "REGISTER_STABLE_CANCEL":
			MasterFrame.getInstance().setPanel(com.esporter.client.vue.Calendar.class, MasterControler.getUser().getPermission());
			break;
		case "REGISTER_STABLE_VALIDATE":
			RegisterStable regSt = (RegisterStable) MasterFrame.getCurrentPanel();
			
			
			String name = regSt.getTxtStableName().getText();
			String nickname = regSt.getTxtNickname().getText();
			BufferedImage image = regSt.getImage() ;
			String username = regSt.getTxtUsername().getText();
			String password = new String(regSt.getTxtPassword().getPassword());
			

			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Le champ nom n'est pas specifié","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(nickname.isEmpty()){
				JOptionPane.showMessageDialog(null, "Le champ diminutif n'est pas specifié","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(image == null){
				JOptionPane.showMessageDialog(null, "L'image n'est pas choisie","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(username.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Le nom d'utilisateur n'est pas choisi","Erreur", JOptionPane.ERROR_MESSAGE);
			}else if(password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Le mot de passe n'est pas choisi","Erreur", JOptionPane.ERROR_MESSAGE);
			}else {
				try {
					ByteArrayOutputStream blob = new ByteArrayOutputStream();
					
					image = TypesImage.resize(image, 200, 300);
					try {
						ImageIO.write(image, "png", blob);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					InputStream is = new ByteArrayInputStream(blob.toByteArray());
					BufferedImage bf = ImageIO.read(is);
					TypesStable st = new TypesStable(name, new TypesImage(bf, "png"), nickname, -1);
					TypesLogin l = new TypesLogin(username, password);
					getUser().registerStable(st, l);
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.Calendar.class, MasterControler.getUser().getPermission());
				} catch (IOException e1) {
					fireError( new IllegalArgumentException("Il y a une erreur avec la photo"), false, false);
				} 
			}
			break;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		RegisterStable rs = (RegisterStable) MasterFrame.getCurrentPanel();
		JFileChooser fileE = rs.getFileExplorer();
		int resultFile = fileE.showOpenDialog(null);
        //si l'utilisateur clique sur enregistrer dans Jfilechooser
        if(resultFile == JFileChooser.APPROVE_OPTION){
        	File selFile = fileE.getSelectedFile();
        	try {
        		rs.setFile(selFile);
        	} catch(Exception ee) {
        		JOptionPane.showMessageDialog(null, "Choisissez une image ayant un ratio de 1","Erreur", JOptionPane.ERROR_MESSAGE);
        	}
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
