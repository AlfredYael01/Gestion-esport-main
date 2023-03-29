package com.esporter.server.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import com.esporter.both.data.Data;
import com.esporter.both.socket.Response;
import com.esporter.both.socket.ResponseObject;
import com.esporter.both.types.Types;
import com.esporter.both.types.TypesFame;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesID;
import com.esporter.both.types.TypesImage;
import com.esporter.both.types.TypesInteger;
import com.esporter.both.types.TypesMatch;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesPool;
import com.esporter.both.types.TypesRanking;
import com.esporter.both.types.TypesStable;
import com.esporter.both.types.TypesTeam;
import com.esporter.both.types.TypesTitle;
import com.esporter.both.types.TypesTournament;
import com.esporter.server.model.database.DatabaseAccess;
import com.esporter.server.model.database.Query;
import com.esporter.server.model.database.Query.typeRequete;

import io.netty.channel.ChannelHandlerContext;

public class mainThread {

	private static mainThread instance;
	private boolean running=true;
	
	private ConcurrentHashMap<String, ConnectionClient> allClients = new ConcurrentHashMap<>();
	private static volatile int nbClient=0;
	private DatabaseAccess db;
	private Data data;
	
	private mainThread() {
		try {
			db = DatabaseAccess.getInstance();
			data = new Data();
			initializeApp();
			instance = this;
			NettyServer netty = null;
			try {
			
				netty = new NettyServer(45000);

				System.out.println("Serv démarré");
				System.out.println("En attente d'une connexion");
				netty.run();
				netty.getChannel().closeFuture().sync();
				
				
				
				//Stop all client

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				netty.shutdownGracefully();//Stop server
				
				//Stop db
				DatabaseAccess.getInstance().getTimerCheckAlive().cancel(); 
				DatabaseAccess.getInstance().stopThread();
				DatabaseAccess.getInstance().getConn().close();
				
				//Disconnect all the clients
				Collection<ConnectionClient> tabClient = allClients.values();
				for (ConnectionClient c : tabClient) {
					try {
						c.getChannel().close().sync();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				System.out.println("Server shutdown");
			}

			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	
	public static mainThread getInstance() {
		if (instance==null)
			instance = new mainThread();
		return instance;
	}
	
	public void ajouterClient(ConnectionClient c, ChannelHandlerContext ctx) {
		String host = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
		int port = ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();
		String uniqueID = host+":"+port;
		synchronized (allClients) {
			allClients.put(uniqueID, c);

			nbClient++;
		}
	}
	
	
	
	public synchronized Data getData() {
		return data;
	}
	
	public void initializeApp() throws InterruptedException, SQLException, IOException {
		//ECURIE
		Query r = new Query(Query.allStables(), typeRequete.QUERY);
		ResultSet rs = db.getData(r).getResultSet();
		ConcurrentHashMap<Integer, TypesStable> ecuries = new ConcurrentHashMap<>();
		while(rs.next()) {
			BufferedImage bf1 = ImageIO.read(rs.getBinaryStream("logoecurie"));
			TypesImage im1 = new TypesImage(bf1, "png");
			ecuries.put(rs.getInt("id_utilisateur"), new TypesStable(rs.getString("nomEcurie"), im1, rs.getString("DiminutifEcurie"), rs.getInt("id_utilisateur")));
		}
		rs.close();
		data.setStables(ecuries);
		System.out.println("Load stable ok");
		
		//EQUIPE
		TypesTeam equipe;
		TypesPlayer joueur;
		ResultSet resultJoueur;
		Query requeteGetJoueur;
		HashMap<Integer,TypesPlayer> joueurs;
		ArrayList<TypesTitle> palmares;
		
		for (TypesStable ec : data.listStables()) {
			r = new Query(Query.allTeamByStables(ec.getId()), typeRequete.QUERY);
			rs = db.getData(r).getResultSet();
			while(rs.next()) {
				System.out.println("Equipe "+ec.getNickname());
				//On itÃ¨re sur chaque Ã©quipe pour trouver tout les joueurs
				//Joueur
				joueurs = new HashMap<>();
				requeteGetJoueur = new Query(Query.allPlayerByTeam(rs.getInt("Id_Equipe")), typeRequete.QUERY);
				resultJoueur = db.getData(requeteGetJoueur).getResultSet();
				//on itÃ¨re sur chaque joueur 
				while(resultJoueur.next()) {
					BufferedImage bf1 = ImageIO.read(resultJoueur.getBinaryStream("photojoueur"));
					TypesImage im1 = new TypesImage(bf1, "png");
					joueur = new TypesPlayer(resultJoueur.getInt("Id_Utilisateur"), resultJoueur.getString("nomjoueur"), resultJoueur.getString("prenomjoueur"), im1, resultJoueur.getTimestamp("datenaissancejoueur"), resultJoueur.getTimestamp("datecontratjoueur"), resultJoueur.getTimestamp("fincontratJoueur"), -1, rs.getInt("Id_Equipe"), ec.getId(), resultJoueur.getString("username"));
					joueurs.put(joueur.getId(), joueur);
					System.out.println("\tJoueur "+joueur.getFirstName());
				}
				resultJoueur.close();
				equipe = new TypesTeam(TypesGame.intToGame(rs.getInt("Id_Jeux")), ec , joueurs, rs.getInt("Id_Equipe"));
				ec.addTeam(equipe);
				data.getTeams().put(equipe.getId(), equipe);
			}
			rs.close();
			
			r = new Query(Query.getTitleByStable(ec.getId()), typeRequete.QUERY);
			rs = db.getData(r).getResultSet();
			palmares = new ArrayList<>();
			while(rs.next()) {
				//Titre
				palmares.add(new TypesTitle(rs.getString("libelle"),rs.getTimestamp("dateobtention")));
				
			}
			rs.close();
			ec.setTitles(palmares);
			
			
		}
		System.out.println("Load team ok");
		
		
		/*On va maintenant initialiser la partie tournoi*/
		//Tournoi
		r = new Query(Query.getCalendar(), typeRequete.QUERY);
		rs = db.getData(r).getResultSet();
		ConcurrentHashMap<Integer, TypesTournament> calendrier = new ConcurrentHashMap<>();
		TypesTournament tournoi;
		Query req;
		ResultSet res;
		ArrayList<Integer> inscrits;
		while(rs.next()) {
			
			tournoi = new TypesTournament(rs.getTimestamp("datelimiteinscription"), rs.getString("nom"), TypesFame.intToRenommee(rs.getInt("Renommee")), TypesGame.intToGame(rs.getInt("id_jeux")), rs.getInt("id_tournois"));
			System.out.println("Tournoi : "+tournoi.getName());
			req = new Query(Query.getRegistered(tournoi.getId()), typeRequete.QUERY);
			res = DatabaseAccess.getInstance().getData(req).getResultSet();
			inscrits = new ArrayList<>();
			while (res.next()) {
				inscrits.add(res.getInt("id_equipe"));
			}
			res.close();
			System.out.println("\tInscrits OK");
			tournoi.setRegistered(inscrits);
			
			tournoi.setPool(getPool(tournoi, TypesGame.gameToInt(tournoi.getGame())));
			System.out.println("\tPool OK");

			calendrier.put(rs.getInt("id_tournois"), tournoi);
		}
		rs.close();
		this.data.setCalendar(calendrier);
		System.out.println("Load Calendar ok");

		
		//Classement
		ResultSet allRanking = DatabaseAccess.getInstance().getData(new Query(Query.getAllRanking(), typeRequete.QUERY)).getResultSet();
		while(allRanking.next()) {
			TypesRanking rank = new TypesRanking(TypesGame.intToGame(allRanking.getInt("id_jeux")), allRanking.getInt("id_classement"));
			
			
			rank.setStables(getCalendar(allRanking.getInt("id_classement")));
			data.getRanking().put(rank.getId(), rank);
			System.out.println("Classement du jeux : "+TypesGame.intToGame(allRanking.getInt("id_jeux")));
		}
		allRanking.close();
		
	}
	
	public ConcurrentHashMap<Integer, Integer> getCalendar(int idClassement) {
		try {
			ConcurrentHashMap<Integer, Integer> stableAndScore = new ConcurrentHashMap<>();
			
			for(TypesStable st : data.getStables().values()) {
				
				ResultSet rankingStable = DatabaseAccess.getInstance().getData(new Query(Query.getRankingByUserByGame(st.getId(), idClassement), typeRequete.QUERY)).getResultSet();
				if(rankingStable.next()) {
					stableAndScore.put(st.getId(), rankingStable.getInt("nombrepoint"));
				}
				rankingStable.close();
			}
			return stableAndScore;
		} catch (Exception e) {
			e.printStackTrace();
			return new ConcurrentHashMap<>();
		}
	}
	
	public ArrayList<TypesPool> getPool(TypesTournament tournoi, int idJeux) throws InterruptedException, SQLException {
		ArrayList<TypesPool> pools = new ArrayList<>();
		
		Query pool = new Query(Query.getPool(tournoi.getId(), idJeux), typeRequete.QUERY);
		ResultSet allPool = DatabaseAccess.getInstance().getData(pool).getResultSet();
		
		while (allPool.next()) {
			Query equipe = new Query(Query.getEquipeParPool(allPool.getInt("id_poule"), tournoi.getId(), idJeux), typeRequete.QUERY);
			ResultSet Equipe = DatabaseAccess.getInstance().getData(equipe).getResultSet();
			HashMap<TypesTeam, Integer> classement = new HashMap<>();
			while (Equipe.next()) {
				classement.put(this.data.getStables().get(Equipe.getInt("id_utilisateur")).getTeams().get(Equipe.getInt("id_equipe")), Equipe.getInt("point"));
			}
			Equipe.close();
			ArrayList<TypesMatch> matchsList = new ArrayList<>();
			//Match des poules
			if(tournoi.isFull()) {

			
				ResultSet allMatch = DatabaseAccess.getInstance().getData(new Query(Query.getMatchs(tournoi.getId(), allPool.getInt("id_poule"), idJeux), typeRequete.QUERY)).getResultSet();
				while(allMatch.next()) {
					int gagnant = 0;
					if(allMatch.getInt("Gagnant")!=0) {
						gagnant = allMatch.getInt("Gagnant");
					}
					TypesMatch match = new TypesMatch(allMatch.getTimestamp("DateMatch"), allMatch.getInt("id_equipeA"), allMatch.getInt("id_equipeB"), gagnant, allMatch.getInt("nombrePointEquipe1"), allMatch.getInt("nombrePointEquipe2"), tournoi.getId(), allPool.getInt("id_poule"));
					matchsList.add(match);
				}
				allMatch.close();
				System.out.println("\tMatch pool "+allPool.getInt("id_poule")+" OK");
			}

			pools.add(new TypesPool(allPool.getInt("id_Poule"), tournoi.getId(), classement, matchsList));
		}
		allPool.close();
		return pools;
	}
	
	
	public synchronized void deleteData(TypesID info, Types data) {
		System.out.println("DELETE DATA");
		ResponseObject r;
		HashMap<TypesID, Types> m = new HashMap<>();
		switch (info) {
		case TOURNAMENT:
			TypesInteger t = (TypesInteger)data;
			m.put(TypesID.TOURNAMENT, new TypesInteger(t.getInteger()));
			this.data.getCalendar().remove(t.getInteger());
			r = new ResponseObject(Response.DELETE_TOURNAMENT, m, null);
			sendAll(r);
			break;
		}
		
	}
	
	
	public synchronized void miseAJourData(HashMap<TypesID, Types> m) {
		System.out.println("MISE A JOUR DES DATA");
		ResponseObject r;
		TypesID infos;
		if(m.size()==1) {
			infos = m.keySet().iterator().next();
		}else {
			infos = TypesID.MATCH;
		}
		switch (infos) {
		case PLAYER:
			r = new ResponseObject(Response.UPDATE_PLAYER, m, null);
			sendAll(r);
			break;
		case TOURNAMENT:
			r = new ResponseObject(Response.UPDATE_TOURNAMENT, m, null);
			sendAll(r);
			break;
		case TEAM:
			r = new ResponseObject(Response.UPDATE_TEAM, m, null);
			sendAll(r);
			break;
		case MATCH:
			r = new ResponseObject(Response.UPDATE_MATCH, m, null);
			sendAll(r);
			break;
		} 
		
	}
	
	public void sendAll(ResponseObject response) {
		System.out.println("Send all "+response.getName());
		Collection<ConnectionClient> tabClient = allClients.values();
		synchronized (allClients) {
			for (ConnectionClient con : tabClient) {
				System.out.println("send");
				con.send(response);
			}
		}
	}
	
	public void closeClient(ChannelHandlerContext ctx) {
		Collection<ConnectionClient> tabClient = allClients.values();
		String host = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
		int port = ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();
		String uniqueID = host+":"+port;
		synchronized (tabClient) {
			tabClient.remove(uniqueID);
			nbClient--;
		}
	}
	
	public ConcurrentHashMap<String, ConnectionClient> getAllClients() {
		synchronized (allClients) {
			return allClients;
		}
	}
}
