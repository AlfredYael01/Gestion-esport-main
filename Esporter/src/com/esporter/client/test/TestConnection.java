package com.esporter.client.test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.esporter.both.socket.Response;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesStable;
import com.esporter.both.types.exception.ExceptionLogin;
import com.esporter.client.controleur.MasterControler;
import com.esporter.client.model.user.User;
import com.esporter.client.vue.MasterFrame;

@DisplayName("Tests de la connection de l'application")
public class TestConnection {
	
	private User user;
	
	
	public TestConnection() throws Exception {
		
		
		this.user = new User();
		MasterControler controler = new MasterControler(user);
		MasterFrame.getInstance().getFrame().setVisible(true);
	}


	
	@Test
	@DisplayName("Connection from the start to server")
	public void testConnection() {

		assertAll("Connected as visitor",
				() -> assertEquals(Response.UPDATE_ALL, user.getWaiting().getActualState()),
				() -> assertEquals(TypesPermission.VISITOR, user.getPermission()),
				() -> assertEquals(null, user.getInfo())
				);
		
		
	}
	
	@Test
	@DisplayName("Connection successful to server")
	public void testConnection1() throws ExceptionLogin {

		assertAll("Connexion esporter",
				() -> this.user.login("test", "mdpTest"),
				() -> assertEquals(Response.LOGIN, user.getWaiting().getActualState()),
				() -> assertEquals(TypesPermission.ORGANIZER, user.getPermission()),
				() -> assertEquals(null, user.getInfo()),
				() -> this.user.logout()
				);
		assertAll("Connexion referee",
				() -> this.user.login("arbitre", "arbitre"),
				() -> assertEquals(Response.LOGIN, user.getWaiting().getActualState()),
				() -> assertEquals(TypesPermission.REFEREE, user.getPermission()),
				() -> assertEquals(null, user.getInfo()),
				() -> this.user.logout()
				);
		assertAll("Connexion Player",
				() -> this.user.login("gimmick", "gimmick"),
				() -> assertEquals(Response.LOGIN, user.getWaiting().getActualState()),
				() -> assertEquals(TypesPermission.PLAYER, user.getPermission()),
				() -> assertTrue(user.getInfo() instanceof TypesPlayer),
				() -> this.user.logout()
				);
		assertAll("Connexion Stable",
				() -> this.user.login("G2", "G2"),
				() -> assertEquals(Response.LOGIN, user.getWaiting().getActualState()),
				() -> assertEquals(TypesPermission.STABLE, user.getPermission()),
				() -> assertTrue(user.getInfo() instanceof TypesStable),
				() -> this.user.logout()
				);
		
		
	}
	
	@Test
	@DisplayName("Connection unsuccessful to server")
	public void testConnection2() {

		assertAll("Connexion non user",
				() -> assertThrows(ExceptionLogin.class, () -> this.user.login("testeee", "mdpTest")),
				() -> assertEquals(Response.ERROR_LOGIN, user.getWaiting().getActualState()),
				() -> assertEquals(TypesPermission.VISITOR, user.getPermission()),
				() -> assertEquals(null, user.getInfo())
				);
		
		
	}
	
	
	
	
	

}
