package com.esporter.client.vue.organizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ComboBoxUI;

import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesTournament;
import com.esporter.client.controleur.ControlerCalendar;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.component.ComboBoxRendererArrow;
import com.esporter.client.vue.component.DataJPanel;
import com.esporter.client.vue.component.RendererCalendar;

public class Calendar extends DataJPanel implements com.esporter.client.vue.component.Calendar{
	/**
	 * 
	 */
	private static final long serialVersionUID = 824169066451626109L;
	private JTextField txtDate;
	private JTextField txtCalendar;
	private JPanel panelTournaments;
	private JLabel lblNoTournament = new JLabel();
	private Timestamp dateFilter =null;
	private TypesGame gameFilter = null;
	private List<RendererCalendar> renderer;
	private JComboBox<TypesGame> comboBoxFilterGame;

	
	public void createListTournament(Timestamp date, TypesGame game) {
        // create List model
		panelTournaments.removeAll();
		dateFilter = date;
		gameFilter = game;
		renderer = new LinkedList<RendererCalendar>();
		List<TypesTournament> tournamentFiltered = MasterFrame.getInstance().getUser().getData().listFilteredTournament(date, game);
		System.out.println(tournamentFiltered.isEmpty());
		if(tournamentFiltered.isEmpty() || tournamentFiltered == null ) {
			System.out.println("isEmpty");
			lblNoTournament.setText("Il n'existe aucun tournoi correspondant aux critères recherchés");
			lblNoTournament.setForeground(MasterFrame.COLOR_TEXT);
			lblNoTournament.setFont(new Font("Cambria", Font.PLAIN , 20));
			panelTournaments.add(lblNoTournament);
		} else {
			int id=0;
			for (TypesTournament t : tournamentFiltered) {
				RendererOrganizerCalendar rend = new RendererOrganizerCalendar(t,id);
				panelTournaments.add(rend);
				renderer.add(rend);
				id++;
			}
			

		}
		        
        panelTournaments.setLayout(new GridLayout(0, 1));

		
		//HashMap<Integer, TournoiInfo> map = Data.getCalendrier();

	
	}	
	/**
	 * Create the application.
	 * @param tournoi 
	 */
	public Calendar() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panelTournaments = new JPanel();
		panelTournaments.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		setLayout(new BorderLayout(0, 0));
		JPanel panelRoot = new JPanel();
		panelRoot.setLayout(new BorderLayout());
		add(panelRoot, BorderLayout.CENTER);
		JPanel panelTitle = new JPanel();
		panelTitle.setPreferredSize(new Dimension(5, 70));
		panelTitle.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		panelTitle.setBorder(null);
		panelRoot.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		add(panelTitle, BorderLayout.NORTH);
		
		txtCalendar = new JTextField();
		txtCalendar.setHorizontalAlignment(SwingConstants.CENTER);
		txtCalendar.setPreferredSize(new Dimension(5, 50));
		txtCalendar.setEditable(false);
		txtCalendar.setForeground(MasterFrame.COLOR_TEXT);
		txtCalendar.setBorder(new EmptyBorder(20, 0, 0, 0));
		txtCalendar.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		txtCalendar.setText("Calendrier des tournois");
		txtCalendar.setFont(new Font("Cambria", Font.BOLD, 40));
		panelTitle.add(txtCalendar);
		txtCalendar.setColumns(20);
		
		createListTournament(null, null);
		JScrollPane scrollPaneCalendar = new JScrollPane(panelTournaments);
		scrollPaneCalendar.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		scrollPaneCalendar.setBorder(new EmptyBorder(50, 100, 50, 100));
		panelRoot.add(scrollPaneCalendar, BorderLayout.CENTER);
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(100, 100, 0, 100));
		panel.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		panelRoot.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 3, 200, 0));
		
		JPanel panelDateFilter = new JPanel();
		panel.add(panelDateFilter);
		panelDateFilter.setLayout(new BorderLayout(0, 0));
		
		txtDate = new JTextField("Selectionnez une date");
		txtDate.setEditable(false);
		txtDate.setForeground(MasterFrame.COLOR_TEXT);
		txtDate.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		txtDate.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)), new EmptyBorder(0, 10, 0, 0)));
		txtDate.setFont(new Font("Cambria", Font.PLAIN, 15));
		
		txtDate.setColumns(10);
		
		/*txtDate.getDocument().addDocumentListener(new DocumentListener() {


			@Override
			public void insertUpdate(DocumentEvent e) {
				
				System.out.println("ok1");
				createListTournament(Date.valueOf(txtDate.getText()), gameFilter);
				revalidate();
				validate();
				repaint();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			});*/
		panelDateFilter.add(txtDate, BorderLayout.CENTER);
		
		ControlerCalendar controler = new ControlerCalendar();
		
		JButton btnMoreDate = new JButton(" ... ");
		btnMoreDate.setBackground(MasterFrame.COLOR_MASTER);
		btnMoreDate.setForeground(MasterFrame.COLOR_TEXT);
		btnMoreDate.setFont(new Font("Cambria", Font.PLAIN, 15));
		btnMoreDate.setBorder(null);
		btnMoreDate.addActionListener(controler);
		btnMoreDate.setActionCommand("CALENDAR_DATE");
		panelDateFilter.add(btnMoreDate, BorderLayout.EAST);

		
		JPanel panelGameFilter = new JPanel();
		panelGameFilter.setBorder(null);
		panel.add(panelGameFilter);
		panelGameFilter.setLayout(new BorderLayout(0, 0));
		
		comboBoxFilterGame = new JComboBox<>(TypesGame.values());
		comboBoxFilterGame.setUI((ComboBoxUI) ComboBoxRendererArrow.createUI(comboBoxFilterGame));
		comboBoxFilterGame.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
		comboBoxFilterGame.setFont(new Font("Cambria", Font.PLAIN, 15));
		comboBoxFilterGame.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		comboBoxFilterGame.setForeground(MasterFrame.COLOR_TEXT);
		
		/*comboBoxFilterGame.addItemListener(new ItemListener() {
	        @Override
	        public void itemStateChanged(ItemEvent e) {
	            if(e.getStateChange() == ItemEvent.SELECTED) {
	                gameFilter = (TypesGame) comboBoxFilterGame.getSelectedItem();
	                createListTournament(dateFilter,gameFilter);
	            }
	        }
	    });
		
		((JTextField) comboBoxFilterGame.getEditor().getEditorComponent()).getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				createListTournament(dateFilter,gameFilter);
				revalidate();
				validate();
				repaint();	
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				
			}
			
		});*/
		comboBoxFilterGame.insertItemAt(null, 0);
		comboBoxFilterGame.setSelectedIndex(0);
		comboBoxFilterGame.addActionListener(controler);
		comboBoxFilterGame.setActionCommand("CALENDAR_GAMECOMBO");
		
		panelGameFilter.add(comboBoxFilterGame);
		
		
		JPanel panelAddTournament = new JPanel();
		panelAddTournament.setBorder(null);
		panel.add(panelAddTournament);
		panelAddTournament.setLayout(new BorderLayout(0, 0));
		panelAddTournament.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);

		
		JButton btnAddTournament = new JButton("Creer un tournois");
		btnAddTournament.setBackground(MasterFrame.COLOR_MASTER);
		btnAddTournament.setFont(new Font("Cambria", Font.PLAIN, 15));
		btnAddTournament.setForeground(MasterFrame.COLOR_TEXT_MENU);
		btnAddTournament.addActionListener(controler);
		btnAddTournament.setActionCommand("CALENDAR_ADD");
		panelAddTournament.add(btnAddTournament);
		
		

	}
	@Override
	public void dataUpdate() {
		createListTournament(dateFilter, gameFilter);
		
	}
	
	public RendererCalendar getRenderer(int id) {
		return renderer.get(id);
	}
	
	public JTextField getTxtDate() {
		return txtDate;
	}
	
	public TypesGame getGame() {
		return gameFilter;
	}
	
	public Timestamp getChosenDate() {
		return dateFilter;
	}
	
	public JComboBox<TypesGame> getComboBoxFilterGame() {
		return comboBoxFilterGame;
	}
	
	@Override
	public void setGame(TypesGame game) {
		
		
	}
	@Override
	public void setChosenDate(Timestamp chosenDate) {
		
		
	}
	

	

}

