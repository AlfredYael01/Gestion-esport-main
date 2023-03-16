package com.esporter.client.model.user;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.esporter.both.socket.Command;
import com.esporter.both.socket.CommandName;
import com.esporter.both.socket.ResponseObject;
import com.esporter.both.types.Types;
import com.esporter.both.types.TypesID;
import com.esporter.both.types.TypesInteger;
import com.esporter.both.types.TypesLogin;
import com.esporter.both.types.TypesMatch;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesRegisterTeam;
import com.esporter.both.types.TypesStable;
import com.esporter.both.types.TypesTeam;
import com.esporter.both.types.TypesTournament;
import com.esporter.client.controleur.MasterControler;
import com.esporter.client.vue.MasterFrame;

public class CommunicationServer{
	
	private User user;
	private int reconnect = 1;
	private long reconnectTime = 1;
	private static final String IP = "localhost"; //144.24.206.118
	private static final int PORT = 45000;
	private Map<Integer, Types> decodeId;
	private NettyClient netty;
	private CommunicationServer waitingThread;
	
	public CommunicationServer getWaitingThread() {
		return waitingThread;
	}
	
	public CommunicationServer(User user) throws UnknownHostException, IOException {
		this.user = user;
		this.decodeId = new HashMap<>();
		netty = new NettyClient(user);
		connect();
		
	}
	
	private void connect() throws UnknownHostException, IOException{

		System.out.println("Tentative de connexion au server "+IP+":"+PORT);
		
		try {
			netty.run(IP, PORT);
			System.out.println("Done");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reconnect();
		}
		
		
	}
	
	private void reconnect() throws IOException,UnknownHostException {
		reconnect++;
		if (reconnect>5) {
			MasterControler.fireError(new Exception("Impossible de se connecter au serveur, veuillez relancer l'application"), false, true);
			JOptionPane.showMessageDialog(null, "L'application va maintenant fermer","Erreur", JOptionPane.ERROR_MESSAGE);
			System.exit(-2);
		
		}
		reconnectTime*=2;
		MasterControler.fireError(new Exception("Erreur de connexion au serveur \n Tentative de reconnexion n°"+reconnect+" dans "+reconnectTime+"s...."), true, false);
		String s = "Reconnecting number "+reconnect+" in "+reconnectTime+"s....";
		System.out.println(s);
		try {
			Thread.sleep(reconnectTime*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			connect();
			reconnect=1;
			reconnectTime=1;
			MasterFrame.getInstance().getError().setVisible(false);
			MasterFrame.getInstance().getError().setException(null);
		} catch (IOException e2) {
			reconnect();
			
		}
	}

	
	public Types waitSynhronousResponse(Command c) {
		int id = (int) (new Date().getTime())/1000;
		c.getInfo().put(TypesID.INT, new TypesInteger(id));
		this.decodeId.put(id, null);
		netty.send(c);
		while(decodeId.get(id)==null) {
			try {
				wait();
				this.waitingThread = this;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return decodeId.get(id);
	}
	
	public void deleteTournament(int t) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TOURNAMENT, new TypesInteger(t));
		Command c = new Command(CommandName.DELETE_TOURNAMENT, m);
		netty.send(c);
	}
	
	
	public void sendLogin(String username, String password) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.LOGIN, new TypesLogin(username, password));
		Command c = new Command(CommandName.LOGIN, m);
		netty.send(c);
	}
	
	public void registerTournament(int idTournament) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TOURNAMENT, new TypesInteger(idTournament));
		m.put(TypesID.PLAYER, new TypesInteger(((TypesPlayer)user.getInfo()).getId()));
		Command c = new Command(CommandName.REGISTER_TOURNAMENT, m);
		netty.send(c);
	}
	
	public void unregisterTournament(int idTournament, int idGame) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TOURNAMENT, new TypesInteger(idTournament));
		m.put(TypesID.PLAYER, new TypesInteger(((TypesPlayer)user.getInfo()).getId()));
		m.put(TypesID.GAME, new TypesInteger(idGame));
		Command c = new Command(CommandName.UNREGISTER_TOURNAMENT ,m);
		netty.send(c);
	}
	
	public void addTeam(TypesRegisterTeam team) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TEAM, team);
		Command c = new Command(CommandName.ADD_TEAM, m);
		netty.send(c);
	}
	
	public void modifyTeam(TypesTeam team) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TEAM, team);
		Command c = new Command(CommandName.MODIFY_TEAM, m);
		netty.send(c);
	}
	
	public void addTournament(TypesTournament t) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TOURNAMENT, t);
		Command c = new Command(CommandName.ADD_TOURNAMENT, m);
		netty.send(c);
	}
	
	public void logout() {
		Command c = new Command(CommandName.LOGOUT, null);
		netty.send(c);
	}
	
	public void receiveLogin(ResponseObject r) {
		TypesPermission perm = TypesPermission.VISITOR;
		if (r.getInfo().containsKey(TypesID.PERMISSION)) {
			perm = (TypesPermission) r.getInfoByID(TypesID.PERMISSION);
		}
		setPermission(perm);
		if (r.getInfo().containsKey(TypesID.STABLE)) {
			user.setInfo(r.getInfoByID(TypesID.STABLE));

		}
		if (r.getInfo().containsKey(TypesID.PLAYER)) {
			user.setInfo(r.getInfoByID(TypesID.PLAYER));
		}
	}
	
	public void initializeApp() {
		Command c = new Command(CommandName.INIT, null);
		netty.send(c);
	}
	
	
	public void setPermission(TypesPermission perms) {
		user.setPermission(perms);
	}
	

	public void modifyTournament(TypesTournament t) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TOURNAMENT, t);
		Command c = new Command(CommandName.MODIFY_TOURNAMENT, m);
		netty.send(c);
	}
	
	
	public void changeScore(TypesMatch match, int idTournament, int idPool) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.MATCH, match);
		m.put(TypesID.TOURNAMENT, new TypesInteger(idTournament));
		m.put(TypesID.POOL, new TypesInteger(idPool));
		Command c = new Command(CommandName.SCORE, m);
		netty.send(c);
	}
	
	public void registerStable(TypesStable s, TypesLogin l) {
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.STABLE, s);
		m.put(TypesID.LOGIN, l);
		Command c = new Command(CommandName.STABLE,m);
		netty.send(c);
	}


	
}
