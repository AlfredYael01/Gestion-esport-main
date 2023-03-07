package com.esporter.client.vue.referee;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.esporter.both.types.TypesMatch;
import com.esporter.client.controleur.ControlerAddScore;
import com.esporter.client.controleur.MasterControler;

public class SetScore extends JDialog{
	
	private JRadioButton rdbTeam1;
	private JRadioButton rdbTeam2;
	private TypesMatch match;

	public SetScore(TypesMatch match) {
		this.match = match;
		MasterControler.openDialog(this);
		setModalityType(ModalityType.APPLICATION_MODAL);

		setSize(new Dimension(400,200));

        // add radio buttons to a ButtonGroup
        final ButtonGroup group = new ButtonGroup();

        getContentPane().setLayout(new GridLayout(0, 1));
        JLabel label = new JLabel("Choisir le gagnant du match");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label);
        
        JPanel panelMain = new JPanel();
        getContentPane().add(panelMain);
        
        rdbTeam1 = new JRadioButton(MasterControler.getUser().getData().getTeams().get(match.getTeam1()).getStable().getNickname());
        group.add(rdbTeam1);
        panelMain.add(rdbTeam1);
        
        rdbTeam2 = new JRadioButton(MasterControler.getUser().getData().getTeams().get(match.getTeam2()).getStable().getNickname());
        group.add(rdbTeam2);
        panelMain.add(rdbTeam2);
        
        JPanel panelValidate = new JPanel();
        getContentPane().add(panelValidate);
        
        ControlerAddScore controler = new ControlerAddScore();
        
        JButton btnCancel = new JButton("Annuler");
        btnCancel.addActionListener(controler);
        btnCancel.setActionCommand("ADD_SCORE_CANCEL");
        panelValidate.add(btnCancel);
        
        JButton btnValidate = new JButton("Valider");
        btnValidate.addActionListener(controler);
        btnValidate.setActionCommand("ADD_SCORE_VALIDATE");
        panelValidate.add(btnValidate);
	}
	
	public JRadioButton getRdbTeam1() {
		return rdbTeam1;
	}
	
	public JRadioButton getRdbTeam2() {
		return rdbTeam2;
	}
	
	public TypesMatch getMatch() {
		return match;
	}
	
	
	

}
