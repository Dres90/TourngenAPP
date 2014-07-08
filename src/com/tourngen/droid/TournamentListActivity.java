package com.tourngen.droid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TournamentListActivity extends Activity implements OnClickListener{

	ProgressDialog progress;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_list);
    }

    @Override
    protected void onResume()
    {
    	super.onResume();
    	setTitle(Config.getInstance().getUserName()+"'s Tournaments");
    	if(WSRequest.isOnline(getApplicationContext()))
    	{
    		progress = new ProgressDialog(this);
    		progress.setTitle("Loading Tournaments");
    		progress.setMessage("Please wait while we get your tournaments");
    		progress.show();
    		new GetTournamentsTask().execute(Config.getInstance().getToken());
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tournament, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
        	case R.id.sync_button:
        		System.out.println(Config.getInstance().getPrivileges());
            	Toast.makeText(getApplicationContext(), "Sync button pressed!", Toast.LENGTH_SHORT).show();
                return true;
        	case R.id.new_tournament_button:
        		Intent newTournamentIntent = new Intent(getApplicationContext(),NewTournamentActivity.class);
        		startActivity(newTournamentIntent);
        		return true;
        	case R.id.logout_button:
        		logout();
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void renderViews()
    {
    	TableLayout table = (TableLayout) findViewById(R.id.tournaments);
    	table.removeAllViews();
    	ArrayList<String> tournaments = Config.getInstance().getNames();
    	for(int i =0;i<tournaments.size();i++)
    	{
    		TableRow row=(TableRow) this.getLayoutInflater().inflate(R.layout.tournament_list_row, null);
    		row.setOnClickListener(this);
    		row.setTag(Config.getInstance().getIds().get(i));
    		((TextView)row.findViewById(R.id.list_text)).setText(tournaments.get(i));
    		CheckBox check = (CheckBox) row.findViewById(R.id.list_check);
			Integer privilege = Config.getInstance().getPrivileges().get(i);
			if (privilege.equals(1)||privilege.equals(2))
				check.setChecked(true);
			else
				check.setChecked(false);
			
    		table.addView(row);    	
    	}
    	table.requestLayout();
      }
    public void logout()
    {
    	if (WSRequest.isOnline(getApplicationContext()))
    	{
	    	progress = new ProgressDialog(this);
	    	progress.setTitle("Logging out");
	    	progress.setMessage("Please wait while we log you out");
	    	progress.show();
	    	new LogOutTask().execute(Config.getInstance().getToken());
    	}
    	else
    	{
			confirmLogout();
    	}
    		
    }
    public void confirmLogout()
    {
    	progress.dismiss();
    	finish();
    }

	@Override
	public void onClick(View v) {
        Intent tournamentIntent = new Intent(getApplicationContext(),TournamentActivity.class);
        int tId = (Integer) v.getTag();
        DataHolder.getInstance().setTournament(Tournament.getTournament("t"+tId,getApplicationContext()));
        startActivity(tournamentIntent);
	}
	
    private class LogOutTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
	    	Config.clear(getApplicationContext());
			JSONObject json;
			String token = EscapeUtils.encodeURIComponent(params[0]);
			WSRequest request = new WSRequest(WSRequest.DELETE,"Login",token,null,null);
			try {
				json = request.getJSON();
				return json;
			} catch (JSONException e) {
				return null;
			}
		}
		
		@Override
        protected void onPostExecute(JSONObject result) {
					confirmLogout();
        }
    }
    
    private class GetTournamentsTask extends AsyncTask<String, Void, Integer> {
    	
    	private String token;
    	private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    	
		@Override
		protected Integer doInBackground(String... params) {
			JSONObject json;
			token = params[0];
			String querystring = "token="+EscapeUtils.encodeURIComponent(token);
			WSRequest request = new WSRequest(WSRequest.GET,"Tournament",null,querystring,null);
			try {
				json = request.getJSON();
				getTournaments(json);
				return 1;
			} catch (JSONException e) {
				return null;
			}
		}
		
		
		@Override
        protected void onPostExecute(Integer result) {
				renderViews();
				progress.dismiss();
		}
		
		private void getTournaments(JSONObject json)
		{
			ArrayList<Integer> idList = Config.getInstance().getIds();
				try {
					if (json.getBoolean("success"))
					{
						JSONArray jsonTournaments = json.getJSONArray("tournaments");
						for (int i = 0; i<jsonTournaments.length();i++)
						{
							JSONObject jT = jsonTournaments.getJSONObject(i);
							Tournament t = new Tournament(jT.getString("Name"));
							t.setExtId(jT.getInt("Tournament_id"));
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
									t.setLast_updated(lupd);
								}
							}
							if (jT.has("info"))
							{
								String info = jT.getString("info");
								t.setInfo(info);
							}	
							
							if (!idList.contains(t.getExtId()))
							{
								Log.v("id list","Not contained");
								Log.v("id",String.valueOf(t.getExtId()));
								Log.v("name",t.getName());
								t = getTeams(t);
								t = getMatches(t);
								t.store(getApplicationContext());
							}


							}
						}
						
					}
					catch (JSONException e) {
						e.printStackTrace();
				}
				
				catch (ParseException e) {
					e.printStackTrace();
				}
		}
				
        private Tournament getTeams(Tournament tournament)
        {
        	String queryString = "token="+EscapeUtils.encodeURIComponent(token)+"&tournament="+tournament.getExtId();
			WSRequest request = new WSRequest(WSRequest.GET,"Team",null,queryString,null);
			ArrayList<Team> teams = new ArrayList<Team>();

			try
			{
				JSONObject result = request.getJSON();
				if (result.getBoolean("success"))
				{
					JSONArray jsonTeams = result.getJSONArray("teams");
					for (int i = 0; i<jsonTeams.length();i++)
					{
						JSONObject jT = jsonTeams.getJSONObject(i);
						Team t = new Team(jT.getString("Name"),tournament);
						t.setExtId(jT.getInt("Team_id"));
						
						SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);

						if(jT.has("Last_updated"))
						{
							String lastupdString = jT.getString("Last_updated");
							if (!lastupdString.equals("null"))
							{
								Calendar lupd = Calendar.getInstance();
								lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
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
						
						teams.add(t);
					}
				}
			}
			catch (JSONException e){
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			tournament.setTeams(teams);
        	return tournament;
        }
        
        private Tournament getMatches(Tournament tournament)
        {
        	String queryString = "token="+EscapeUtils.encodeURIComponent(token)+"&tournament="+tournament.getExtId();
			WSRequest request = new WSRequest(WSRequest.GET,"Fixture",null,queryString,null);
			ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
			try
			{
				JSONObject result = request.getJSON();
				if (result.getBoolean("success"))
				{
					JSONArray jsonFixtures = result.getJSONArray("fixtures");
					for (int i = 0; i<jsonFixtures.length();i++)
					{
						JSONObject jF = jsonFixtures.getJSONObject(i);
						Fixture f = new Fixture(tournament, jF.getInt("Number"));
						
						f.setExtId(jF.getInt("Fixture_id"));
						
						SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
						if (jF.has("Last_updated"))
						{
							String lastupdString = jF.getString("Last_updated");
							if (!lastupdString.equals("null"))
							{
								Calendar lupd = Calendar.getInstance();
								lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
								f.setLast_updated(lupd);
							}
						}

						if (jF.has("info"))
						{
							String info = jF.getString("info");
							f.setInfo(info);
						}
							
						fixtures.add(f);
					}
				}
				ArrayList<Team> teams = tournament.getTeams();
				SimpleArrayMap<Integer,Team> teamMap = new SimpleArrayMap<Integer,Team>();
				
				for (int i = 0; i< teams.size(); i++)
				{
					Team team = teams.get(i);
					teamMap.put(team.getExtId(), team);
				}
				
				SimpleArrayMap<Integer,Fixture> fixtureMap = new SimpleArrayMap<Integer,Fixture>();
				
				for (int i = 0; i< fixtures.size(); i++)
				{
					Fixture fixture = fixtures.get(i);
					fixtureMap.put(fixture.getExtId(), fixture);
				}
	        	queryString = "token="+EscapeUtils.encodeURIComponent(token)+"&tournament="+tournament.getExtId();
				request = new WSRequest(WSRequest.GET,"Match",null,queryString,null);
				result = request.getJSON();
				if (result.getBoolean("success"))
				{
					JSONArray jsonMatches = result.getJSONArray("matches");
					for (int i = 0; i<jsonMatches.length();i++)
					{
						JSONObject jM = jsonMatches.getJSONObject(i);
						Team home = teamMap.get(jM.getInt("home_id"));
						Team away = teamMap.get(jM.getInt("away_id"));
						Match m = new Match(tournament, home, away);
						m.setExtId(jM.getInt("match_id"));
						m.setFixture(fixtureMap.get(jM.getInt("fixture_id")));
						fixtureMap.get(jM.getInt("fixture_id")).getMatches().add(m);
						m.setHomeGoal(jM.getInt("score_home"));
						m.setAwayGoal(jM.getInt("score_away"));
						int played = jM.getInt("played");
						if (played==1)
							m.setPlayed(true);
						else
							m.setPlayed(false);
						SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
						if (jM.has("Last_updated"))
						{
							String lastupdString = jM.getString("Last_updated");
							if (!lastupdString.equals("null"))
							{
								Calendar lupd = Calendar.getInstance();
								lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
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
				}
				
			}
			catch (JSONException e){
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			tournament.setFixtures(fixtures);
        	return tournament;
        }
        
    }

}
