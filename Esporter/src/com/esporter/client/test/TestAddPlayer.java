package com.esporter.client.test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.esporter.both.socket.Response;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesLogin;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesRegisterPlayer;
import com.esporter.both.types.TypesRegisterTeam;
import com.esporter.both.types.exception.ExceptionInvalidPermission;
import com.esporter.both.types.exception.ExceptionLogin;
import com.esporter.client.controleur.Controler;
import com.esporter.client.model.user.User;
import com.esporter.client.vue.MasterFrame;

class TestAddPlayer {

	private TypesPlayer player = new TypesPlayer(300, "test", "test", null , Timestamp.valueOf("2002-10-05 00:00:00"), Timestamp.valueOf("2015-10-10 00:00:00"),
			Timestamp.valueOf("2010-10-15 00:00:00"), 1, 1, 1, "test");
	private TypesPlayer playerTwo = new TypesPlayer(301, "test2", "test2", null , Timestamp.valueOf("2002-10-05 00:00:00"), Timestamp.valueOf("2015-10-10 00:00:00"),
			Timestamp.valueOf("2010-10-15 00:00:00"), 1, 1, 1, "test2");
	private TypesLogin typesLogin = new TypesLogin("test","test");
	private TypesRegisterPlayer registerPlayerOne = new TypesRegisterPlayer(player, typesLogin);
	private TypesRegisterPlayer registerPlayerTwo = new TypesRegisterPlayer(player, new TypesLogin("test1","test1"));
	private TypesRegisterPlayer registerPlayerThree = new TypesRegisterPlayer(player, new TypesLogin("test2","test2"));
	
	private Controler controler;
	private User user;
	
	
	public TestAddPlayer() throws SQLException {
		this.controler = Controler.getInstance();
		MasterFrame.getInstance().getFrame().setVisible(true);
		this.user = controler.getUser();
	}
	
	@Test
	@Order(1)
	@DisplayName("Test permission")
	public void testAddTeam1() {
		ArrayList<TypesRegisterPlayer> team = new ArrayList<TypesRegisterPlayer>();
		team.add(registerPlayerOne);
		team.add(registerPlayerTwo);
		team.add(registerPlayerThree);
		TypesRegisterTeam teamfinal = new TypesRegisterTeam(TypesGame.ROCKET_LEAGUE, 10, team);
		System.out.println("Connexion esporter");
		assertAll("Connexion esporter",
				() -> this.user.login("test", "mdpTest"),
				() -> assertEquals(TypesPermission.ORGANIZER, user.getPermission()),
				() -> assertThrows(ExceptionInvalidPermission.class, () -> {user.addTeam(teamfinal);}),
				() -> this.user.logout()
				);
		System.out.println("Connexion referee");
		assertAll("Connexion referee",
				() -> this.user.login("arbitre", "arbitre"),
				() -> assertEquals(TypesPermission.REFEREE, user.getPermission()),
				() -> assertThrows(ExceptionInvalidPermission.class, () -> {user.addTeam(teamfinal);}),
				() -> this.user.logout()
				);
		System.out.println("Connexion Player");
		assertAll("Connexion Player",
				() -> this.user.login("gimmick", "gimmick"),
				() -> assertEquals(TypesPermission.PLAYER, user.getPermission()),
				() -> assertThrows(ExceptionInvalidPermission.class, () -> {user.addTeam(teamfinal);}),
				() -> this.user.logout()
				);
		System.out.println("Connexion Stable");
		assertAll("Connexion Stable",
				() -> this.user.login("G2", "G2"),
				() -> assertEquals(TypesPermission.STABLE, user.getPermission()),
				() -> this.user.addTeam(teamfinal),
				() -> this.user.logout()
				);
		System.out.println("Connexion visitor");
		assertAll("Connexion visitor",
				() -> assertEquals(TypesPermission.VISITOR, user.getPermission()),
				() -> assertThrows(ExceptionInvalidPermission.class, () -> {user.addTeam(teamfinal);})
				);
	}
	
	@Test
	@Order(2)
	@DisplayName("Test add player")
	public void testAddPlayer2() throws ExceptionLogin {
		this.user.login("G2", "G2");
		ArrayList<TypesRegisterPlayer> team = new ArrayList<TypesRegisterPlayer>();
		team.add(registerPlayerOne);
		team.add(registerPlayerTwo);
		team.add(registerPlayerThree);
		TypesRegisterTeam teamfinal = new TypesRegisterTeam(TypesGame.ROCKET_LEAGUE, 10, team);
		user.addTeam(teamfinal);
		assertAll("Team added",
				() -> assertEquals(Response.UPDATE_TEAM, user.getWaiting().getActualState())
				);	
	}
	
	@Test
	void testGettersTypesPlayer() {
		assertTrue(player.getBirthDate().equals(Timestamp.valueOf("2002-10-05 00:00:00")));
		assertTrue(player.getFirstName().equals("test"));
		assertTrue(player.getName().equals("test"));
		assertTrue(player.getContractEndDate().equals(Timestamp.valueOf("2010-10-15 00:00:00")));
		assertTrue(player.getContractStartDate().equals(Timestamp.valueOf("2015-10-10 00:00:00")));
		assertTrue(player.getId()==300);
		assertTrue(player.getIdStable()== 1);
		assertTrue(player.getIdTeam()==1);
		assertTrue(player.getUsername().equals("test"));
		assertTrue(player.getNationality()==1);
		assertTrue(player.getImage()==null);
		
	}
	
	@Test
	void testSetIdTypesPlayer() {
		player.setId(305);
		assertTrue(player.getId()==305);
	}
	
	@Test
	void testEqualsTypesPlayer() {
		TypesPlayer playerCopy = new TypesPlayer(300, "test", "test", null , Timestamp.valueOf("2002-10-05 00:00:00"), Timestamp.valueOf("2015-10-10 00:00:00"),
				Timestamp.valueOf("2010-10-15 00:00:00"), 1, 1, 1, "test");
		assertTrue(player.equals(playerCopy));
		assertFalse(playerTwo.equals(playerCopy));
	}
	
	@Test
	void testGettersTypesRegisterPlayer() {
		assertTrue(registerPlayerOne.getPlayer().equals(player));
		assertEquals(registerPlayerOne.getLogin(), typesLogin);
	}

	
}
