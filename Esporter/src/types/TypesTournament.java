package types;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;


public class TypesTournament implements Types, Serializable, Comparable<TypesTournament> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2465967083363947302L;
	private Date registerDate;
	private String name;
	private TypesFame fame;
	// private List<Jeu> jeux;
	private TypesGame game;
	private int id;
	private ArrayList<TypesPool> pool;
	private ArrayList<Integer> registered;
	
	public TypesTournament(Date registerDate, String name, TypesFame fame, TypesGame game, int id) {
		this.registerDate = registerDate;
		this.name = name;
		this.fame = fame;
		this.game = game;
		this.id = id;
		this.registered = new ArrayList<>();
		this.pool = new ArrayList<>();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	

	public Date getRegisterDate() {
		return registerDate;
	}

	public String getName() {
		return name;
	}

	public TypesFame getFame() {
		return fame;
	}

	public TypesGame getGame() {
		return game;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(registerDate, id, game, name, fame);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypesTournament other = (TypesTournament) obj;
		return id == other.id;
	}
	
	public ArrayList<Integer> getRegistered() {
		return registered;
	}
	
	public ArrayList<TypesPool> getPool() {
		return pool;
	}
	
	public void setRegistered(ArrayList<Integer> inscris) {
		this.registered = inscris;
	}

	@Override
	public int compareTo(TypesTournament o) {
		return this.getRegisterDate().compareTo(o.getRegisterDate());
	}
	
	
	
	
	
	

}