package com.esporter.both.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TypesStable implements Types, Serializable,  Comparable<TypesStable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3925659840033195163L;
	private String name;
	private com.esporter.both.types.TypesImage logo;
	private String nickname;
	private HashMap<Integer,TypesTeam> teams;
	private ArrayList<TypesTitle> titles;
	private int id;
	
	public TypesStable(String name, TypesImage logo, String nickname, int id) {
		this.name = name;
		this.logo = logo;
		this.nickname = nickname;
		this.id = id;
		this.teams = new HashMap<>();
		this.titles = new ArrayList<>();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setTitles(ArrayList<TypesTitle> titles) {
		this.titles = titles;
	}
	
	public com.esporter.both.types.TypesImage getLogo() {
		return logo;
	}

	public String getNickname() {
		return nickname;
	}


	public HashMap<Integer, TypesTeam> getTeams() {
		return teams;
	}
	
	public ArrayList<TypesTitle> getTitles() {
		return titles;
	}
	
	public void addTeam(TypesTeam e) {
		this.teams.put(e.getId(), e);
	}
	
	public void ajouterJoueur(TypesPlayer j) throws IllegalArgumentException {
		if (this.teams.get(j.getIdTeam())==null) {
			throw new IllegalArgumentException();
		}
		this.teams.get(j.getIdTeam()).addPlayer(j);
	}
	
	@Override
	public int compareTo(TypesStable o) {
		return this.getName().compareTo(o.getName());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypesStable other = (TypesStable) obj;
		return this.id == other.getId();
	}
	
	
	

}
