package com.tourngen.droid;

import java.io.Serializable;

public class Team implements Serializable{
	
	private String name;
	private String email;
	private String info;
	private Tournament tournament;
	private static final long serialVersionUID = 0L;
	
	public Team (String name, Tournament tournament)
	{
		this.setName(name);
		this.setTournament(tournament);
	}
	
	public Tournament getTournament() {
		return tournament;
	}
	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String toString()
	{
		return name;
	}

}
