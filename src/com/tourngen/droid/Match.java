package com.tourngen.droid;

import java.io.Serializable;
import java.util.Calendar;

public class Match implements Serializable{
	private Tournament tournament;
	private Fixture fixture;
	private Team home;
	private Team away;
	private Calendar date;
	private String info;
	private int hGoal;
	private int aGoal;
	private boolean played;
	private static final long serialVersionUID = 1L;
	
	public Match(Tournament tournament, Team home, Team away){
		this.tournament = tournament;
		this.home = home;
		this.away = away;
		this.date = Calendar.getInstance();
		this.info = "";
		this.hGoal = 0;
		this.aGoal = 0;
		this.played = false;
		
	}
	
	public Tournament getTournament() {
		return tournament;
	}
	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}
	public Fixture getFixture() {
		return fixture;
	}
	public void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}
	public Team getHome() {
		return home;
	}
	public void setHome(Team home) {
		this.home = home;
	}
	public Team getAway() {
		return away;
	}
	public void setAway(Team away) {
		this.away = away;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getHomeGoal() {
		return hGoal;
	}
	public void setHomeGoal(int hGoal) {
		this.hGoal = hGoal;
	}
	public int getAwayGoal() {
		return aGoal;
	}
	public void setAwayGoal(int aGoal) {
		this.aGoal = aGoal;
	}
	public boolean isPlayed() {
		return played;
	}
	public void setPlayed(boolean played) {
		this.played = played;
	}
	
	public String toString()
	{
		return home.getName() + " - " + away.getName();
	}
	public void revertTeams()
	{
		Team temp = home;
		home = away;
		away = temp;
	}
	
	public Match getRevertedMatch()
	{
		Match match = new Match(tournament,away,home);
		return match;
	}

}
