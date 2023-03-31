package com.esporter.client.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.esporter.client.vue.component.DatePicker;

public class ControlerDatePicker implements ActionListener{
	
	private DatePicker datePicker;

	public ControlerDatePicker(DatePicker d) {
		this.datePicker = d;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		switch(e.getActionCommand()) {
		case "DATEPICKER_NEXT_YEAR":
			datePicker.setYear(datePicker.getYear()+1);
			break;
		case "DATEPICKER_PREVIOUS_YEAR":
			datePicker.setYear(datePicker.getYear()-1);
			break;
		case "DATEPICKER_NEXT_MONTH":
			datePicker.setMonth(datePicker.getMonth()+1);
			break;
		case "DATEPICKER_PREVIOUS_MONTH":
			datePicker.setMonth(datePicker.getMonth()-1);
			break;
		case "DATEPICKER_RESET":
			datePicker.getLblSelectedDate().setText(null);
			datePicker.setSelectedButton(null);
			datePicker.setDaySelected(0);
			datePicker.setMonthSelected(0);
			datePicker.setYearSelected(0);
			break;
		case "DATEPICKER_DAY":
			JButton clickedOn = (JButton)e.getSource();
			if (!clickedOn.getText().isEmpty()) {
				datePicker.setSelectedButton(clickedOn);
				datePicker.setMonthSelected(datePicker.getMonth());
				datePicker.setYearSelected(datePicker.getYear());
				datePicker.setDaySelected(Integer.parseInt(clickedOn.getText()));
			}
			break;
		case "DATEPICKER_OK":
			datePicker.getD().dispose();
			break;
		}
		datePicker.displayDate();
		
	}
	
}
