package com.esporter.client.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import com.esporter.both.types.TypesLogin;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesRegisterPlayer;

class TestAddPlayer {

	private TypesPlayer player = new TypesPlayer(300, "test", "test", null , Timestamp.valueOf("2002-10-05 00:00:00"), Timestamp.valueOf("2015-10-10 00:00:00"),
			Timestamp.valueOf("2010-10-15 00:00:00"), 1, 1, 1, "test");
	private TypesPlayer playerTwo = new TypesPlayer(301, "test2", "test2", null , Timestamp.valueOf("2002-10-05 00:00:00"), Timestamp.valueOf("2015-10-10 00:00:00"),
			Timestamp.valueOf("2010-10-15 00:00:00"), 1, 1, 1, "test2");
	private TypesLogin typesLogin = new TypesLogin("test","test");
	private TypesRegisterPlayer registerPlayer = new TypesRegisterPlayer(player, typesLogin);
	
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
		assertTrue(registerPlayer.getPlayer().equals(player));
		assertEquals(registerPlayer.getLogin(), typesLogin);
	}

}
