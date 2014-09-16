package com.tourngen.droid.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Fixture implements Serializable{
	
	private Tournament tournament;
	private int number;
	private String info;
	private ArrayList<Match> matches;
	private static final long serialVersionUID = 1L;
	private int extId;
	private Calendar last_updated;
	
	public int getExtId() {
		return extId;
	}

	public void setExtId(int extId) {
		this.extId = extId;
	}

	public Calendar getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(Calendar last_updated) {
		this.last_updated = last_updated;
	}

	public Fixture(Tournament tournament, int number)
	{
		this.tournament=tournament;
		this.number=number;
		this.info="";
		this.matches = new ArrayList<Match>();
		this.last_updated = Calendar.getInstance();
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
