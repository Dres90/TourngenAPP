package com.tourngen.droid.utils;

import com.tourngen.droid.objects.Fixture;
import com.tourngen.droid.objects.Match;
import com.tourngen.droid.objects.Team;
import com.tourngen.droid.objects.Tournament;

public class DataHolder {
	private Tournament tournament;
	public Tournament getTournament() {return tournament;}
	public void setTournament(Tournament tournament) 
	{
		this.tournament = tournament;
		this.team = null;
		this.fixture = null;
		this.match = null;
	}
	
	private Team team;
	public Team getTeam() {return team;}
	public void setTeam(Team team) {this.team = team;}
	
	private Fixture fixture;
	public Fixture getFixture() {return fixture;}
	public void setFixture(Fixture fixture) {this.fixture = fixture;}
	
	private Match match;
	public Match getMatch() {return match;}
	public void setMatch(Match match) {this.match = match;}
	
	private static final DataHolder holder = new DataHolder();
	public static DataHolder getInstance() {return holder;}
}

