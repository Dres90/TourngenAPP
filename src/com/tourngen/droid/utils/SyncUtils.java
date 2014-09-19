package com.tourngen.droid.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.tourngen.droid.objects.*;

public final class SyncUtils {
	
	private static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	final static SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
	static String token = Config.getInstance().getToken();
	static TimeZone tz = TimeZone.getDefault();
	static int offset = tz.getOffset(Calendar.getInstance().getTimeInMillis())/1000/60/60;
	
	public static void getTournament(Tournament tournament) throws JSONException, ParseException
	{
		String querystring = "token="+EscapeUtils.encodeURIComponent(token);
		WSRequest request = new WSRequest(WSRequest.GET,"Tournament",String.valueOf(tournament.getExtId()),querystring,null);
		JSONObject jT;
		jT = request.getJSON();
		if (jT.getBoolean("success"))
		{
			Tournament t = new Tournament(jT.getString("Name"));
			int homeandaway = jT.getInt("Home_and_away");
			if(homeandaway==1)
				t.setHomeandaway(true);
			else
				t.setHomeandaway(false);
			int ispublic = jT.getInt("Public");
			if(ispublic==1)
				t.setIspublic(true);
			else
				t.setIspublic(false);
			t.setPrivilege(jT.getInt("privilege_id"));
			
			SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
			if(jT.has("Date_start"))
			{
				String startString = jT.getString("Date_start");
				if (!startString.equals("null"))
				{
					Calendar start = Calendar.getInstance();
					start.setTime(ISO8601DATEFORMAT.parse(startString));
					t.setStartDate(start);
				}
			}
			
			if(jT.has("Date_end"))
			{
				String endString = jT.getString("Date_end");
				if (!endString.equals("null"))
				{
					Calendar end = Calendar.getInstance();
					end.setTime(ISO8601DATEFORMAT.parse(endString));
					t.setEndDate(end);
				}
			}
			
			if(jT.has("Last_updated"))
			{
				String lastupdString = jT.getString("Last_updated");
				if (!lastupdString.equals("null"))
				{
					Calendar lupd = Calendar.getInstance();
					lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
					lupd.add(Calendar.HOUR_OF_DAY, offset);
					t.setLast_updated(lupd);
				}
			}
			if (jT.has("info"))
			{
				String info = jT.getString("info");
				t.setInfo(info);
			}
		}
	}
	
	public static boolean sendTournament (Tournament tournament) throws JSONException, ParseException
	{
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		JSONObject jsonPut = new JSONObject();
		jsonPut.put("token", token);
		JSONObject jTournament = new JSONObject();
		jTournament.put("name", tournament.getName());
		jTournament.put("date_start", simpleDate.format(tournament.getStartDate().getTime()));
		jTournament.put("date_end", simpleDate.format(tournament.getEndDate().getTime()));
		jTournament.put("is_public",tournament.isIspublic());
		jTournament.put("info",tournament.getInfo());
		jsonPut.put("tournament", jTournament);
		WSRequest requestPut = new WSRequest(WSRequest.PUT,"Tournament",String.valueOf(tournament.getExtId()),null,jsonPut);
		JSONObject json = requestPut.getJSON();
		boolean success = false;
		if (json.getBoolean("success"))
		{
			success = true;
			if(json.has("last_updated"))
			{
				String lastupdString = json.getString("last_updated");
				if (!lastupdString.equals("null"))
				{
					Calendar lupd = Calendar.getInstance();
					lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
					lupd.add(Calendar.HOUR_OF_DAY, offset);
					tournament.setLast_updated(lupd);
				}
			}
		}
		return success;
	}
	
	public static void getTeam (Tournament tournament, int id) throws JSONException, ParseException
	{
		String querystring = "token="+EscapeUtils.encodeURIComponent(token);
		WSRequest request = new WSRequest(WSRequest.GET,"Team",String.valueOf(id),querystring,null);
		JSONObject jT;
		jT = request.getJSON();
		if (jT.getBoolean("success"))
		{
			
			Team t = findTeam(tournament, jT.getInt("Team_id"));
			t.setName(jT.getString("Name"));
			
			SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);

			if(jT.has("Last_updated"))
			{
				String lastupdString = jT.getString("Last_updated");
				if (!lastupdString.equals("null"))
				{
					Calendar lupd = Calendar.getInstance();
					lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
					lupd.add(Calendar.HOUR_OF_DAY, offset);
					t.setLast_updated(lupd);
				}
			}
			
			if (jT.has("info"))
			{
				String info = jT.getString("info");
				t.setInfo(info);
			}
			
			if (jT.has("E-mail"))
			{
				String email = jT.getString("E-mail");
				t.setEmail(email);
			}
		
		}
	}
	
	public static void getFixture (Tournament tournament, int id) throws JSONException, ParseException
	{
		String querystring = "token="+EscapeUtils.encodeURIComponent(token);
		WSRequest request = new WSRequest(WSRequest.GET,"Fixture",String.valueOf(id),querystring,null);
		JSONObject jF;
		jF = request.getJSON();
		if (jF.getBoolean("success"))
		{
			Fixture f = findFixture(tournament,jF.getInt("Fixture_id"));
			f.setNumber(jF.getInt("Number"));
			SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
			if (jF.has("Last_updated"))
			{
				String lastupdString = jF.getString("Last_updated");
				if (!lastupdString.equals("null"))
				{
					Calendar lupd = Calendar.getInstance();
					lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
					lupd.add(Calendar.HOUR_OF_DAY, offset);
					f.setLast_updated(lupd);
				}
			}

			if (jF.has("info"))
			{
				String info = jF.getString("info");
				f.setInfo(info);
			}
		}
	}
	
	public static void getMatch (Tournament tournament, int id) throws JSONException, ParseException
	{
		
		String querystring = "token="+EscapeUtils.encodeURIComponent(token);
		WSRequest request = new WSRequest(WSRequest.GET,"Match",String.valueOf(id),querystring,null);
		JSONObject jM;
		jM = request.getJSON();
		Match m = findMatch(tournament, jM.getInt("match_id"));
		m.setHomeGoal(jM.getInt("score_home"));
		m.setAwayGoal(jM.getInt("score_away"));
		int played = jM.getInt("played");
		if (played==1)
			m.setPlayed(true);
		else
			m.setPlayed(false);
		SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
		if (jM.has("last_updated"))
		{
			String lastupdString = jM.getString("last_updated");
			if (!lastupdString.equals("null"))
			{
				Calendar lupd = Calendar.getInstance();
				lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
				lupd.add(Calendar.HOUR_OF_DAY, offset);
				m.setLast_updated(lupd);
			}
		}
		
		if (jM.has("date"))
		{
			String date = jM.getString("date");
			if (!date.equals("null"))
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(ISO8601DATEFORMAT.parse(date));
				m.setDate(cal);
			}
		}
		
		if (jM.has("info"))
		{
			String info = jM.getString("info");
			m.setInfo(info);
		}	
	}
	
	public static boolean sendMatch (Tournament tournament, int id) throws JSONException, ParseException
	{
		JSONObject jsonPut = new JSONObject();
		
		jsonPut.put("token", token);
		Match match = findMatch(tournament, id);
		JSONObject jMatch = new JSONObject();
		jMatch.put("score_home",match.getHomeGoal());
		jMatch.put("score_away",match.getAwayGoal());
		jMatch.put("played", match.isPlayed());
		jsonPut.put("match", jMatch);
		boolean success = false;
		WSRequest requestPut = new WSRequest(WSRequest.PUT,"Match",String.valueOf(match.getExtId()),null,jsonPut);
		JSONObject json = requestPut.getJSON();
		if (json.getBoolean("success"))
		{
			success = true;
			if(json.has("last_updated"))
			{
				String lastupdString = json.getString("last_updated");
				if (!lastupdString.equals("null"))
				{
					Calendar lupd = Calendar.getInstance();
					lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
					lupd.add(Calendar.HOUR_OF_DAY, offset);
					match.setLast_updated(lupd);
				}
			}
		}
		return success;
	}
	
	private static Team findTeam(Tournament tournament, int id)
	{
		ArrayList<Team> teams = tournament.getTeams();
		for (int i =0; i<teams.size(); i++)
		{
			Team team = teams.get(i);
			if (team.getExtId()==id)
				return team;
		}
		return null;
	}
	
	private static Fixture findFixture(Tournament tournament, int id)
	{
		ArrayList<Fixture> fixtures = tournament.getFixtures();
		for (int i =0; i<fixtures.size(); i++)
		{
			Fixture fixture = fixtures.get(i);
			if (fixture.getExtId()==id)
				return fixture;
		}
		return null;
	}
	
	private static Match findMatch(Tournament tournament, int id)
	{
		ArrayList<Fixture> fixtures = tournament.getFixtures();
		for (int i =0; i<fixtures.size(); i++)
		{
			Fixture fixture = fixtures.get(i);
			ArrayList<Match> matches = fixture.getMatches();
			for (int j=0; j<matches.size();j++)
			{
				Match match = matches.get(j);
				if (match.getExtId()==id)
					return match;
			}
		}
		return null;
	}
}
