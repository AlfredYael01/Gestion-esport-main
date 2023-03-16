package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esporter.both.types.TypesStable;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.Stables;

public class ControlerStableList extends MasterControler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String model = "STABLE_LIST RENDERER ";
		int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
		TypesStable stable = ((Stables)MasterFrame.getCurrentPanel()).getRenderer(id).getStable();
		MasterFrame.getInstance().setPanel(com.esporter.client.vue.StableInfo.class, stable);	
	}
	
}
