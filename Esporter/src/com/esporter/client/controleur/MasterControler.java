package com.esporter.client.controleur;


import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.esporter.both.types.TypesMenu;
import com.esporter.client.model.user.User;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.DatePicker;
import com.esporter.client.vue.component.DatePicker.FilterDate;
import com.esporter.client.vue.error.Error;

public class MasterControler {

	private static MasterFrame masterInstance = null;
	private static User user;
	private static JDialog lastDialog = null;
	private static Error errorWindowBeforeConnection = null;
	
	public MasterControler(User user) {
		this.user = user;
		//state = OldControler.getInstance().getState();
		masterInstance = MasterFrame.getInstance();
	}
	
	public MasterControler() {
		this(user);
	}
	
	
	public static User getUser() {
		return user;
	}
	
	public static MasterFrame getMasterInstance() {
		return masterInstance;
	}


	public static JDialog getLastDialog() {
		return lastDialog;
	}


	public static Error getErrorWindowBeforeConnection() {
		return errorWindowBeforeConnection;
	}


	public static void setLastDialog(JDialog lastDialog) {
		MasterControler.lastDialog = lastDialog;
	}


	public static void setErrorWindowBeforeConnection(Error errorWindowBeforeConnection) {
		MasterControler.errorWindowBeforeConnection = errorWindowBeforeConnection;
	}
	
	//Fire an error, if the masterFrame is launched then fire it there
	//else, it open is own window to display the error
	public static void fireError(Exception e, boolean persistent, boolean critical) {
		if(masterInstance != null) {
			MasterFrame.getInstance().fireError(e, persistent, critical);
		}else {
			SwingUtilities.invokeLater(new Runnable(){
                public void run() {
					if (errorWindowBeforeConnection == null) {
						errorWindowBeforeConnection = new Error("Error", e.getMessage(),0);
					}else {
						errorWindowBeforeConnection.addMessage(e.getMessage());
						errorWindowBeforeConnection.addProgress(errorWindowBeforeConnection.getProgress()+20);
					}
                }
			});
		}
	}
	
	public static void setMenu(TypesMenu m) {
		masterInstance.setMenu(m);
	}
	
	public void closeDialog() {
		lastDialog=null;
	}
	
	public static void openDialog(JDialog jdiag) {
		lastDialog=jdiag;
	}
	
	public String openDatePicker(FilterDate... filterDates) {
		final JFrame f1 = new JFrame();
		return new DatePicker(f1, filterDates).setPickedDate();
	}
	
	
	
	
}
