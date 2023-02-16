package com.esporter.both.socket;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.esporter.both.types.Types;
import com.esporter.both.types.TypesID;
import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesStable;
import com.esporter.both.types.TypesTeam;
import com.esporter.both.types.TypesTournament;

public class ResponseObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4180943570637657256L;
	private Response name;
	private Map<TypesID,Types> info = new HashMap<>();
	private String error;
	
	
	
	public ResponseObject(Response name, Map<TypesID,Types> info, String error) {
		this.name = name;
		this.info = info;
		this.error = error;
	}
	
	public Response getName() {
		return name;
	}

	public String getError() {
		return error;
	}
	
	public Map<TypesID, Types> getInfo() {
		return info;
	}
	
	public Types getInfoByID(TypesID id) {
		if (this.info.get(id)==null) {
			return null;
		}
		return this.info.get(id);
	}
	
	public String toString() {
		String output = "[MAP";
		for (TypesID key : info.keySet()) {
	        output+=(key + "=" + info.get(key) + ", ");
	    }
		return output;
	}
	
}
