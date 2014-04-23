package com.tourngen.droid;

public class DataHolder {
	private Tournament tournament;
	public Tournament getTournament() {return tournament;}
	public void setTournament(Tournament tournament) {this.tournament = tournament;}
	
	private Team team;
	public Team getTeam() {return team;}
	public void setTeam(Team team) {this.team = team;}
	
	private Match match;
	public Match getMatch() {return match;}
	public void setMatch(Match match) {this.match = match;}
	
	private String username;
	public String getUser() {return username;}
	public void setUser(String username) {this.username = username;}
	
	private static final DataHolder holder = new DataHolder();
	public static DataHolder getInstance() {return holder;}
}

