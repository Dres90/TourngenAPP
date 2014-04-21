package com.tourngen.droid;

import java.io.Serializable;
import java.util.ArrayList;

public class Fixture implements Serializable{
	
	private Tournament tournament;
	private int number;
	private String info;
	private ArrayList<Match> matches;
	private static final long serialVersionUID = 0L;
	
	public Fixture(Tournament tournament, int number)
	{
		this.tournament=tournament;
		this.number=number;
		this.info="";
		matches = new ArrayList<Match>();
	}
	
	public Tournament getTournament() {
		return tournament;
	}
	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public ArrayList<Match> getMatches() {
		return matches;
	}
	public void setMatches(ArrayList<Match> matches) {
		this.matches = matches;
	}
	
	public String toString()
	{
		return "Fixture #"+number;
	}

}
