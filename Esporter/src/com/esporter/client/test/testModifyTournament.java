package com.esporter.client.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.sql.SQLException;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

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
	@DisplayName("Test Modify Succesfully")
	public void testRegister1()throws ExceptionLogin, SQLException, ExceptionInvalidPermission {
		this.user.login("test","mdpTest");
		TypesTournament tournament = this.user.getData().getCalendar().get(TEST_TOURNAMENT_ID);
		TypesGame oldGame = tournament.getGame();
		TypesGame newGame = TypesGame.COUNTER_STRIKE;
		tournament.setGame(newGame);
		this.user.modifyTournament(tournament);
		
		assertAll("Modification",
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(newGame, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getGame()),
				() -> tournament.setGame(oldGame),
				() -> this.user.modifyTournament(tournament),
				() -> assertEquals(Response.UPDATE_TOURNAMENT,user.getWaiting().getActualState()),
				() -> assertEquals(oldGame, user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getGame())
				);
		this.user.logout();
	}
	
	
	
}
