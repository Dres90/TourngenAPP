package com.tourngen.droid;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable{
	
	private String name;
	private String email;
	private String info;
	private Tournament tournament;
	private static final long serialVersionUID = 1L;
	
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
	
	
	public ArrayList<Match> getMatches()
	{
		ArrayList<Match> matches = new ArrayList<Match>();
		ArrayList<Fixture> fixtures = new ArrayList<Fixture>(tournament.getFixtures());
		
		for (int i = 0; i<fixtures.size();i++)
		{
			Fixture fixture = fixtures.get(i);
			ArrayList<Match> fMatches = fixture.getMatches();
			for (int j = 0; j<fMatches.size();j++)
			{
				Match match = fMatches.get(j);
				if (match.getHome().equals(this)||match.getAway().equals(this))
				{
					matches.add(match);
				}
			}
		}
		
		return matches;
	}

}
