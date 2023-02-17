	package com.esporter.server.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;

import com.esporter.both.socket.ResponseObject;
import com.esporter.both.types.Types;
import com.esporter.both.types.TypesFame;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesID;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesTournament;
import com.esporter.server.model.database.DatabaseAccess;
import com.esporter.server.model.database.Query;
import com.esporter.server.model.database.Query.typeRequete;
import com.esporter.server.model.database.Result;

public class ConnectionClient {

	
	private Thread thread;
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean isLogin = false;
	private TypesPermission role = TypesPermission.VISITOR;
	
	public ConnectionClient(Socket s) {
		this.s = s;
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());			
		} catch (IOException e) {
			
		}
		thread = new Thread(new ListenClient(this));
		thread.setDaemon(true);
		thread.start();
	}
	
	public int login(String username, String password) {
		try {
			Result r = DatabaseAccess.getInstance().login(new Query(Query.Login(username, password), typeRequete.FUNCTION));
			if (r.isError()) {
				System.out.println("Error");
				return -1;
			}
			System.out.println(r.getInteger());
			return r.getInteger();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
		/*
		if (username.equals("test") && password.equals("mdpTest"))
			return 4;
		return -1;*/
	}
	
	public int ajouterTournoi(TypesTournament t) {
		Query req = new Query(Query.addTournament(TypesGame.gameToInt(t.getGame()), t.getName(), TypesFame.FameToInt(t.getFame())), typeRequete.FUNCTION);
		req.setDates(t.getRegisterDate());

		try {
			Result r = DatabaseAccess.getInstance().insertData(req);
			int id = r.getInteger();
			t.setId(id);
			mainThread.getInstance().getData().getCalendar().put(t.getId(), t);
			HashMap<TypesID, Types> m = new HashMap<>();
			m.put(TypesID.TOURNAMENT, t);
			mainThread.getInstance().miseAJourData(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	
	
	public void setPermission(int perm) {
		switch(perm) {
		case 1:role = TypesPermission.ORGANIZER;break;
		case 2:role = TypesPermission.REFEREE;break;
		case 3:role = TypesPermission.PLAYER;break;
		case 4:role = TypesPermission.STABLE;break;
		}
	}
	
	public TypesPermission getRole() {
		return role;
	}
	
	public void setPermission(TypesPermission perm) {
		role = perm;
	}
	
	public void send(ResponseObject o) {
		try {
			out.reset();
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public int logout() {
		return -1;
	}
	
	public Socket getSocket() {
		return s;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public ObjectInputStream getIn() {
		return in;
	}
	
	public ObjectOutputStream getOut() {
		return out;
	}
	
	public boolean getIsLogin() {
		return isLogin;
	}
	
	public void setIsLogin(boolean b) {
		isLogin = b;
	}
	
}
