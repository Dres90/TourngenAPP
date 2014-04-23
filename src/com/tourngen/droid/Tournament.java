package com.tourngen.droid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

public class Tournament implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String name;
	private ArrayList<Team> teams;
	private ArrayList<Fixture> fixtures;
	private Calendar startDate;
	private Calendar endDate;
	private String info;
	private boolean ispublic;
	private boolean homeandaway;
	private boolean locked;
	
	private int extId;
	private Calendar last_updated;
	private int privilege;
	
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

	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public boolean getLocked(){
		return this.locked;
	}
	

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
		last_updated = Calendar.getInstance();
		privilege = 1;
		extId = 0;
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
	
	
	public boolean store(Context context)
	{
		if (extId==0)
		{
			ArrayList<Integer> idList = Config.getInstance().getIds();
			int i = -1;
			while(idList.contains(i))
			{
				i--;
			}
			extId = i;
		}
		String filename = "t"+extId;
		FileOutputStream outputStream;
		boolean success = false;

		try {
		outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(outputStream);
		os.writeObject(this);
		os.close();
	  	outputStream.close();
	  	success = true;
	  	ArrayList<Integer> idList = Config.getInstance().getIds();
	  	if (idList.contains(extId))
	  	{
	  		int pos = idList.indexOf(extId);
	  		Config.getInstance().getNames().set(pos,name);
	  	}
	  	else
	  	{
	  		Config.getInstance().getIds().add(extId);
	  		Config.getInstance().getNames().add(name);
	  	}
	  	Config.store(context);
	  	System.out.println("Succeeded to store tournament "+ name + " tId:"+extId);
		} catch (Exception e) {
		System.out.println("Failed to store tournament "+ name + " tId:"+extId);
		success = false;
		}
		return success;
	}
	
	public Tournament reload(Context context)
	{
		String fileName = "t"+extId;
		Tournament tournament;
		try{
		FileInputStream fis = context.openFileInput(fileName);
		ObjectInputStream is = new ObjectInputStream(fis);
		tournament = (Tournament) is.readObject();
		is.close();
		name = tournament.getName();
		teams = tournament.getTeams();
		fixtures = tournament.getFixtures();
		startDate = tournament.getStartDate();
		endDate = tournament.getEndDate();
		info = tournament.getInfo();
		ispublic = tournament.isIspublic();
		homeandaway = tournament.isHomeandaway();
		locked = tournament.isLocked();
		extId = tournament.getExtId();
		last_updated = tournament.getLast_updated();
		privilege = tournament.getPrivilege();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return this;
	}
	
	public static Tournament getTournament(String fileName, Context context)
	{
		Tournament tournament;
		try{
		FileInputStream fis = context.openFileInput(fileName);
		ObjectInputStream is = new ObjectInputStream(fis);
		tournament = (Tournament) is.readObject();
		is.close();
		return tournament;
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}

	}
}
