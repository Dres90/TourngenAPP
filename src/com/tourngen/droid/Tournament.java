package com.tourngen.droid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Tournament implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	private String name;
	private ArrayList<Team> teams;
	private ArrayList<Fixture> fixtures;
	private Calendar startDate;
	private Calendar endDate;
	private String info;
	private boolean ispublic;
	private boolean homeandaway;
	public boolean locked;
	

	public Tournament(String name)
	{
		this.setName(name);
		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		info = "";
		ispublic = false;
		homeandaway = false;
		locked = false;
		teams = new ArrayList<Team>();
		fixtures = new ArrayList<Fixture>();
		
	}
	
	public Calendar getStartDate() {
		return startDate;
	}


	public void setStartDate(Calendar startDate) {
		if (!locked)
		{
			this.startDate = startDate;
		}
		
	}


	public Calendar getEndDate() {
		return endDate;
	}


	public void setEndDate(Calendar endDate) {
		if (!locked)
		{
			this.endDate = endDate;
		}
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public boolean isIspublic() {
		return ispublic;
	}


	public void setIspublic(boolean ispublic) {
		if (!locked)
		{
			this.ispublic = ispublic;
		}
	}


	public boolean isHomeandaway() {
		return homeandaway;
	}


	public void setHomeandaway(boolean homeandaway) {
		if (!locked)
		{
			this.homeandaway = homeandaway;
		}
	}


	public boolean isLocked() {
		return locked;
	}


	public void lock() {
		this.locked = true;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(!locked)
		{
			this.name = name;
		}
	}
	public ArrayList<Team> getTeams() {
		return teams;
	}
	public void setTeams(ArrayList<Team> teams) {
		if(!locked)
		{
			this.teams = teams;
		}
	}
	public ArrayList<Fixture> getFixtures() {
		return fixtures;
	}
	public void setFixtures(ArrayList<Fixture> fixtures) {
		if(!locked)
		{
			this.fixtures = fixtures;
		}
	}
	public String toString()
	{
		return name;
	}
}
