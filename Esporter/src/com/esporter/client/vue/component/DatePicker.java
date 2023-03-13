package com.esporter.client.vue.component;

//import statements
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.esporter.client.controleur.ControlerDatePicker;
import com.esporter.client.vue.MasterFrame;

//create class
public class DatePicker 
{
	//Define the possible day to chose
	public enum FilterDate {BEFORE_TODAY, AFTER_TODAY, TODAY}

	//define variables
	int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
	int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
	int today = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);
	int monthToday = month;
	int yearToday = year;
	//create object of JLabel with alignment
	JLabel lblSelectedDate = new JLabel("", JLabel.CENTER);
	//define variable
	String day = "";
	//declaration
	JDialog d;
	int monthSelected = 0;
	int yearSelected = 0;
	int daySelected = 0;
	//create object of JButton
	JButton[] button = new JButton[49];

	JButton selectedButton = null;

	boolean AcceptBefore = true;
	boolean AcceptToday = true;
	boolean AcceptAfter = true;
	private JLabel lblCurrentYear;
	private JLabel lblCurrentMonth;
	private Calendar cal;

	public DatePicker(JFrame parent,String time, FilterDate... filterDates)//create constructor 
	{
		//create object
		for(FilterDate fd : filterDates) {
			if (fd == FilterDate.BEFORE_TODAY) {
				AcceptBefore = false;
			}else if (fd == FilterDate.TODAY) {
				AcceptToday = false;
			}else if (fd == FilterDate.AFTER_TODAY) {
				AcceptAfter = false;
			}

		}
		try {
			Timestamp ts = Timestamp.valueOf(time + " 00:00:00");
			Calendar cal = Calendar.getInstance(Locale.FRANCE);
			cal.setTimeInMillis(ts.getTime());
			daySelected = cal.get(Calendar.DAY_OF_MONTH);
			monthSelected = cal.get(Calendar.MONTH);
			yearSelected = cal.get(Calendar.YEAR);
		} catch (IllegalArgumentException e) {
			//The timestamp can't be parsed, it must be another string
		}
		

		d = new JDialog();
		d.setPreferredSize(new Dimension(700,250));
		//set modal true
		d.setModal(true);
		//define string
		String[] header = { "Dim.", "Lundi", "Mardi", "Merc.", "Jeudi", "Vend.", "Samedi" };

		//Create header that contains the year and the month
		JPanel head = new JPanel(new BorderLayout());
		JPanel years = new JPanel();
		JPanel months = new JPanel();

		head.add(years, BorderLayout.NORTH);
		years.setLayout(new GridLayout(1, 3, 0, 0));

		Component strutLeftYear = Box.createHorizontalStrut(20);
		years.add(strutLeftYear);

		JButton previousYear = new JButton("<< ann\u00E9e");
		years.add(previousYear);

		lblCurrentYear = new JLabel("New label");
		lblCurrentYear.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblCurrentYear.setHorizontalAlignment(SwingConstants.CENTER);
		years.add(lblCurrentYear);

		JButton nextYear = new JButton("ann\u00E9e >>");
		years.add(nextYear);

		Component strutRightYear = Box.createHorizontalStrut(20);
		years.add(strutRightYear);
		head.add(months, BorderLayout.SOUTH);
		months.setLayout(new GridLayout(1, 3, 20, 0));

		Component strutLeftMonth = Box.createHorizontalStrut(20);
		months.add(strutLeftMonth);

		JButton previousMonth = new JButton("<< mois");
		months.add(previousMonth);



		lblCurrentMonth = new JLabel("New label");
		lblCurrentMonth.setHorizontalAlignment(SwingConstants.CENTER);
		months.add(lblCurrentMonth);

		JButton nextMonth = new JButton("mois >>");
		months.add(nextMonth);

		Component strutRightMonth = Box.createHorizontalStrut(20);
		months.add(strutRightMonth);

		d.getContentPane().add(head, BorderLayout.NORTH);

		Component verticalStrut = Box.createVerticalStrut(5);
		head.add(verticalStrut, BorderLayout.CENTER);

		ControlerDatePicker controler = new ControlerDatePicker(this);

		previousYear.addActionListener(controler);
		previousYear.setActionCommand("DATEPICKER_PREVIOUS_YEAR");

		nextYear.addActionListener(controler);
		nextYear.setActionCommand("DATEPICKER_NEXT_YEAR");

		previousMonth.addActionListener(controler);
		previousMonth.setActionCommand("DATEPICKER_PREVIOUS_MONTH");

		nextMonth.addActionListener(controler);
		nextMonth.setActionCommand("DATEPICKER_NEXT_MONTH");

		//create JPanel object and set layout
		JPanel p1 = new JPanel(new GridLayout(7, 7));
		//set size
		p1.setPreferredSize(new Dimension(430, 120));
		//for loop condition
		for (int x = 0; x < button.length; x++) 
		{		
			//define variable
			final int selection = x;
			//create object of JButton
			button[x] = new JButton();
			//set focus painted false
			button[x].setFocusPainted(false);
			//set background colour
			button[x].setBackground(Color.white);
			//if loop condition
			if (x > 6)
				//add action listener
				button[x].addActionListener(controler);
			button[x].setActionCommand("DATEPICKER_DAY");
			if (x < 7)//if loop condition 
			{
				button[x].setText(header[x]);
				//set fore ground colour
				button[x].setForeground(Color.red);
			}
			p1.add(button[x]);//add button
		}
		//create JPanel object with grid layout
		JPanel p2 = new JPanel(new GridLayout(1, 3));
		p2.add(lblSelectedDate);//add label
		//create object of button for next month
		JButton btnOK = new JButton("Accepter");
		//add action command
		btnOK.addActionListener(controler);
		btnOK.setActionCommand("DATEPICKER_OK");

		JButton reset = new JButton("Remettre a z\u00E9ro");
		reset.addActionListener(controler);
		reset.setActionCommand("DATEPICKER_RESET");

		p2.add(reset);
		p2.add(btnOK);// add next button
		//set border alignment
		d.getContentPane().add(p1, BorderLayout.CENTER);
		d.getContentPane().add(p2, BorderLayout.SOUTH);
		d.pack();
		//set location
		d.setLocationRelativeTo(parent);
		//call method
		displayDate();
		//set visible true
		d.setVisible(true);
	}

	public void displayDate() {
		for (int x = 7; x < button.length; x++) //for loop
			button[x].setText("");//set text
		java.text.SimpleDateFormat sdfMonth = new java.text.SimpleDateFormat("MMMM", Locale.FRANCE);	
		java.text.SimpleDateFormat sdfYear = new java.text.SimpleDateFormat("yyyy", Locale.FRANCE);
		//create object of SimpleDateFormat 
		cal = java.util.Calendar.getInstance(Locale.FRANCE);	

		//create object of java.util.Calendar 
		cal.set(year, month, 1); //set year, month and date

		java.util.Calendar calToday = java.util.Calendar.getInstance();
		calToday.set(yearToday, monthToday, today);
		//define variables
		int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
		int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
		//condition
		for (int x=0; x<=6+daysInMonth; x++) {
			button[x].setBackground(Color.white);
		}
		for (int x=daysInMonth; x<button.length; x++) {
			button[x].setBackground(Color.white);
		}
		for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
			cal.set(year, month, day);
			if(calToday.compareTo(cal) == 0) {
				if(!AcceptToday) {
					button[x].setEnabled(false);
				}else {
					button[x].setEnabled(true);
				}
				button[x].setBackground(MasterFrame.COLOR_MASTER);
			}else {
				if(calToday.compareTo(cal) > 0 && !AcceptAfter) {
					button[x].setEnabled(false);
				}else if (calToday.compareTo(cal) < 0 && !AcceptBefore) {
					button[x].setEnabled(false);
				}else {
					button[x].setEnabled(true);
				}

				button[x].setBackground(Color.white);
			}
			if(year==yearSelected && month==monthSelected && day==daySelected) {
				button[x].setBackground(Color.red.brighter());
			}

			button[x].setText("" + day);
		}
		//If a date has been selected
		if(daySelected != 0) {
			Calendar calSelected = Calendar.getInstance(Locale.FRANCE);
			calSelected.set(yearSelected, monthSelected, daySelected);
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);
			lblSelectedDate.setText("Date selectionnée : \n"+sdf.format(calSelected.getTime()));
		} else {
			lblSelectedDate.setText("Vous n'avez selectionné aucune date.");
		}
		//set text
		lblCurrentMonth.setText(sdfMonth.format(cal.getTime()));
		lblCurrentYear.setText(sdfYear.format(cal.getTime()));


		//set title
		d.setTitle("Choix de la date");
	}

	public String setPickedDate() {
		if(daySelected==0) {
			return "Sélectionnez une date";
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(yearSelected, monthSelected, daySelected);
		return sdf.format(cal.getTime());
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return month;
	}

	public JLabel getLblSelectedDate() {
		return lblSelectedDate;
	}

	public JButton getSelectedButton() {
		return selectedButton;
	}

	public void setMonthSelected(int monthSelected) {
		this.monthSelected = monthSelected;
	}

	public void setYearSelected(int yearSelected) {
		this.yearSelected = yearSelected;
	}

	public void setSelectedButton(JButton selectedButton) {
		this.selectedButton = selectedButton;
	}

	public Calendar getCal() {
		return cal;
	}

	public int getDaySelected() {
		return daySelected;
	}

	public void setDaySelected(int daySelected) {
		this.daySelected = daySelected;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	public JDialog getD() {
		return d;
	}




}