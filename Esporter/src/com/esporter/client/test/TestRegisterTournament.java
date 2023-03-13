package com.esporter.client.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.esporter.both.socket.Response;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.exception.ExceptionInvalidPermission;
import com.esporter.both.types.exception.ExceptionLogin;
import com.esporter.client.controleur.MasterControler;
import com.esporter.client.model.user.User;
import com.esporter.client.vue.MasterFrame;

@DisplayName("Tests inscription aux tournois")
public class TestRegisterTournament {
	
	private MasterControler controler;
	private User user;
	private final static int TEST_TOURNAMENT_ID = 192 ;
	
	public TestRegisterTournament() throws SQLException, UnknownHostException, IOException {
		this.user = new User();
		this.controler = new MasterControler(user);
		MasterFrame.getInstance().getFrame().setVisible(true);
		
	}
	
	@Test
	@Order(1)
	@DisplayName("Test permission")
	public void testRegister1() {
		System.out.println("Connexion esporter");
		assertAll("Connexion esporter",
				() -> this.user.login("test", "mdpTest"),
				() -> assertEquals(TypesPermission.ORGANIZER, user.getPermission()),
				() -> assertThrows(ExceptionInvalidPermission.class, () -> {user.registerTournament(TEST_TOURNAMENT_ID);}),
				() -> this.user.logout()
				);
		System.out.println("Connexion referee");
		assertAll("Connexion referee",
				() -> this.user.login("arbitre", "arbitre"),
				() -> assertEquals(TypesPermission.REFEREE, user.getPermission()),
				() -> assertThrows(ExceptionInvalidPermission.class, () -> {user.registerTournament(TEST_TOURNAMENT_ID);}),
				() -> this.user.logout()
				);
		System.out.println("Connexion Player");
		assertAll("Connexion Player",
				() -> this.user.login("gimmick", "gimmick"),
				() -> assertEquals(TypesPermission.PLAYER, user.getPermission()),
				() -> user.registerTournament(TEST_TOURNAMENT_ID),
				() -> user.unregisterTournament(TEST_TOURNAMENT_ID, TypesGame.gameToInt(TypesGame.ROCKET_LEAGUE)),
				() -> this.user.logout()
				);
		System.out.println("Connexion Stable");
		assertAll("Connexion Stable",
				() -> this.user.login("G2", "G2"),
				() -> assertEquals(TypesPermission.STABLE, user.getPermission()),
				() -> assertThrows(ExceptionInvalidPermission.class, () -> {user.registerTournament(TEST_TOURNAMENT_ID);}),
				() -> this.user.logout()
				);
		System.out.println("Connexion visitor");
		assertAll("Connexion visitor",
				() -> assertEquals(TypesPermission.VISITOR, user.getPermission()),
				() -> assertThrows(ExceptionInvalidPermission.class, () -> {user.registerTournament(TEST_TOURNAMENT_ID);})
				);
	}
	
	@Test
	@Order(2)
	@DisplayName("Test unregister")
	public void testRegister2() throws ExceptionLogin, SQLException, ExceptionInvalidPermission {
		this.user.login("gimmick", "gimmick");
		user.unregisterTournament(TEST_TOURNAMENT_ID, TypesGame.gameToInt(TypesGame.ROCKET_LEAGUE));
		assertAll("Registration",
			() -> assertEquals(Response.UPDATE_TOURNAMENT, user.getWaiting().getActualState()),
			() -> assertFalse(this.user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getRegistered().contains(((TypesPlayer)user.getInfo()).getIdTeam())));
		user.logout();
		
	}
	
	
	@Test
	@Order(3)
	@DisplayName("Test register")
	public void testRegister3() throws ExceptionLogin, SQLException, ExceptionInvalidPermission {
		this.user.login("gimmick", "gimmick");
		user.registerTournament(TEST_TOURNAMENT_ID);
		assertAll("Registration",
				() -> assertEquals(Response.UPDATE_TOURNAMENT, user.getWaiting().getActualState()),
				() -> assertTrue(this.user.getData().getCalendar().get(TEST_TOURNAMENT_ID).getRegistered().contains(((TypesPlayer)user.getInfo()).getIdTeam())));
		user.logout();
		
	}
	
	
}
