package com.esporter.client.controleur;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.esporter.both.data.Data;
import com.esporter.both.socket.Response;
import com.esporter.both.types.TypesFame;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesImage;
import com.esporter.both.types.TypesLogin;
import com.esporter.both.types.TypesMatch;
import com.esporter.both.types.TypesMenu;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesRegisterPlayer;
import com.esporter.both.types.TypesRegisterTeam;
import com.esporter.both.types.TypesStable;
import com.esporter.both.types.TypesTeam;
import com.esporter.both.types.TypesTournament;
import com.esporter.both.types.exception.ExceptionInvalidPermission;
import com.esporter.both.types.exception.ExceptionTeamNotFull;
import com.esporter.client.model.user.User;
import com.esporter.client.vue.Calendar;
import com.esporter.client.vue.CalendarAndScoreMatch;
import com.esporter.client.vue.LogIn;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.ProgramMatchs;
import com.esporter.client.vue.Ranking;
import com.esporter.client.vue.SeePlayerInfos;
import com.esporter.client.vue.StableInfo;
import com.esporter.client.vue.Stables;
import com.esporter.client.vue.component.ContainerModifyPlayer;
import com.esporter.client.vue.component.ContainerPlayer;
import com.esporter.client.vue.component.DatePicker;
import com.esporter.client.vue.component.DatePicker.FilterDate;
import com.esporter.client.vue.component.RendererProgramMatch;
import com.esporter.client.vue.organizer.CreateTournament;
import com.esporter.client.vue.organizer.EditTournament;
import com.esporter.client.vue.player.RegisterTournament;
import com.esporter.client.vue.referee.SetScore;
import com.esporter.client.vue.stable.AddPlayer;
import com.esporter.client.vue.stable.ModifyPlayer;
import com.esporter.client.vue.stable.ModifyTeam;
import com.esporter.client.vue.stable.RegisterStable;
import com.esporter.client.vue.stable.TeamManagement;
import com.esporter.client.vue.stable.management.AddTeam;
import com.esporter.client.vue.error.Error;

public class Controler implements ActionListener, MouseListener, KeyListener{

	private static Controler instance = null;
	private MasterFrame masterInstance = null;
	private User user;
	private State state = null;
	private JDialog lastDialog = null;
	private State stateBeforeLogin;
	private State stateBeforeError;
	private State stateBefore;
	private Error errorWindowBeforeConnection = null;
	
	
	private Controler(){
		instance = this;
		try {
			user = new User();
			SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                	masterInstance = MasterFrame.getInstance();
                }
			});
		} catch (UnknownHostException e) {
			System.out.println("Impossible de se connecter au server");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Controler getInstance() {
		if (instance==null)
			instance = new Controler();
		return instance;
	}
	
	public void dataUpdate() {
		MasterFrame.getInstance().dataUpdate();
	}
	
	public void setMenu(TypesMenu type) {
		MasterFrame.getInstance().setMenu(type);
	}
	
	public void fireError(Exception e, boolean persistent, boolean critical) {
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
	
	public void openError() {
		stateBeforeError = state;
		
		state = State.ERROR;
	}
	
	public void openLogin() {
		stateBeforeLogin = state;
		
		state = State.LOGIN;
	}
	
	public void closeError() {
		state = stateBeforeError;
		
	}
	
	public void closeLogin() {
		state = stateBeforeLogin;
		
	}
	
	public Data getData() {
		return user.getData();
	}
	
	public User getUser() {
		return user;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	

	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////		 ACTION LISTENER PART 		/////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contains("menu")) {
			if (user.getPermission() == TypesPermission.STABLE && e.getActionCommand().equals("menuStableManagement")) {
				MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.TeamManagement.class, null);
				state = State.STABLE_MANAGEMENT;
			} else if (e.getActionCommand().equals("menuCalendar")) {
				if(getUser().getPermission() == TypesPermission.ORGANIZER) {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
				} else {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.Calendar.class, getUser().getPermission());
				}
				
				
				state = State.CALENDAR;
			} else if (e.getActionCommand().equals("menuRanking")) {
				MasterFrame.getInstance().setPanel(com.esporter.client.vue.Ranking.class, null);
				state = State.RANKING;
			} else if (e.getActionCommand().equals("menuStable")) {
				MasterFrame.getInstance().setPanel(com.esporter.client.vue.Stables.class, null);
				state = State.STABLE_LIST;
			} else if (e.getActionCommand().equals("menuLogin")) {
				if(getUser().getPermission()!=TypesPermission.VISITOR) {
					//LOGGED IN
					getUser().logout();
					setMenu(TypesMenu.VISITOR);
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.Calendar.class, getUser().getPermission());
					state = State.CALENDAR;
				} else {
					//NOT LOG IN
					MasterFrame.getInstance().getMain().setVisible(false);
					MasterFrame.getInstance().getError().setVisible(false);
					MasterFrame.getInstance().getLoginPage().setVisible(true);
					MasterFrame.getInstance().getLoginPage().getTxtUsername().requestFocus();
					openLogin();
				}
			}
		} else {
			
			switch(state) {
			case ADD_PLAYER:
				AddPlayer jd = (AddPlayer)lastDialog;
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
					}else if(this.getUser().chckUsernameUsed(jd.getTxtUsername().getText()).equals("1")){
						JOptionPane.showMessageDialog(null, "Le nom d'utilisateur est déjà utilisé", "Erreur", JOptionPane.ERROR_MESSAGE);
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
							com.esporter.both.types.TypesImage im = new com.esporter.both.types.TypesImage(image, "png");
							TypesPlayer joueur = new TypesPlayer(-1,name, firstname,im,birthDate,contractStartDate,contractEndDate,1,-1,((TypesStable)MasterFrame.getInstance().getUser().getInfo()).getId(), jd.getTxtUsername().getText());
							TypesLogin l = new TypesLogin(jd.getTxtUsername().getText(), jd.getTxtPassword().getText());
							jd.getContainer().setPlayer(new TypesRegisterPlayer(joueur, l));
							jd.dispose();
							closeDialog();
						} catch (IOException e1) {
							fireError( new IllegalArgumentException("Il y a une erreur avec la photo"),false, false);
						} 
						
					}
					break;
				case "ADD_PLAYER_MORE1":
					final JFrame f = new JFrame();
					//set text which is collected by date picker i.e. set date 
					jd.getTxtBirthDate().setText(new DatePicker(f, FilterDate.BEFORE_TODAY).setPickedDate());
					break;
				case "ADD_PLAYER_MORE2":
					//create frame new object  f
					final JFrame f1 = new JFrame();
					//set text which is collected by date picker i.e. set date 
					jd.getTxtEndContractDate().setText(new DatePicker(f1).setPickedDate());
					break;
				case "ADD_PLAYER_MORE3":
					//create frame new object  f
					final JFrame f2 = new JFrame();
					//set text which is collected by date picker i.e. set date 
					jd.getTxtStartContractDate().setText(new DatePicker(f2).setPickedDate());
					break;
					
				}
				break;
			case ADD_TEAM:
				switch(e.getActionCommand()) {
				case "ADD_TEAM_COMBOGAME":
					MasterFrame.getInstance().setPanel(AddTeam.class, ((AddTeam)MasterFrame.getCurrentPanel()).getComboBox().getSelectedItem());
					break;
				case "ADD_TEAM_VALIDATE":
					JComboBox<TypesGame> jcombo = ((AddTeam)MasterFrame.getCurrentPanel()).getComboBox();
					for (ContainerPlayer c : ((AddTeam)MasterFrame.getCurrentPanel()).getPlayerList()) {
						if (c.getPlayer()==null) {
							fireError(new ExceptionTeamNotFull("Erreur de creation de l'équipe"),false,  false);
							return;
						}
					}
					ArrayList<TypesRegisterPlayer> players = new ArrayList<>();
					for (ContainerPlayer c : ((AddTeam)MasterFrame.getCurrentPanel()).getPlayerList()) {
						players.add(c.getPlayer());
					}
					
					TypesRegisterTeam team = new TypesRegisterTeam((TypesGame)jcombo.getSelectedItem(), ((TypesStable)Controler.getInstance().getUser().getInfo()).getId(), players);
					MasterFrame.getInstance().getUser().addTeam(team);
					if (MasterFrame.getInstance().getUser().getWaiting().getActualState()==Response.UPDATE_TEAM) {
						MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.TeamManagement.class, null);
						JOptionPane.showMessageDialog(null, "L'équipe a bien été ajoutée", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					break;
				}
				break;
			case ADD_TOURNAMENT:
				CreateTournament create = ((CreateTournament)MasterFrame.getCurrentPanel());
				switch(e.getActionCommand()) {
				case "ADD_TOURNAMENT_YES":
					try {
						if(create.getTxtDateStartTournament().getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Veuillez préciser la date de début du tournoi","Error", JOptionPane.ERROR_MESSAGE);
						}else if(create.getTxtTournamentName().getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Veuillez préciser le nom du tournoi","Error", JOptionPane.ERROR_MESSAGE);
						}else {
							Timestamp tournamentStart = null;
							try {
								tournamentStart = Timestamp.valueOf(create.getTxtDateStartTournament().getText() + " 00:00:00");
							} catch (IllegalArgumentException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "Format de date invalide","Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							int n = JOptionPane.showConfirmDialog(null, "Confirmez vous l'ajout du tournoi ?","Confirmation", JOptionPane.YES_NO_OPTION);
							if (n == JOptionPane.YES_OPTION) {
								TypesTournament t = new TypesTournament(tournamentStart, create.getTxtTournamentName().getText(), 
										(TypesFame) create.getComboBoxFame().getSelectedItem(), (TypesGame) create.getComboBoxGame().getSelectedItem(), -1);
								if (MasterFrame.getInstance().getUser().getData().listSortedTournaments().contains(t)) { 
									JOptionPane.showMessageDialog(null, "Un tournoi à cette date existe déjà","Error", JOptionPane.ERROR_MESSAGE);
								}else{
									MasterFrame.getInstance().getUser().addTournament(t);
									MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
									state = State.CALENDAR;
								}
							}
						}
					} catch (ExceptionInvalidPermission e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case "ADD_TOURNAMENT_DATE":
					//create frame new object  f
					final JFrame f = new JFrame();
					//set text which is collected by date picker i.e. set date 
					create.getTxtDateStartTournament().setText(new DatePicker(f).setPickedDate());
					break;
				case "ADD_TOURNAMENT_CANCEL":
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
					state = State.CALENDAR;
					break;
				}
				break;
			case MODIFY_TOURNAMENT:
				switch(e.getActionCommand()) {
				case "MODIFY_TOURNAMENT_DATE":
					//create frame new object  f
					final JFrame f = new JFrame();
					//set text which is collected by date picker i.e. set date 
					((EditTournament)MasterFrame.getCurrentPanel()).getTxtDateStartTournament().setText(new DatePicker(f).setPickedDate());
					break;
				case "MODIFY_TOURNAMENT_VALIDATE":
					try {
						EditTournament editPage = ((EditTournament)MasterFrame.getCurrentPanel());
						if(editPage.getTxtDateStartTournament().getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Veuillez préciser la date de début du tournoi","Error", JOptionPane.ERROR_MESSAGE);
						}else if(editPage.getTxtTournamentName().getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Veuillez préciser le nom du tournoi","Error", JOptionPane.ERROR_MESSAGE);
						}else {
							Timestamp tournamentStart = null;
							try {
								tournamentStart = Timestamp.valueOf(editPage.getTxtDateStartTournament().getText() + " 00:00:00");
							} catch (IllegalArgumentException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "Format de date invalide","Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							int n = JOptionPane.showConfirmDialog(null, "Confirmez vous la modification du tournoi ?","Confirmation", JOptionPane.YES_NO_OPTION);
							if (n == JOptionPane.YES_OPTION) {
								TypesTournament t = new TypesTournament(tournamentStart, editPage.getTxtTournamentName().getText(), 
										(TypesFame) editPage.getComboBoxFame().getSelectedItem(), (TypesGame) editPage.getComboBoxGame().getSelectedItem(),editPage.getTournament().getId());
								TypesTournament dummyDate = new TypesTournament(tournamentStart, null, null,null, -1);
								if (MasterFrame.getInstance().getUser().getData().listSortedTournaments().contains(t) && !(MasterFrame.getInstance().getUser().getData().listSortedTournaments().get(MasterFrame.getInstance().getUser().getData().listSortedTournaments().indexOf(dummyDate)).getId() == t.getId())) { 
									JOptionPane.showMessageDialog(null, "Un tournoi à cette date existe déjà","Error", JOptionPane.ERROR_MESSAGE);
								}else{
									MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
									MasterFrame.getInstance().getUser().modifyTournament(t);
									state = State.CALENDAR;
								}
							}
						}
					} catch (ExceptionInvalidPermission e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case "MODIFY_TOURNAMENT_CANCEL":
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.Calendar.class, null);
					state = State.CALENDAR;
					break;
				}
				break;
			case CALENDAR:
				com.esporter.client.vue.component.Calendar cal;
				if(MasterFrame.getCurrentPanel() instanceof com.esporter.client.vue.organizer.Calendar) {
					cal = ((com.esporter.client.vue.organizer.Calendar)MasterFrame.getCurrentPanel());
				} else {
					cal = ((Calendar)MasterFrame.getCurrentPanel());
				}
				
				switch(e.getActionCommand()) {
				case "CALENDAR_DATE":
					//create frame new object  f
					final JFrame f = new JFrame();
					//set text which is collected by date picker i.e. set date 
					cal.getTxtDate().setText(new DatePicker(f).setPickedDate());
					MasterFrame.getInstance().getMain().repaint();
	                MasterFrame.getInstance().getMain().revalidate();
	                if(!cal.getTxtDate().getText().equals("")) {
						Timestamp t = Timestamp.valueOf(cal.getTxtDate().getText() + " 00:00:00");
						System.out.println(t.toString());
						cal.setChosenDate(t);
						cal.createListTournament(t,cal.getGame() );
	                } else {
	                	cal.createListTournament(null,cal.getGame() );
	                }
	                MasterFrame.getInstance().getMain().repaint();
	                MasterFrame.getInstance().getMain().revalidate();
					break;
				case "CALENDAR_GAMECOMBO":
	                cal.createListTournament(cal.getChosenDate(),(TypesGame)cal.getComboBoxFilterGame().getSelectedItem());
	                ((JPanel)cal).repaint();
	                ((JPanel)cal).revalidate();
					break;
				case "CALENDAR_ADD":
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.CreateTournament.class,null);
					state = State.ADD_TOURNAMENT;
					break;
				default:
					if(e.getActionCommand().contains("RENDERER")) {
						String model = null;
						if(e.getActionCommand().contains("MODIFY")) {
							model = "CALENDAR RENDERER MODIFY ";
						}else if(e.getActionCommand().contains("REMOVE")) {
							model = "CALENDAR RENDERER REMOVE ";
						} else if(e.getActionCommand().contains("INSCRIPTION")) {
							model = "CALENDAR RENDERER INSCRIPTION ";
						} else if(e.getActionCommand().contains("SCORE")) {
							model = "CALENDAR RENDERER SCORE ";
						} else if(e.getActionCommand().contains("POOL")) {
							model = "CALENDAR RENDERER POOL ";
						}
						int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
						TypesTournament tournament;
						if(MasterFrame.getCurrentPanel() instanceof com.esporter.client.vue.organizer.Calendar) {
							tournament = ((com.esporter.client.vue.organizer.Calendar)MasterFrame.getCurrentPanel()).getRenderer(id).getTournament();
						} else {
							tournament = ((Calendar)MasterFrame.getCurrentPanel()).getRenderer(id).getTournament();
						}
						 
						//MasterFrame.getInstance().setPanel(vue.SeePlayerInfos.class, team);
						
						if(e.getActionCommand().contains("MODIFY")) {
							MasterFrame.getInstance().setPanel(com.esporter.client.vue.organizer.EditTournament.class, tournament);
							state = State.MODIFY_TOURNAMENT;
						}else if(e.getActionCommand().contains("REMOVE")) {
							int n = JOptionPane.showConfirmDialog (null, "Etes vous sur de vouloir supprimer le tournoi?","WARNING", JOptionPane.YES_NO_OPTION);
							if(n== JOptionPane.YES_OPTION) {
								MasterFrame.getInstance().getUser().deleteTournament(tournament);
							}
						} else if(e.getActionCommand().contains("INSCRIPTION")) {
							JDialog confirmation = new RegisterTournament(tournament.getId(), tournament);
							
						} else if(e.getActionCommand().contains("SCORE")) {
							System.out.println("NOT YET IMPLEMENTED");
						} else if(e.getActionCommand().contains("POOL")) {
							MasterFrame.getInstance().setPanel(com.esporter.client.vue.ProgramMatchs.class, tournament);
							state = State.POOL;
						}
					}
					break;
				}
				break;
			case INSCRIPTION:
				RegisterTournament reg = (RegisterTournament)lastDialog;
				switch(e.getActionCommand()) {
				case "INSCRIPTION_YES":
					if(!reg.getTournament().getRegistered().contains(((TypesPlayer)MasterFrame.getInstance().getUser().getInfo()).getIdTeam())){
						try {
							MasterFrame.getInstance().getUser().registerTournament(reg.getTournament().getId());
							reg.dispose();
						} catch (ExceptionInvalidPermission e1) {
							
							e1.printStackTrace();
						}
					}else {
						try {
							MasterFrame.getInstance().getUser().unregisterTournament(reg.getTournament().getId(), TypesGame.gameToInt(reg.getTournament().getGame()));
						} catch (ExceptionInvalidPermission e1) {
							e1.printStackTrace();
						};
						reg.dispose();
					}
					break;
				case "INSCRIPTION_NO":
					reg.dispose();
					break;
				}
				break;
			case ERROR:
				if(e.getActionCommand().equals("ERROR_CONTINUE")) {
					if(MasterFrame.getInstance().getError().isCritical()) {
						MasterFrame.getInstance().getFrame().dispose();
						System.exit(-2);
					}
					MasterFrame.getInstance().getError().setVisible(false);
					MasterFrame.getInstance().getError().setException(null);
					closeError();
				}
				break;
			case HOME_ORGANIZER:
				break;
			case HOME_PLAYER:
				break;
			case HOME_REFEREE:
				break;
			case HOME_STABLE:
				break;
			case HOME_VISITOR:
				break;
			case LOGIN:
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
					closeLogin();
					break;
				case "LOGIN_REGISTER_STABLE":
					MasterFrame.getInstance().getLoginPage().setVisible(false);
					MasterFrame.getInstance().getMain().setVisible(true);
					closeLogin();
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.RegisterStable.class, null);
					state = State.REGISTER_STABLE;
					break;
				}
				
				break;
			case MODIFY_PLAYER:
				ModifyPlayer mp = (ModifyPlayer)lastDialog;
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
							com.esporter.both.types.TypesImage im = new com.esporter.both.types.TypesImage(image, "png");
							TypesPlayer joueur = new TypesPlayer(mp.getContainer().getPlayer().getId(),name, firstname,im,birthDate,contractStartDate,contractEndDate,1,mp.getContainer().getPlayer().getIdTeam(),((TypesStable)MasterFrame.getInstance().getUser().getInfo()).getId(), mp.getContainer().getPlayer().getUsername());
							mp.getContainer().setPlayer(joueur);
							mp.dispose();
						} catch (IOException e1) {
							fireError( new IllegalArgumentException("Il y a une erreur avec la photo"), false, false);
						} 
					}
					break;
				case "ADD_PLAYER_MORE1":
					final JFrame f = new JFrame();
					//set text which is collected by date picker i.e. set date 
					mp.getTxtBirthDate().setText(new DatePicker(f).setPickedDate());
					break;
				case "ADD_PLAYER_MORE2":
					//create frame new object  f
					final JFrame f1 = new JFrame();
					//set text which is collected by date picker i.e. set date 
					mp.getTxtEndContractDate().setText(new DatePicker(f1).setPickedDate());
					break;
				case "ADD_PLAYER_MORE3":
					//create frame new object  f
					final JFrame f2 = new JFrame();
					//set text which is collected by date picker i.e. set date 
					mp.getTxtStartContractDate().setText(new DatePicker(f2).setPickedDate());
					break;			
					
				}
				break;
			case MODIFY_TEAM:
				switch(e.getActionCommand()) {
				case "MODIFY_TEAM_VALIDATE":
					for (ContainerModifyPlayer c : ((ModifyTeam)MasterFrame.getCurrentPanel()).getPlayerList()) {
						if (c.getPlayer()==null) {
							fireError(new ExceptionTeamNotFull("Erreur de modification de l'équipe"), false, false);
							return;
						}
					}
					HashMap<Integer,TypesPlayer> players = new HashMap<>();
					for (ContainerModifyPlayer c : ((ModifyTeam)MasterFrame.getCurrentPanel()).getPlayerList()) {
						System.out.println(c.getPlayer().getId());
						players.put(c.getPlayer().getId(),c.getPlayer());
					}
					TypesTeam previousTeam = ((ModifyTeam)MasterFrame.getCurrentPanel()).getTeam();
					TypesTeam newTeam = new TypesTeam(previousTeam.getGame(), previousTeam.getStable(), players, previousTeam.getId());
					MasterFrame.getInstance().getUser().modifyTeam(newTeam);
					if (MasterFrame.getInstance().getUser().getWaiting().getActualState()==Response.UPDATE_TEAM) {
						MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.TeamManagement.class, null);
						state = State.STABLE_MANAGEMENT;
					}
					break;
				}
				break;
			case PLAYER_INFO:
				switch(e.getActionCommand()) {
				case "PLAYER_INFO_STABLE":
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.Stables.class, null);
					state = State.STABLE_LIST;
					break;
				case "PLAYER_INFO_TEAM":
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.StableInfo.class, ((SeePlayerInfos)MasterFrame.getCurrentPanel()).getTeam().getStable());
					state = State.STABLE_INFO;
					break;
				}
				break;
			case RANKING:
				switch(e.getActionCommand()) {
				case "RANKING_COMBOGAME":
					((Ranking)MasterFrame.getCurrentPanel()).createListRenderer((TypesGame)((Ranking)MasterFrame.getCurrentPanel()).getComboBoxGame().getSelectedItem());
					break;
				}
				break;
			case STABLE_INFO:
				if(e.getActionCommand().contains("RENDERER")) {
					String model = "STABLE_INFO RENDERER ";
					int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
					TypesTeam team = ((StableInfo)MasterFrame.getCurrentPanel()).getRenderer(id).getTeam();
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.SeePlayerInfos.class, team);
					state = State.PLAYER_INFO;
				}else {
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.Stables.class, null);
					state = State.STABLE_LIST;
				}
				break;
			case STABLE_LIST:
				if(e.getActionCommand().contains("RENDERER")) {
					String model = "STABLE_LIST RENDERER ";
					int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
					TypesStable stable = ((Stables)MasterFrame.getCurrentPanel()).getRenderer(id).getStable();
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.StableInfo.class, stable);
					state = State.STABLE_INFO;
				}
				break;
			case STABLE_MANAGEMENT:
				switch(e.getActionCommand()) {
				case "STABLE_MANAGEMENT_ADDTEAM":
					TypesGame game = (TypesGame)((TeamManagement)MasterFrame.getCurrentPanel()).getComboBoxFilterGame().getSelectedItem();
					if (game == null) {
						game = TypesGame.intToGame(1); 
					}
					MasterFrame.getInstance().setPanel(AddTeam.class, game);
					state = State.ADD_TEAM;
					break;
				case "STABLE_MANAGEMENT_COMBO":
					((TeamManagement)MasterFrame.getCurrentPanel()).setFilterGame((TypesGame)((TeamManagement)MasterFrame.getCurrentPanel()).getComboBoxFilterGame().getSelectedItem());
					break;
				default:
					if(e.getActionCommand().contains("RENDERER")) {
						String model = null;
						if (e.getActionCommand().contains("INFO")) {
							model = "STABLE_MANAGEMENT RENDERER INFO ";
						}else {
							model = "STABLE_MANAGEMENT RENDERER MODIFY ";
						}
						int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
						TypesTeam team = ((TeamManagement)MasterFrame.getCurrentPanel()).getRenderer(id).getTeam();
						if (e.getActionCommand().contains("INFO")) {
							MasterFrame.getInstance().setPanel(com.esporter.client.vue.SeePlayerInfos.class, team);
							state = State.PLAYER_INFO;
						}else {
							MasterFrame.getInstance().setPanel(com.esporter.client.vue.stable.ModifyTeam.class, team);
							state = State.MODIFY_TEAM;
						}
					}
					break;
				}
				break;
			case POOL:
				switch(e.getActionCommand()) {
				case "PROGRAM_MATCH_MATCH":
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.CalendarAndScoreMatch.class, ((ProgramMatchs)MasterFrame.getCurrentPanel()).getTournament());
					state = State.MATCHES;
					break;
					
				}
				break;
			case MATCHES:
				if(e.getActionCommand().contains("RENDERER")) {
					String model = "MATCHES RENDERER SCORE ";
					int id = Integer.parseInt(e.getActionCommand().substring(model.length()));
					RendererProgramMatch rpm = ((CalendarAndScoreMatch)MasterFrame.getCurrentPanel()).getRenderer(id);
					//Appel du JDialog pour choisir le gagant
					SetScore scorePage = new SetScore(rpm.getMatch());
					scorePage.setVisible(true);
					scorePage.setAlwaysOnTop(true);
				}
				break;
			case ADD_SCORE:
				SetScore s = (SetScore)lastDialog;
				if(e.getActionCommand() == "ADD_SCORE_VALIDATE") {
					TypesMatch match = s.getMatch();
					if(s.getRdbTeam1().isSelected()) {
						match.setPoint(1, 0);
						JOptionPane.showMessageDialog(null, "Choix enregistré","", JOptionPane.INFORMATION_MESSAGE);
						s.dispose();
						closeDialog();
						Controler.getInstance().getUser().changeScore(match, match.getIdTournament(), match.getIdPool());
						Controler.getInstance().getUser().getWaiting().waitFor(Response.ERROR, Response.ERROR_PERMISSION, Response.UPDATE_TOURNAMENT);
					} else if (s.getRdbTeam2().isSelected()) {
						match.setPoint(0, 1);
						JOptionPane.showMessageDialog(null, "Choix enregistré","", JOptionPane.INFORMATION_MESSAGE);
						s.dispose();
						closeDialog();
						Controler.getInstance().getUser().changeScore(match, match.getIdTournament(), match.getIdPool());
						Controler.getInstance().getUser().getWaiting().waitFor(Response.ERROR, Response.ERROR_PERMISSION, Response.UPDATE_TOURNAMENT);
					} else {
						JOptionPane.showMessageDialog(null, "Aucun choix selectionné","Erreur", JOptionPane.ERROR_MESSAGE);
					}
				} else if (e.getActionCommand() == "ADD_SCORE_CANCEL") {
					s.dispose();
				}
				break;
			case REGISTER_STABLE:
				switch(e.getActionCommand()) {
				case "REGISTER_STABLE_CANCEL":
					MasterFrame.getInstance().setPanel(com.esporter.client.vue.Calendar.class, this.getUser().getPermission());
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
							MasterFrame.getInstance().setPanel(com.esporter.client.vue.Calendar.class, user.getPermission());
						} catch (IOException e1) {
							fireError( new IllegalArgumentException("Il y a une erreur avec la photo"), false, false);
						} 
					}
					break;
				}
				break;
			default:
				break;
			
			}
		}
		System.out.println(state);
		
	}

	
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////		 MOUSE LISTENER PART 		/////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		switch(state) {
		case ADD_PLAYER:
			//Si on a un mouse click sur ADD_PLAYER c'est que l'on clique sur un label
			AddPlayer jd = (AddPlayer)lastDialog;
			JFileChooser file = jd.getFileExplorer();
			int res = file.showOpenDialog(null);
	        //si l'utilisateur clique sur enregistrer dans Jfilechooser
	        if(res == JFileChooser.APPROVE_OPTION){
	        	File selFile = file.getSelectedFile();
	        	jd.setFile(selFile);
	        }

			break;
		case ADD_TEAM:
			if (e.getSource() instanceof ContainerPlayer) {
				AddPlayer ajout = new AddPlayer((ContainerPlayer)e.getSource());
				ajout.setVisible(true);
				ajout.setAlwaysOnTop(true);
				
			}
			break;
		case CALENDAR:
			break;
		case ERROR:
			break;
		case HOME_ORGANIZER:
			break;
		case HOME_PLAYER:
			break;
		case HOME_REFEREE:
			break;
		case HOME_STABLE:
			break;
		case HOME_VISITOR:
			break;
		case LOGIN:
			break;
		case MODIFY_PLAYER:
			//Si on a un mouse click sur MODIFY_PLAYER c'est que l'on clique sur un label
			ModifyPlayer mp = (ModifyPlayer)lastDialog;
			JFileChooser fileExplorer = mp.getFileExplorer();
			int result = fileExplorer.showOpenDialog(null);
	        //si l'utilisateur clique sur enregistrer dans Jfilechooser
	        if(result == JFileChooser.APPROVE_OPTION){
	        	File selFile = fileExplorer.getSelectedFile();
	        	mp.setFile(selFile);
	        }
			break;
		case MODIFY_TEAM:
			if(e.getSource() instanceof ContainerModifyPlayer) {
				ModifyPlayer ajout = new ModifyPlayer((ContainerModifyPlayer)e.getSource());
				ajout.setVisible(true);
				ajout.setAlwaysOnTop(true);
			}
			break;
		case PLAYER_INFO:
			break;
		case RANKING:
			break;
		case STABLE_INFO:
			break;
		case STABLE_LIST:
			break;
		case STABLE_MANAGEMENT:
			break;
		case REGISTER_STABLE:
			RegisterStable rs = (RegisterStable) MasterFrame.getCurrentPanel();
			JFileChooser fileE = rs.getFileExplorer();
			int resultFile = fileE.showOpenDialog(null);
	        //si l'utilisateur clique sur enregistrer dans Jfilechooser
	        if(resultFile == JFileChooser.APPROVE_OPTION){
	        	File selFile = fileE.getSelectedFile();
	        	rs.setFile(selFile);
	        }
		default:
			break;
		
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
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////			JDIALOG SUPPORT			/////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	
	public void closeDialog() {
		this.lastDialog=null;
		this.state = stateBefore;
	}
	
	public void openDialog(JDialog jdiag, State name) {
		this.lastDialog=jdiag;
		this.stateBefore = this.state;
		this.state=name;
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////			KEY LISTENER PART			/////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(state) {
		case LOGIN:
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
			break;
		case ERROR:
			if (e.getKeyCode()==KeyEvent.VK_ENTER){
				if(MasterFrame.getInstance().getError().isCritical()) {
					MasterFrame.getInstance().getFrame().dispose();
					System.exit(-2);
				}
				MasterFrame.getInstance().getError().setVisible(false);
				MasterFrame.getInstance().getError().setException(null);
				closeError();
				
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
