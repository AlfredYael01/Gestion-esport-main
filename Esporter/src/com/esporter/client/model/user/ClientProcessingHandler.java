package com.esporter.client.model.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.esporter.both.data.Data;
import com.esporter.both.socket.Response;
import com.esporter.both.socket.ResponseObject;
import com.esporter.both.types.Types;
import com.esporter.both.types.TypesID;
import com.esporter.both.types.TypesInteger;
import com.esporter.both.types.TypesMatch;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesRanking;
import com.esporter.both.types.TypesStable;
import com.esporter.both.types.TypesTeam;
import com.esporter.both.types.TypesTournament;
import com.esporter.both.types.exception.ExceptionError;
import com.esporter.both.types.exception.ExceptionInvalidPermission;
import com.esporter.client.vue.MasterFrame;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientProcessingHandler extends ChannelInboundHandlerAdapter {
 
	private Map<Integer, Types> decodeId;
	private User user;
	
	public ClientProcessingHandler(User user) {
		this.user = user;
		decodeId = new ConcurrentHashMap<>();
	}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ResponseObject r = (ResponseObject)msg;
		System.out.println(r .getName());
		switch(r.getName()) {
		case SYNCHRONIZED_COMMAND:
			int id= ((TypesInteger)r.getInfoByID(TypesID.INT)).getInteger();
			if(decodeId.containsKey(id)) {
				decodeId.put(id, r.getInfoByID(TypesID.STRING));
			}
			synchronized (this.user.getCom().getWaitingThread()) {
				this.user.getCom().getWaitingThread().notify();
			}
			
			break;
		case ERROR_LOGIN:
			this.user.getWaiting().setActualState(Response.ERROR_LOGIN);
			break;
		case ERROR_PERMISSION:
			MasterFrame.getInstance().fireError(new ExceptionInvalidPermission("Vous n'avez pas la permission d'effectuer ceci"), false, false);
			System.out.println("ERREUR PERMISSION");
			this.user.getWaiting().setActualState(Response.ERROR_PERMISSION);
			break;
		case LOGIN:
			receiveLogin(r);
			this.user.getWaiting().setActualState(r.getName());
			break;
		case UPDATE_CALENDAR:
			//ok
			break;
		case UPDATE_STABLE:
			TypesStable stable = (TypesStable)r.getInfoByID(TypesID.STABLE);
			this.user.getData().getStables().put(stable.getId(), stable);
			MasterFrame.getInstance().dataUpdate();
			break;
		case UPDATE_TEAM:
			TypesTeam team = (TypesTeam)r.getInfoByID(TypesID.TEAM);
			this.user.getData().getStables().get(team.getStable().getId()).getTeams().put(team.getId(), team);
			if(this.user.getPermission() != TypesPermission.VISITOR) {
				if (((TypesStable)this.user.getInfo()).getId() == team.getStable().getId()) {
					((TypesStable)this.user.getInfo()).addTeam(team);
				}
			}
			this.user.getData().getTeams().put(team.getId(), team);
			MasterFrame.getInstance().dataUpdate();
			break;
		case UPDATE_TOURNAMENT:
			TypesTournament tournament = (TypesTournament)r.getInfoByID(TypesID.TOURNAMENT);
			this.user.getData().getCalendar().put(tournament.getId(), tournament);
			MasterFrame.getInstance().dataUpdate();
			break;
		case UPDATE_ALL:
			this.user.setData((Data)r.getInfo().get(TypesID.ALL));
			this.user.getWaiting().setActualState(Response.UPDATE_ALL);
			
			break;
		case ERROR:
			MasterFrame.getInstance().fireError(new ExceptionError(r.getError()), false, false);
			System.out.println("ERREUR");
			break;
		case DELETE_TOURNAMENT:
			this.user.getData().getCalendar().remove(((TypesInteger)r.getInfoByID(TypesID.TOURNAMENT)).getInteger());
			MasterFrame.getInstance().dataUpdate();
			break;
		case UPDATE_MATCH:
			int idTournoi = ((TypesInteger)r.getInfoByID(TypesID.TOURNAMENT)).getInteger();
			int Pool = ((TypesInteger)r.getInfoByID(TypesID.POOL)).getInteger();
			TypesMatch match = ((TypesMatch)r.getInfoByID(TypesID.MATCH));
			
			for(TypesMatch m : this.user.getData().getCalendar().get(idTournoi).getPool().get(Pool).getMatchs()) {
				if(m.equals(match)) {
					m.setPoint(match.getTeam1Score(), match.getTeam2Score());
					break;
				}
			}
			MasterFrame.getInstance().dataUpdate();
			break;
		case UPDATE_RANKING:
			TypesRanking ranking = ((TypesRanking)r.getInfoByID(TypesID.RANKING));
			this.user.getData().getRanking().put(ranking.getId(), ranking);
			MasterFrame.getInstance().dataUpdate();
			break;
		default:
			break;
		
		
		}
		this.user.getWaiting().setActualState(r.getName());
        
    }
    
    
    
    public void receiveLogin(ResponseObject r) {
		TypesPermission perm = TypesPermission.VISITOR;
		if (r.getInfo().containsKey(TypesID.PERMISSION)) {
			perm = (TypesPermission) r.getInfoByID(TypesID.PERMISSION);
		}
		this.user.setPermission(perm);
		if (r.getInfo().containsKey(TypesID.STABLE)) {
			this.user.setInfo(r.getInfoByID(TypesID.STABLE));

		}
		if (r.getInfo().containsKey(TypesID.PLAYER)) {
			this.user.setInfo(r.getInfoByID(TypesID.PLAYER));
		}
	}
}
