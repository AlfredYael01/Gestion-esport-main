package com.esporter.client.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.esporter.both.types.TypesFame;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesTournament;
import com.esporter.both.types.exception.ExceptionInvalidPermission;
import com.esporter.both.types.exception.ExceptionLogin;
import com.esporter.client.controleur.Controler;
import com.esporter.client.model.user.User;
import com.esporter.client.vue.MasterFrame;
import com.esporter.both.data.Data;
import com.esporter.both.socket.Response;


@DisplayName("Test Modifier un tournoi")
public class testModifyTournament {

	private Controler controler;
	private User user;
	private final static int TEST_TOURNAMENT_ID = 192 ;
	
	
	public testModifyTournament() throws SQLException {
		this.controler = Controler.getInstance();
		MasterFrame.getInstance().getFrame().setVisible(true);
		this.user = controler.getUser();
		
	}
	@Test
	@Order(1)
	@DisplayName("Test Modify Name Succesfully")
	public void testRegister1()throws ExceptionLogin, SQLException, ExceptionInvalidPermission {
		this.user.login("test","mdpTest");
		TypesTournament tournament = this.user.getData().getCalendar().get(TEST_TOURNAMENT_ID);
		String oldName = tournament.getName();
		String newName = "TestTournamentSucces";
		tournament.setName(newName);
		this.user.modifyTournament(tournament);
		
		assertAll("Modification of game type",
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(newName, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getName()),
				() -> tournament.setName(oldName),
				() -> this.user.modifyTournament(tournament),
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(oldName, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getGame())
				);
		this.user.logout();
	}
	
	@Test
	@Order(2)
	@DisplayName("Test Modify Fame Succesfully")
	public void testRegister2()throws ExceptionLogin, SQLException, ExceptionInvalidPermission {
		this.user.login("test","mdpTest");
		TypesTournament tournament = this.user.getData().getCalendar().get(TEST_TOURNAMENT_ID);
		TypesFame oldFame = tournament.getFame();
		TypesFame newFame = TypesFame.INTERNATIONAL;
		tournament.setFame(newFame);
		this.user.modifyTournament(tournament);
		
		assertAll("Modification of fame type",
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(newFame, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getFame()),
				() -> tournament.setFame(oldFame),
				() -> this.user.modifyTournament(tournament),
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(oldFame, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getFame())
				);
		this.user.logout();
		}
	
	@Test
	@Order(3)
	@DisplayName("Test Modify Date Succesfully")
	public void testRegister3()throws ExceptionLogin, SQLException, ExceptionInvalidPermission {
		this.user.login("test","mdpTest");
		TypesTournament tournament = this.user.getData().getCalendar().get(TEST_TOURNAMENT_ID);
		Timestamp oldDate = tournament.getRegisterDate();
		Timestamp newDate = new Timestamp(System.currentTimeMillis());
		tournament.setRegisterDate(newDate);
		this.user.modifyTournament(tournament);
		
		assertAll("Modification of the date",
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(newDate, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getFame()),
				() -> tournament.setRegisterDate(oldDate),
				() -> this.user.modifyTournament(tournament),
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(oldDate, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getFame())
				);
		this.user.logout();
		}
	
	@Test
	@Order(4)
	@DisplayName("Test  Modification no value changed")
	public void testRegister4()throws ExceptionLogin, SQLException, ExceptionInvalidPermission {
		this.user.login("test","mdpTest");
		TypesTournament tournament = this.user.getData().getCalendar().get(TEST_TOURNAMENT_ID);
		TypesTournament NewTournament = new TypesTournament(tournament.getRegisterDate(),tournament.getName(),tournament.getFame(),tournament.getGame(),193);
		TypesFame oldFame = NewTournament.getFame();
		TypesFame newFame = oldFame;
		this.user.modifyTournament(NewTournament);
		
		assertAll("Modification of fame type",
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(newFame, user.getData().getCalendar().get(193).getFame()),
				() -> NewTournament.setFame(oldFame),
				() -> this.user.modifyTournament(NewTournament),
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(oldFame, user.getData().getCalendar().get(193).getFame())
				);
		this.user.logout();
		}
	
	@Test
	@Order(5)
	@DisplayName("Test Modification change date tournament in a another date tounament")
	public void testRegister5()throws ExceptionLogin, SQLException, ExceptionInvalidPermission {
		this.user.login("test","mdpTest");
		TypesTournament tournament = this.user.getData().getCalendar().get(TEST_TOURNAMENT_ID);
		TypesTournament NewTournament = new TypesTournament(tournament.getRegisterDate(),tournament.getName(),tournament.getFame(),tournament.getGame(),193);
		Timestamp oldDate = NewTournament.getRegisterDate();
		Timestamp newDate = oldDate;
		this.user.modifyTournament(NewTournament);
		
		assertAll("Modification of the date",
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(newDate, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getFame()),
				() -> NewTournament.setRegisterDate(oldDate),
				() -> this.user.modifyTournament(NewTournament),
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(oldDate, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getFame())
				);
		this.user.logout();
		}
	
}
