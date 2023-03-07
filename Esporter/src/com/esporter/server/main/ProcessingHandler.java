package com.esporter.server.main;

import java.net.InetSocketAddress;
import java.util.HashMap;

import com.esporter.both.data.Data;
import com.esporter.both.socket.Command;
import com.esporter.both.socket.Response;
import com.esporter.both.socket.ResponseObject;
import com.esporter.both.types.Types;
import com.esporter.both.types.TypesID;
import com.esporter.both.types.TypesInteger;
import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.TypesRegisterTeam;
import com.esporter.both.types.TypesTeam;
import com.esporter.both.types.TypesTournament;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    		Command c = null;

    		if (msg instanceof Command)
    			c = (Command)msg;
    		System.out.println("Message recu : "+c.getName());
    		ConnectionClient client = getClientData(ctx);
    		if(client.getIsLogin()) {

    			switch(c.getName()) {
    			case LOGOUT : 
    				client.logout();
    				break;
    			case ADD_TEAM:
    				if(client.getRole()!=TypesPermission.STABLE) {
    					client.errorPermission();
    					break;
    				}
    				TypesRegisterTeam equipe = (TypesRegisterTeam) c.getInfoByID(TypesID.TEAM);
    				client.ajouterEquipe(equipe);
    				break;
    			case MODIFY_TEAM:
    				if(client.getRole()!=TypesPermission.STABLE) {
    					client.errorPermission();
    					break;
    				}
    				TypesTeam team = (TypesTeam) c.getInfoByID(TypesID.TEAM);
    				client.modifyTeam(team);
    				break;
    			case ADD_TOURNAMENT:
    				client.ajouterTournoi((TypesTournament)c.getInfoByID(TypesID.TOURNAMENT));
    				break;
    			case REGISTER_TOURNAMENT:
    				client.registerTournament(((TypesInteger)c.getInfoByID(TypesID.TOURNAMENT)).getInteger(), ((TypesInteger)c.getInfoByID(TypesID.PLAYER)).getInteger());
    				break;
    			case UNREGISTER_TOURNAMENT:
    				client.desinscriptionTournoi(((TypesInteger)c.getInfoByID(TypesID.TOURNAMENT)).getInteger(), ((TypesInteger)c.getInfoByID(TypesID.PLAYER)).getInteger(), ((TypesInteger)c.getInfoByID(TypesID.GAME)).getInteger());
    				break;
    			case CALENDAR:
    				break;
    			case STABLE:
    				//register new Stable
    				client.registerStable(c);
    				break;
    			case DELETE_TOURNAMENT:
    				client.deleteTournament(((TypesInteger)c.getInfoByID(TypesID.TOURNAMENT)).getInteger());
    				break;
    			case MODIFY_TOURNAMENT:
    				client.modifyTournament(((TypesTournament)c.getInfoByID(TypesID.TOURNAMENT)));
    				break;
    			case SCORE:
    				client.changeScore(c);
    				break;
    			case SYNCHRONIZED_COMMAND:
    				client.synchronizedCommand(c);
    			default:
    			}

    		} else {
    			switch(c.getName()) {
    			case LOGIN : 
    				client.login(c);
    				break;
    			case CALENDAR:
    				break;
    			case STABLE:
    				client.registerStable(c);
    				break;
    			case INIT:

    				Data d = mainThread.getInstance().getData();
    				HashMap<TypesID,Types> m = new HashMap<>();
    				m.put(TypesID.ALL, d);
    				ResponseObject r = new ResponseObject(Response.UPDATE_ALL, m, null);
    				client.send(r);
    				System.out.println("Send init");
    				break;
    			default:
    				client.errorPermission();
    			}
    		}
        
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	mainThread.getInstance().ajouterClient(new ConnectionClient(ctx), ctx);
    	System.out.println("Connection acceptée");
    	System.out.println("En attente d'une connexion");
    }
    
    public ConnectionClient getClientData(ChannelHandlerContext ctx) {
    	String host = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
		int port = ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();
		String uniqueID = host+":"+port;
    	return mainThread.getInstance().getAllClients().get(uniqueID);
    }
}