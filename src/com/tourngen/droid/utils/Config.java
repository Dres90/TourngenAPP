package com.tourngen.droid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;

public class Config implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private String username;
	public String getUserName(){return username;}
	public void setUserName(String username){this.username = username;}
	
	private String token;
	public String getToken(){return token;}
	public void setToken(String token){this.token = token;}
	
	private ArrayList<Integer> tIds;
	public ArrayList<Integer> getIds(){return tIds;}
	public void setIds(ArrayList<Integer> tIds){this.tIds = tIds;}	
	
	private ArrayList<String> tNames;
	public ArrayList<String> getNames(){return tNames;}
	public void setNames(ArrayList<String> names){this.tNames = names;}	
	
	private ArrayList<Integer> tPrivileges;
	public ArrayList<Integer> getPrivileges(){return tPrivileges;}
	public void setPrivileges(ArrayList<Integer> privileges){this.tPrivileges = privileges;}	
	
	private static final Config config = new Config();
	public static Config getInstance() {return config;}
	
	public static boolean store(Context context)
	{
		String filename = "config";
		FileOutputStream outputStream;
		boolean success = false;
		try {
		outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(outputStream);
		os.writeObject(getInstance());
		os.close();
	  	outputStream.close();
	  	success = true;
		} catch (Exception e) {
		success = false;
		}
		return success;
	}
	public static boolean load(Context context){
		String fileName = "config";
		try{
		FileInputStream fis = context.openFileInput(fileName);
		ObjectInputStream is = new ObjectInputStream(fis);
		Config config = ((Config)is.readObject());
		is.close();
		Config.getInstance().setUserName(config.getUserName()); 
		Config.getInstance().setToken(config.getToken()); 
		Config.getInstance().setIds(new ArrayList<Integer>(config.getIds())); 
		Config.getInstance().setNames(new ArrayList<String>(config.getNames()));
		Config.getInstance().setPrivileges(new ArrayList<Integer>(config.getPrivileges()));
		return true;
		} catch (IOException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}
	public static boolean clear(Context context){
		File dir = context.getFilesDir();
		if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(dir, children[i]).delete();
	        }
	    }
    	Config.getInstance().setToken(null);
    	Config.getInstance().setUserName(null);
    	Config.getInstance().setIds(null);
    	Config.getInstance().setNames(null);
		return true;
		
	}
}
