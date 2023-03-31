	package com.esporter.server.main;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.esporter.both.socket.Command;
import com.esporter.both.socket.Response;
import com.esporter.both.socket.ResponseObject;
import com.esporter.both.types.Types;
import com.esporter.both.types.TypesFame;
import com.esporter.both.types.TypesGame;
import com.esporter.both.types.TypesID;
import com.esporter.both.types.TypesImage;
import com.esporter.both.types.TypesInteger;
import com.esporter.both.types.TypesLogin;
import com.esporter.both.types.TypesMatch;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesPool;
import com.esporter.both.types.TypesRanking;
import com.esporter.both.types.TypesRegisterPlayer;
import com.esporter.both.types.TypesRegisterTeam;
import com.esporter.both.types.TypesStable;
import com.esporter.both.types.TypesString;
import com.esporter.both.types.TypesTeam;
import com.esporter.both.types.TypesTournament;
import com.esporter.server.model.database.DatabaseAccess;
import com.esporter.server.model.database.Query;
import com.esporter.server.model.database.Query.typeRequete;
import com.esporter.server.model.database.Result;

import io.netty.channel.ChannelHandlerContext;

public class ConnectionClient {

	private boolean isLogin = false;
	private TypesPermission role = TypesPermission.VISITOR;
	private ChannelHandlerContext channel;
	
	public ConnectionClient(ChannelHandlerContext channel) {
		this.channel = channel;
	}
	
	public ChannelHandlerContext getChannel() {
		return channel;
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
		} catch (SQLException s) {
			return -1;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return -1;
		}
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

			Thread.currentThread().interrupt();
		} catch (SQLException e) {

			return -1;
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
	
	public void send(ResponseObject response) {
		channel.writeAndFlush(response);
	}
	
	public void setPermission(TypesPermission perm) {
		role = perm;
	}
	
	
	public boolean getIsLogin() {
		return isLogin;
	}
	
	public void setIsLogin(boolean b) {
		isLogin = b;
	}
	
	public void synchronizedCommand(Command c) {
		switch(c.getSm()) {
			case CHECK_USERNAME:
				int id = ((TypesInteger)c.getInfoByID(TypesID.INT)).getInteger();
				String user = ((TypesString)c.getInfoByID(TypesID.STRING)).getString();
				Query q = new Query(Query.chckUsernameUsed(user), typeRequete.QUERY);
				
			Result r;
			try {
				r = DatabaseAccess.getInstance().getData(q);
				if(r.isError()) {
					error("Impossible de récupérer l'identifiant");
					return;
				}
				ResultSet rs = r.getResultSet();
				rs.next();
				String s = String.valueOf(rs.getInt(1));
				System.out.println("Résultat chck "+s);
				HashMap<TypesID, Types> m = new HashMap<>();
				m.put(TypesID.STRING, new TypesString(s));
				m.put(TypesID.INT, new TypesInteger(id));
				ResponseObject res = new ResponseObject(Response.SYNCHRONIZED_COMMAND,m,null);
				mainThread.getInstance().sendAll(res);
			} catch (SQLException e) {
			}
					
				break;
		}
		
	}

	public void registerStable(Command c) {
		TypesStable s = (TypesStable) c.getInfoByID(TypesID.STABLE);
		TypesLogin l = (TypesLogin) c.getInfoByID(TypesID.LOGIN);
		
		Query q = new Query(Query.addStable(l.getUsername(), l.getPassword(), s.getName(), s.getNickname()),typeRequete.INSERT_DATE_BLOB);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(s.getLogo().getImage(), "png", os);
		} catch (IOException e) {
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		q.setInputStream(is);
		
		try {
			Result r = DatabaseAccess.getInstance().getData(q);
			if(r.isError()) {
				error("Impossible de s'inscrire");
				return;
			}
			
			int id = r.getInteger();
			s.setId(id);
			mainThread.getInstance().getData().getStables().put(id, s);
			
			HashMap<TypesID, Types> m = new HashMap<>();
			m.put(TypesID.STABLE, s);
			ResponseObject res = new ResponseObject(Response.UPDATE_STABLE,m,null);
			mainThread.getInstance().sendAll(res);
			
		} catch (SQLException e) {
		}
		
		
	}
	
	public void changeScore(Command c) {
		int idTournoi = ((TypesInteger)c.getInfoByID(TypesID.TOURNAMENT)).getInteger();
		int Pool = ((TypesInteger)c.getInfoByID(TypesID.POOL)).getInteger();
		TypesMatch match = ((TypesMatch)c.getInfoByID(TypesID.MATCH));
		TypesGame jeu = mainThread.getInstance().getData().getCalendar().get(match.getIdTournament()).getGame();
		Query q;
		System.out.println("Gagnant = "+match.getWinner());
		if(match.getWinner() == match.getTeam1()) {
			q = new Query(Query.setScoreA(match.getWinner(), idTournoi, Pool, match.getTeam1(), match.getTeam2(), TypesGame.gameToInt(jeu)), typeRequete.QUERY);
		}else {
			q = new Query(Query.setScoreB(match.getWinner(), idTournoi, Pool, match.getTeam1(), match.getTeam2(), TypesGame.gameToInt(jeu)), typeRequete.QUERY);
		}
		try {
			DatabaseAccess.getInstance().getData(q);
			DatabaseAccess.getInstance().getData(new Query(Query.remplissagePoule(Pool, idTournoi, TypesGame.gameToInt(jeu), match.getWinner(), match.getTeam1(), match.getTeam2()),typeRequete.PROCEDURE));
		} catch (SQLException e) {
		}
		
		TypesPool pool = mainThread.getInstance().getData().getCalendar().get(idTournoi).getPool().get(Pool-1);
		for(TypesMatch m : pool.getMatchs()) {
			if(m.equals(match)) {
				m.setPoint(match.getTeam1Score(), match.getTeam2Score());
				break;
			}
		}
		
		
		
		TypesTournament t = mainThread.getInstance().getData().getCalendar().get(match.getIdTournament());
		ArrayList<TypesPool> listP = t.getPool();
		
		for(TypesPool p : listP) {
			if(p.getId() == match.getIdPool()) {
				p.getPoint().put(mainThread.getInstance().getData().getTeams().get(match.getWinner()), pool.getPoint().get(mainThread.getInstance().getData().getTeams().get(match.getWinner()+1)));
			}
		}
		
		try {
			listP = mainThread.getInstance().getPool(t, TypesGame.gameToInt(t.getGame()));
			mainThread.getInstance().getData().getCalendar().get(t.getId()).setPool(listP);
		} catch (IllegalArgumentException e) {

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();
		} catch (SQLException e) {

		}
		
		
		TypesPool p = listP.get(4);
		if(p.getPoint().size()!=4 && !p.poolFinished()) {
			
			try {
				DatabaseAccess.getInstance().getData(new Query(Query.rencontrepouleFinale(idTournoi, Pool),typeRequete.PROCEDURE));
				listP = mainThread.getInstance().getPool(t, TypesGame.gameToInt(t.getGame()));
				mainThread.getInstance().getData().getCalendar().get(t.getId()).setPool(listP);
			} catch (InterruptedException e) {

				Thread.currentThread().interrupt();
			} catch (SQLException e) {

			}
			
		}
	
		
		/*HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.MATCH, match);
		m.put(TypesID.TOURNAMENT, new TypesInteger(idTournoi));
		m.put(TypesID.POOL, new TypesInteger(Pool));
		
		ResponseObject r = new ResponseObject(Response.UP,m,null);*/
		
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TOURNAMENT, mainThread.getInstance().getData().getCalendar().get(t.getId()));
		ResponseObject r = new ResponseObject(Response.UPDATE_TOURNAMENT,m,null);
		
		
		mainThread.getInstance().sendAll(r);
		
		TypesTournament tournois = mainThread.getInstance().getData().getCalendar().get(t.getId());
		if (tournois.getPool().get(4).poolFinished()) {
			int multiplicateur = 1;
			int point = 100;

			//100 60 30 10
			switch(tournois.getFame()) {
			case INTERNATIONAL:
				multiplicateur = 3;
				break;
			case NATIONAL:
				multiplicateur = 2;
				break;
			case LOCAL:
				multiplicateur = 1;
				break;
			}
			
			Comparator<TypesTeam> comp = new Comparator<TypesTeam>() {
				
				@Override
				public int compare(TypesTeam o1, TypesTeam o2) {
					if(tournois.getPool().get(4).getPoint().get(o1).compareTo(tournois.getPool().get(4).getPoint().get(o2))==0 ) {
						return o1.getId() - (o2.getId());
					}
					return tournois.getPool().get(4).getPoint().get(o1).compareTo(tournois.getPool().get(4).getPoint().get(o2));
				}
			};
			
			List<TypesTeam> listeTeamSort = new ArrayList<>(tournois.getPool().get(4).getPoint().keySet());
			Collections.sort(listeTeamSort, comp.reversed());
			int passage = 1;
			for(TypesTeam team : listeTeamSort) {
				switch(passage) {
				case 1:
					point = 100*multiplicateur;
					break;
				case 2:
					point = 60*multiplicateur;
					break;
				case 3:
					point = 30*multiplicateur;
					break;
				case 4:
					point = 10*multiplicateur;
					break;
				}
				passage++;
				try {
					DatabaseAccess.getInstance().getData(new Query(Query.setScore(team.getStable().getId(),TypesGame.gameToInt(jeu) , point),typeRequete.PROCEDURE));
				} catch (IllegalArgumentException e) {

				} catch (SQLException e) {

				}
			}
			for(TypesRanking ranking : mainThread.getInstance().getData().getRanking().values()) {
				if(ranking.getGame()==jeu) {
					mainThread.getInstance().getData().getRanking().get(ranking.getId()).setStables(mainThread.getInstance().getCalendar(ranking.getId()));
					HashMap<TypesID, Types> m2 = new HashMap<>();
					m2.put(TypesID.RANKING, mainThread.getInstance().getData().getRanking().get(ranking.getId()));
					ResponseObject r2 = new ResponseObject(Response.UPDATE_RANKING,m2,null);
					mainThread.getInstance().sendAll(r2);
					break;
				}
			}
			
			
		}
		
		
	}
	
	
	public void modifyTournament(TypesTournament t) {
		mainThread.getInstance().getData().getCalendar().put(t.getId(), t);
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TOURNAMENT, t);
		mainThread.getInstance().miseAJourData(m);
	}

	public void ajouterEquipe(TypesRegisterTeam equipe) {
		Result res = null;
		try {
			Query r = new Query(Query.addTeam(TypesGame.gameToInt(equipe.getGame()), equipe.getIdStable()), typeRequete.FUNCTION);
			res = DatabaseAccess.getInstance().getData(r);
			if (res.isError()) {
				error("Erreur dans la creation des equipes veuillez ressayer plus tard");
			}
		} catch (SQLException e) {
			error("Erreur dans la creation des equipes veuillez ressayer plus tard");
		}
		try {

			Query temp = new Query(Query.getStableInfo(equipe.getIdStable()),typeRequete.QUERY);
			Result tempRes = DatabaseAccess.getInstance().getData(temp);
			ResultSet rs = tempRes.getResultSet();
			rs.next();
			BufferedImage bf1 = ImageIO.read(rs.getBinaryStream("logoecurie"));
			TypesImage im1 = new TypesImage(bf1, "png");

			TypesStable ecurie = new TypesStable(rs.getString("nomecurie"), im1, rs.getString("diminutifecurie"), equipe.getIdStable());
			rs.close();
			TypesTeam eq = new TypesTeam(equipe.getGame(), ecurie, null,res.getInteger());
			HashMap<Integer, TypesPlayer> joueurs = new HashMap<>();
			for (TypesRegisterPlayer jou : equipe.getPlayers()) {
				TypesPlayer joueur = jou.getPlayer();
				Query reqJou = new Query(Query.addPlayer(jou.getLogin().getUsername(), jou.getLogin().getPassword(), joueur.getName(),joueur.getFirstName(), res.getInteger(), 1),typeRequete.INSERT_DATE_BLOB);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(joueur.getImage().getImage(), "png", os);
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				reqJou.setInputStream(is);
				reqJou.setDates(joueur.getBirthDate(), joueur.getContractStartDate(), joueur.getContractEndDate());
				Result resJou = DatabaseAccess.getInstance().getData(reqJou);
				if (resJou.isError()) {
					erreurAjoutEquipe(res.getInteger());
					error("Erreur dans la creation des equipes veuillez ressayer plus tard");
					return;
				}
				joueur.setId(resJou.getInteger());
				joueurs.put(resJou.getInteger(), joueur);
			}
			eq.setPlayers(joueurs);
			mainThread.getInstance().getData().getStables().get(eq.getStable().getId()).getTeams().put(eq.getId(), eq);
			HashMap<TypesID, Types> m = new HashMap<>();
			m.put(TypesID.TEAM, eq);
			mainThread.getInstance().miseAJourData(m);

		} catch (SQLException e) {
			erreurAjoutEquipe(res.getInteger());
			error("Erreur dans l'ajout de cette equipe, veuillez ressayer plus tard");
		} catch (IOException e) {
			erreurAjoutEquipe(res.getInteger());
			error("Erreur dans l'ajout de cette equipe, veuillez ressayer plus tard");
		}
	}

	public void erreurAjoutEquipe(int idEquipe) {
		try {
			Query r = new Query(Query.removePlayerByTeam(idEquipe),typeRequete.QUERY);
			DatabaseAccess.getInstance().getData(r);

			r = new Query(Query.removeTeam(idEquipe),typeRequete.QUERY);
			DatabaseAccess.getInstance().getData(r);
		} catch (SQLException e) {

		}


	}

	public void modifyTeam(TypesTeam team) {
		Iterator<TypesPlayer> ite = team.getPlayers().values().iterator();
		while(ite.hasNext()) {
			TypesPlayer player = ite.next();
			Query r = new Query(Query.modifyPlayer(player.getFirstName(), player.getName(), player.getId()),typeRequete.MODIFY_DATE_BLOB);
			r.setDates(player.getBirthDate(),player.getContractStartDate(),player.getContractEndDate());
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ImageIO.write(player.getImage().getImage(), "png", os);
			} catch (IOException e) {

			}
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			r.setInputStream(is);
			try {
				Result res = DatabaseAccess.getInstance().getData(r);
				if (res.isError()) {
					System.out.println("Error on insert");
				}
			} catch (SQLException e) {

			}
		}
		mainThread.getInstance().getData().getStables().get(team.getStable().getId()).getTeams().put(team.getId(), team);
		HashMap<TypesID, Types> m = new HashMap<>();
		m.put(TypesID.TEAM, team);
		mainThread.getInstance().miseAJourData(m);

	}
	
	public void deleteTournament(int t) {
		Query req = new Query(Query.removeTournament(t),typeRequete.QUERY);
		try {
			Result r = DatabaseAccess.getInstance().getData(req);
			if (r.isError()) {
				error("Le tournoi ne peut pas etre supprim�");
				return;
			}
			mainThread.getInstance().deleteData(TypesID.TOURNAMENT, new TypesInteger(t));
		} catch (SQLException e) {

		}
	}


	public void registerTournament(int id_Tournament, int id_Player) {

		try {
			//Recuperer equipe
			Query r = new Query(Query.getTeamByPlayer(id_Player),typeRequete.QUERY);
			Result res = DatabaseAccess.getInstance().getData(r);
			res.getResultSet().next();
			int id_team = res.getResultSet().getInt("id_equipe");
			res.getResultSet().close();

			TypesTournament tournament;
			r = new Query(Query.getTournamentByID(id_Tournament), typeRequete.QUERY);
			res = DatabaseAccess.getInstance().getData(r);
			ResultSet rs = res.getResultSet();
			rs.next();
			tournament = mainThread.getInstance().getData().getCalendar().get(id_Tournament);
			rs.close();
			Query request = new Query(Query.getTeamGame(id_team), typeRequete.QUERY);
			ResultSet resultset = DatabaseAccess.getInstance().getData(request).getResultSet();
			resultset.next();

			TypesGame gameTeam= TypesGame.intToGame(resultset.getInt("id_jeux"));
			resultset.close();

			System.out.println("Jeu tournoi :"+tournament.getGame()+", jeux equipe : "+gameTeam);
			if (tournament.getGame() != gameTeam) {
				error("Vous ne pouvez pas vous inscrire, ce jeu n'est celui de votre equipe");
				return;
			}
	
				
			r = new Query(Query.registerTournament(TypesGame.gameToInt(tournament.getGame()), id_Tournament, id_team), typeRequete.PROCEDURE);
			res = DatabaseAccess.getInstance().getData(r);
			if (res.isError()) {
				error("Vous etes deja inscrit");
				return;
			}
	
	
	
	
	
			tournament.registerTeam(id_team);
			if(tournament.isFull()) {
				//on charge le tournoi
				System.out.println("Tournoi full, chargement de la poule");
				TimeUnit.SECONDS.sleep(5);
				tournament.setPool(mainThread.getInstance().getPool(tournament, TypesGame.gameToInt(tournament.getGame())));
			}
	
			//Il manque le get Poule
	
			mainThread.getInstance().getData().getCalendar().put(tournament.getId(), tournament);
			HashMap<TypesID, Types> m = new HashMap<>();
			m.put(TypesID.TOURNAMENT, tournament);
			mainThread.getInstance().miseAJourData(m);
	
		} catch (InterruptedException | SQLException e) {

			Thread.currentThread().interrupt();
			error("Vous etes deja inscrit");
		}
	}
	
	public void desinscriptionTournoi(int id_Tournoi, int id_Joueur, int id_Jeu) {
		try {
			//Recuperer equipe
			Query r = new Query(Query.getTeamByPlayer(id_Joueur),typeRequete.QUERY);
			Result res = DatabaseAccess.getInstance().getData(r);
			res.getResultSet().next();
			int id_equipe = res.getResultSet().getInt("id_equipe");
			res.getResultSet().close();
			
			if(mainThread.getInstance().getData().getCalendar().get(id_Tournoi).isFull()) {
				error("Le tournoi a deja commencé vous ne pouvez plus vous desinscrire");
				
			}
	
			r = new Query(Query.unregisterTournament(id_Jeu, id_Tournoi, id_equipe), typeRequete.PROCEDURE);
			res = DatabaseAccess.getInstance().getData(r);
			if (res.isError()) {
				error("Vous n'êtes pas inscrit");
				return;
			}
	
	
			TypesTournament tournoi;
			r = new Query(Query.getTournamentByID(id_Tournoi), typeRequete.QUERY);
			res = DatabaseAccess.getInstance().getData(r);
			ResultSet rs = res.getResultSet();
			rs.next();
			tournoi = new TypesTournament(rs.getTimestamp("datelimiteinscription"), rs.getString("nom"), TypesFame.intToRenommee(rs.getInt("Renommee")), TypesGame.intToGame(rs.getInt("id_jeux")), rs.getInt("id_tournois"));
			rs.close();
	
	
			//TournoiInfo tournoi = mainThread.getInstance().getData().getCalendrier().get(id_Tournoi).clone();
			tournoi.unregisterTeam(id_equipe);
	
			mainThread.getInstance().getData().getCalendar().put(tournoi.getId(), tournoi);
			HashMap<TypesID, Types> m = new HashMap<>();
			m.put(TypesID.TOURNAMENT, tournoi);
			mainThread.getInstance().miseAJourData(m);
	
	
		} catch (SQLException e) {

		}
	
	}
	
	public void logout() {
		setIsLogin(false);
		setPermission(TypesPermission.VISITOR);
	}
	
	public void errorPermission() {
		ResponseObject r = new ResponseObject(Response.ERROR_PERMISSION, null, null);
		send(r);
	}
	
	public void error(String s) {
		ResponseObject r = new ResponseObject(Response.ERROR, null, s);
		send(r);
	}
	
	public void ErrorLogin() {
		ResponseObject r = new ResponseObject(Response.ERROR_LOGIN, null, null);
		send(r);
	}
	
	public void login(Command c) {
		TypesLogin l = (TypesLogin) c.getInfoByID(TypesID.LOGIN);
		int result = login(l.getUsername(), l.getPassword());
		if (result == -1) {
			ErrorLogin();
		} else {
			try {
				Result res = DatabaseAccess.getInstance().getData(new Query(Query.getUserByID(result), typeRequete.QUERY));
				if (res.isError()) {
					ErrorLogin();
				}
				ResultSet rs = res.getResultSet();
				rs.next();
				int perm = rs.getInt("id_role");
				setPermission(perm);
				HashMap<TypesID,Types> m = new HashMap<>();
				switch (perm) {
				case 1:
					m.put(TypesID.PERMISSION, TypesPermission.ORGANIZER);
					break;
				case 2:
					m.put(TypesID.PERMISSION, TypesPermission.REFEREE);
					break;
				case 3:
					m.put(TypesID.PERMISSION, TypesPermission.PLAYER);
					Result r = DatabaseAccess.getInstance().getData(new Query(Query.getTeamByPlayer(result), typeRequete.QUERY));
					ResultSet resultset = r.getResultSet();
					resultset.next();
					int idTeam = resultset.getInt("id_equipe");
					r = DatabaseAccess.getInstance().getData(new Query(Query.getStableByTeam(idTeam), typeRequete.QUERY));
					resultset = r.getResultSet();
					resultset.next();
					
					BufferedImage bf = ImageIO.read(rs.getBinaryStream("photojoueur"));
					TypesImage im = new TypesImage(bf, "png");
					m.put(TypesID.PLAYER, new TypesPlayer(result, rs.getString("nomjoueur"), rs.getString("prenomjoueur"),im, rs.getTimestamp("datenaissancejoueur"), rs.getTimestamp("datecontratjoueur"), rs.getTimestamp("fincontratJoueur"), rs.getInt("id_nationalite"), rs.getInt("id_equipe"), resultset.getInt("id_utilisateur"), rs.getString("username")));
					break;
				case 4:
					m.put(TypesID.PERMISSION, TypesPermission.STABLE);
					BufferedImage bf1 = ImageIO.read(rs.getBinaryStream("logoecurie"));
					TypesImage im1 = new TypesImage(bf1, "png");
					m.put(TypesID.STABLE, new TypesStable(rs.getString("nomecurie"), im1, rs.getString("diminutifecurie"), result));
					break;
	
				}
				rs.close();
				ResponseObject r = new ResponseObject(Response.LOGIN, m, null);
				send(r);
				setIsLogin(true);
			} catch (SQLException e) {

				ErrorLogin();
			} catch (IOException e) {

			}
	
	
	
	
		}
	}

	
}
