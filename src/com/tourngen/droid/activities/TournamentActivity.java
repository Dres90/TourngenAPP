package com.tourngen.droid.activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tourngen.droid.R;
import com.tourngen.droid.objects.Fixture;
import com.tourngen.droid.objects.Match;
import com.tourngen.droid.objects.Team;
import com.tourngen.droid.objects.Tournament;
import com.tourngen.droid.utils.Config;
import com.tourngen.droid.utils.DataHolder;
import com.tourngen.droid.utils.SyncUtils;
import com.tourngen.droid.utils.WSRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TournamentActivity extends Activity{
	
	Tournament tournament;
	ArrayList<Position> positions;
	ProgressDialog progress;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament);
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
        tournament = DataHolder.getInstance().getTournament();
        setTitle(tournament.getName());
        renderViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tournament_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
        	case R.id.sync_button:
        		progress = new ProgressDialog(this);
        		if (tournament.getExtId()>0)
        		{
        			progress.setTitle("Syncing Tournament");
            		progress.setMessage("Please wait while we synchronize your tournament");
            		progress.show();
            		new SyncTournamentTask().execute(tournament);
        		}
        		else
        		{
        			progress.setTitle("Sending Tournament");
            		progress.setMessage("Please wait while we send your tournament");
            		progress.show();
            		new SendTournamentTask().execute(tournament);
        		}
               return true;	
        	case R.id.delete_button:
        		progress = new ProgressDialog(this);
    			progress.setTitle("Deleting Tournament");
        		progress.setMessage("Please wait while we delete your tournament");
        		progress.show();
        		tournament.delete(getApplicationContext());
        		new DeleteTournamentTask().execute(tournament);
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void renderViews()
    {
    	calcPositions();
    	
		Calendar start = tournament.getStartDate();
		TextView textview = (TextView) findViewById(R.id.tournament_sDate);
		DateFormat dateFormat = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
		textview.setText(dateFormat.format(start.getTime()));
		
		Calendar end = tournament.getEndDate();
		TextView textview1 = (TextView) findViewById(R.id.tournament_eDate);
		DateFormat dateFormat1 = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
		textview1.setText(dateFormat1.format(end.getTime()));
    	
    	CheckBox homeAndAway = (CheckBox) findViewById(R.id.tournament_homeaway);
    	homeAndAway.setChecked(tournament.isHomeandaway());
    	CheckBox isPublic = (CheckBox) findViewById(R.id.tournament_public);
    	isPublic.setChecked(!tournament.isIspublic());
    	
    	TableLayout table = (TableLayout) findViewById(R.id.tournament_table);
    	table.removeAllViews();
    	for(int i=0;i<positions.size();i++)
    	{
    		Position pos = positions.get(i);
    		TableRow row=(TableRow) this.getLayoutInflater().inflate(R.layout.position_row, null);
    		((TextView)row.findViewById(R.id.position_num)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_team)).setText(pos.team.getName());
    		((TextView)row.findViewById(R.id.position_pts)).setText(String.valueOf(pos.getPoints()));
    		((TextView)row.findViewById(R.id.position_gp)).setText(String.valueOf(pos.getPlayed()));
    		((TextView)row.findViewById(R.id.position_gw)).setText(String.valueOf(pos.getWon()));
    		((TextView)row.findViewById(R.id.position_gt)).setText(String.valueOf(pos.getTied()));
    		((TextView)row.findViewById(R.id.position_gl)).setText(String.valueOf(pos.getLost()));
    		((TextView)row.findViewById(R.id.position_gf)).setText(String.valueOf(pos.getFavor()));
    		((TextView)row.findViewById(R.id.position_ga)).setText(String.valueOf(pos.getAgainst()));
    		((TextView)row.findViewById(R.id.position_gd)).setText(String.valueOf(pos.getDifference()));
    		table.addView(row);    	
    	}
    	table.requestLayout();
    }
    
    public void onClick(View view)
    {
    	switch (view.getId())
    	{
    	case R.id.tournament_teams:
            Intent teams = new Intent(getApplicationContext(),TeamListActivity.class);
            startActivity(teams);
    		break;
    	case R.id.tournament_matches:
            Intent matches = new Intent(getApplicationContext(),MatchListActivity.class);
            startActivity(matches);
    		break;
    	case R.id.tournament_fixtures:
            Intent fixtures = new Intent(getApplicationContext(),FixtureListActivity.class);
            DataHolder.getInstance().setFixture(null);
            startActivity(fixtures);
    		break;
    	}
    }
    
    private void calcPositions()
    {
    	positions = new ArrayList<Position>();
    	ArrayList<Team> teams = tournament.getTeams();
    	for(int i = 0; i<teams.size();i++)
    	{
			Position newPos = new Position(teams.get(i));
			positions.add(newPos);
    	}
    	for (int i = 0; i<tournament.getFixtures().size();i++)
    	{
    		Fixture fixture = tournament.getFixtures().get(i);
    		for (int j = 0; j<fixture.getMatches().size();j++)
    		{
    			Match match = fixture.getMatches().get(j);
    			if (match.isPlayed())
    			{
    				Team home = match.getHome();
    				Team away = match.getAway();
    				
    				int posHome = positionsContain(home);
    				Position homePosition;
    				if (posHome==-1)
    				{
    					homePosition = new Position(home);
    					positions.add(homePosition);
    				}
    				else
    				{
    					homePosition = positions.get(posHome);
    				}
    				
    				if (match.getHomeGoal()>match.getAwayGoal())
    				{
    					homePosition.addPoints(3);
    					homePosition.addWon(1);
    				}
    				else if (match.getHomeGoal()==match.getAwayGoal())
    				{
    					homePosition.addPoints(1);
    					homePosition.addTied(1);
    				}
    				else
    					homePosition.addLost(1);
    				
    				homePosition.addPlayed(1);
    				homePosition.addFavor(match.getHomeGoal());
    				homePosition.addAgainst(match.getAwayGoal());
    				
    				int posAway = positionsContain(away);
    				Position awayPosition;
    				if (posAway==-1)
    				{
    					awayPosition = new Position(away);
    					positions.add(awayPosition);
    				}
    				else
    				{
    					awayPosition = positions.get(posAway);
    				}
    				
    				if (match.getAwayGoal()>match.getHomeGoal())
    				{
    					awayPosition.addPoints(3);
    					awayPosition.addWon(1);
    				}
    				else if (match.getHomeGoal()==match.getAwayGoal())
    				{
    					awayPosition.addPoints(1);
    					awayPosition.addTied(1);
    				}
    				else
    					awayPosition.addLost(1);
    				
    				awayPosition.addPlayed(1);
    				awayPosition.addFavor(match.getAwayGoal());
    				awayPosition.addAgainst(match.getHomeGoal());
    			}
    		}
    	}
    	Collections.sort(positions);
    }
    
    private int positionsContain(Team team)
    {
    	for (int i = 0; i<positions.size();i++)
    	{
    		if (positions.get(i).team==team)
    		{
    			return i;
    		}
    	}
    	return -1;
    }
    
    private class Position implements Comparable<Position>
    {
    	public Team team;
    	private int points, played, won, tied, lost, favor, against;
    	public Position (Team team)
    	{
    		this.team = team;
    		points = 0;
    		played = 0;
    		won = 0;
    		tied = 0;
    		lost = 0;
    		favor = 0;
    		against = 0;
    	}
		public int getPoints() {
			return points;
		}
		public int getPlayed() {
			return played;
		}
		public int getWon() {
			return won;
		}
		public int getTied() {
			return tied;
		}
		public int getLost() {
			return lost;
		}
		public int getFavor() {
			return favor;
		}
		public int getAgainst() {
			return against;
		}
		public int getDifference() {
			return favor-against;
		}
		public void addPoints(int points) {
			this.points = this.points+points;
		}
		public void addPlayed(int played) {
			this.played = this.played+played;
		}
		public void addWon(int won) {
			this.won = this.won+won;
		}
		public void addTied(int tied) {
			this.tied = this.tied + tied;
		}
		public void addLost(int lost) {
			this.lost = this.lost+lost;
		}
		public void addFavor(int favor) {
			this.favor = this.favor+favor;
		}
		public void addAgainst(int against) {
			this.against = this.against+against;
		}
		@Override
		public int compareTo(Position another) {
			if (points==another.points)
				if (getDifference()==another.getDifference())
					if (favor==another.favor)
						return 0;
					else
						return another.favor-favor;
				else
					return another.getDifference()-getDifference();	
			else
				return another.points-points;
		}
    }
    private class SendTournamentTask extends AsyncTask<Tournament, Void, Integer> {
    	
    	private String token;
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    	
		@Override
		protected Integer doInBackground(Tournament... params) {
			
			try {
				if (WSRequest.isOnline(getApplicationContext()))
				{
				JSONObject jTournament = new JSONObject();
				JSONObject json;
				token = Config.getInstance().getToken();
				jTournament.put("token",token);
				addIds();
				jTournament.put("name", tournament.getName());
				jTournament.put("date_start", simpleDate.format(tournament.getStartDate().getTime()));
				jTournament.put("date_end", simpleDate.format(tournament.getEndDate().getTime()));
				jTournament.put("home_and_away",tournament.isHomeandaway());
				jTournament.put("is_public",tournament.isIspublic());
				jTournament.put("info",tournament.getInfo());
				JSONArray jTeams = new JSONArray();
				ArrayList<Team> teams = tournament.getTeams();
				for (int i = 0; i<teams.size(); i++)
				{
					Team team = teams.get(i);
					JSONObject jTeam = new JSONObject();
					jTeam.put("name",team.getName());
					jTeam.put("id", team.getExtId());
					jTeam.put("email", team.getEmail());
					jTeam.put("info", team.getInfo());
					jTeams.put(jTeam);
				}
				jTournament.put("teams", jTeams);
				
				JSONArray jFixtures = new JSONArray();
				ArrayList<Fixture> fixtures = tournament.getFixtures();
				for (int i = 0; i<fixtures.size(); i++)
				{
					Fixture fixture = fixtures.get(i);
					JSONObject jFixture = new JSONObject();
					jFixture.put("number",fixture.getNumber());
					jFixture.put("id", fixture.getExtId());
					jFixture.put("info", fixture.getInfo());
					JSONArray jMatches = new JSONArray();
					
					ArrayList<Match> matches = fixture.getMatches();
					for (int j = 0; j<matches.size(); j++)
					{
						Match match = matches.get(j);
						JSONObject jMatch = new JSONObject();
						jMatch.put("id", match.getExtId());
						jMatch.put("home",match.getHome().getExtId());
						jMatch.put("away",match.getAway().getExtId());
						jMatch.put("score_home", match.getHomeGoal());
						jMatch.put("score_away",match.getAwayGoal());
						jMatch.put("date",simpleDate.format(match.getDate().getTime()));
						jMatch.put("info",match.getInfo());
						jMatch.put("played",match.isPlayed());
						jMatches.put(jMatch);
					}
					jFixture.put("matches", jMatches);
					jFixtures.put(jFixture);
				}
				jTournament.put("teams", jTeams);
				jTournament.put("fixtures", jFixtures);
				

				WSRequest request = new WSRequest(WSRequest.POST,"Tournament",null,null,jTournament);
				
				json = request.getJSON();
				
				System.out.println(json);
				if (json.getBoolean("success"))
				{
					setIds(json);
					return 1;
				}
				else
					return 0;
				}
				else return -1;
				
			} catch (JSONException e) {
				return null;
			} catch (ParseException e) {
				return null;
			}
		}
		
		
		@Override
        protected void onPostExecute(Integer result) {
			switch (result)
			{
			case 1:
				Toast.makeText(getApplicationContext(), "Tournament Send Success", Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(getApplicationContext(), "Tournament Send Error", Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(getApplicationContext(), "Connection problem, please try again later", Toast.LENGTH_LONG).show();
				break;
			}
			progress.dismiss();
		}
		
		private void addIds()
		{
			ArrayList<Team> teams = tournament.getTeams();
			for (int i = 0 ; i <teams.size() ; i++)
			{
				teams.get(i).setExtId(i);
			}
			
			ArrayList<Fixture> fixtures = tournament.getFixtures();
			for (int i = 0 ; i <fixtures.size() ; i++)
			{
				Fixture fixture = fixtures.get(i);
				fixture.setExtId(i);
				ArrayList<Match> matches = fixture.getMatches();
				for (int j = 0; j < matches.size(); j++)
				{
					matches.get(j).setExtId(i*matches.size()+j);
				}
			}
		}
		
		private void setIds(JSONObject json) throws ParseException
		{
			String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
			SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
			TimeZone tz = TimeZone.getDefault();
			int offset = tz.getOffset(Calendar.getInstance().getTimeInMillis())/1000/60/60;
			try 
			{
				tournament.setExtId(json.getInt("tournament"),getApplicationContext());
				ArrayList<Team> teams = tournament.getTeams();
				if(json.has("last_updated"))
				{
					String lastupdString = json.getString("last_updated");
					if (!lastupdString.equals("null"))
					{
						Calendar lupd = Calendar.getInstance();
						lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
						lupd.add(Calendar.HOUR_OF_DAY, offset);
						tournament.setAllLastUpdated(lupd);
					}
				}
				JSONObject jTeams = json.getJSONObject("teams");
				for (int i = 0 ; i <teams.size() ; i++)
				{
					Team team = teams.get(i);
					team.setExtId(getId(jTeams,team.getExtId()));
				}
				JSONObject jFixtures = json.getJSONObject("fixtures");
				JSONObject jMatches = json.getJSONObject("matches");
				ArrayList<Fixture> fixtures = tournament.getFixtures();
				for (int i = 0 ; i <fixtures.size() ; i++)
				{
					Fixture fixture = fixtures.get(i);
					fixture.setExtId(getId(jFixtures,fixture.getExtId()));
					ArrayList<Match> matches = fixture.getMatches();
					for (int j = 0; j < matches.size(); j++)
					{
						Match match = matches.get(j); 
						match.setExtId(getId(jMatches,match.getExtId()));
					}
				}
			} catch (JSONException e) 
			{
				e.printStackTrace();
			}

		}
		
		private int getId(JSONObject dict, int localId) throws JSONException
		{
			JSONArray keys = dict.getJSONArray("Keys");
			JSONArray values = dict.getJSONArray("Values");
			for(int i=0; i<keys.length();i++)
			{
				if (localId==keys.getInt(i))
					return values.getInt(i);
			}
			return -1;
		}
    }
    
    private class SyncTournamentTask extends AsyncTask<Tournament, Void, Integer> {
    	
    	private String token;
		private String dateFormat1 = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat sendFormat = new SimpleDateFormat(dateFormat1, Locale.US);
		TimeZone tz = TimeZone.getDefault();
		int offset = tz.getOffset(Calendar.getInstance().getTimeInMillis())/1000/60/60;
    	
		@Override
		protected Integer doInBackground(Tournament... params) {
			
			try {
				
				if (WSRequest.isOnline(getApplicationContext()))
				{
					WSRequest request = new WSRequest(WSRequest.POST,"Tournament",String.valueOf(tournament.getExtId()),null,getUpdatedJSON(tournament));
					JSONObject json;
					json = request.getJSON();
					System.out.println(json);
					if (json.getBoolean("success"))
					{
						if (tournament.getPrivilege()==1&&json.getInt("status")==2)
						{
							SyncUtils.sendTournament(tournament);
								
						}
						else if (json.getInt("status")==1)
						{
							SyncUtils.getTournament(tournament);
						}
						JSONArray jTeams = json.getJSONArray("teams");
						for (int i=0; i < jTeams.length(); i++)
						{
							JSONObject jTeam = jTeams.getJSONObject(i);
							if (jTeam.getInt("status")==1)
								SyncUtils.getTeam(tournament,jTeam.getInt("id"));
						}
						JSONArray jFixtures = json.getJSONArray("fixtures");
						for (int i=0; i < jFixtures.length(); i++)
						{
							JSONObject jFixture = jFixtures.getJSONObject(i);
							if (jFixture.getInt("status")==1)
								SyncUtils.getFixture(tournament,jFixture.getInt("id"));
						}
						JSONArray jMatches = json.getJSONArray("matches");
						for (int i=0; i < jMatches.length(); i++)
						{
							JSONObject jMatch = jMatches.getJSONObject(i);
							if (tournament.getPrivilege()==1&&jMatch.getInt("status")==2)
								SyncUtils.sendMatch(tournament, jMatch.getInt("id"));
							else if (jMatch.getInt("status")==1)
								SyncUtils.getMatch(tournament,jMatch.getInt("id"));
						}
						return 1;
					}
					return 0;
				}
				else
					return -1;

			} catch (JSONException e) {
				return null;
			} catch (ParseException e) {
				return null;
			}
		}
		
		
		@Override
        protected void onPostExecute(Integer result) {
			switch(result)
			{
			case 1:
				Toast.makeText(getApplicationContext(), "Tournament Sync Success", Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(getApplicationContext(), "Tournament Sync Error", Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(getApplicationContext(), "Connection problem, please try again later", Toast.LENGTH_LONG).show();
				break;
			}
			progress.dismiss();
		}
		
		private JSONObject getUpdatedJSON(Tournament tournament) throws JSONException
		{
			JSONObject jSONrequest = new JSONObject();
			token = Config.getInstance().getToken();
			jSONrequest.put("token",token);
			JSONObject jTournament =  new JSONObject();
			jTournament.put("updated", getRemoteTimeString(tournament.getLast_updated()));
			JSONArray jTeams = new JSONArray();
			ArrayList<Team> teams = tournament.getTeams();
			for (int i = 0; i<teams.size(); i++)
			{
				Team team = teams.get(i);
				JSONObject jTeam = new JSONObject();
				jTeam.put("id", team.getExtId());
				jTeam.put("updated", getRemoteTimeString(team.getLast_updated()));
				jTeams.put(jTeam);
			}
			jTournament.put("teams", jTeams);
			
			JSONArray jFixtures = new JSONArray();
			ArrayList<Fixture> fixtures = tournament.getFixtures();
			JSONArray jMatches = new JSONArray();
			for (int i = 0; i<fixtures.size(); i++)
			{
				Fixture fixture = fixtures.get(i);
				JSONObject jFixture = new JSONObject();
				jFixture.put("id", fixture.getExtId());
				jFixture.put("updated", getRemoteTimeString(fixture.getLast_updated()));
				ArrayList<Match> matches = fixture.getMatches();
				for (int j = 0; j<matches.size(); j++)
				{
					Match match = matches.get(j);
					JSONObject jMatch = new JSONObject();
					jMatch.put("id", match.getExtId());
					jMatch.put("updated", getRemoteTimeString(match.getLast_updated()));
					jMatches.put(jMatch);
				}
				jFixtures.put(jFixture);
			}
			jTournament.put("teams", jTeams);
			jTournament.put("fixtures", jFixtures);
			jTournament.put("matches", jMatches);
			jSONrequest.put("tournament", jTournament);
			System.out.println(jSONrequest);
			return jSONrequest;
		}
		
		private String getRemoteTimeString(Calendar cal)
		{
			Calendar lupd = Calendar.getInstance();
			lupd.setTime(cal.getTime());
			lupd.add(Calendar.HOUR_OF_DAY, -offset);
			return sendFormat.format(lupd.getTime());
		}
		
    }
    
    private class DeleteTournamentTask extends AsyncTask<Tournament, Void, Integer>{

    	private String token = Config.getInstance().getToken();
    	SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		@Override
		protected Integer doInBackground(Tournament... params) {
			
			try {
				
				if (WSRequest.isOnline(getApplicationContext())&&tournament.getPrivilege()==1)
				{
					JSONObject jsonPut = new JSONObject();
					jsonPut.put("token", token);
					JSONObject jTournament = new JSONObject();
					jTournament.put("name", tournament.getName());
					jTournament.put("date_start", simpleDate.format(tournament.getStartDate().getTime()));
					jTournament.put("date_end", simpleDate.format(tournament.getEndDate().getTime()));
					jTournament.put("is_public",tournament.isIspublic());
					jTournament.put("info",tournament.getInfo());
					jTournament.put("status", false);
					jsonPut.put("tournament", jTournament);
					WSRequest requestPut = new WSRequest(WSRequest.PUT,"Tournament",String.valueOf(tournament.getExtId()),null,jsonPut);
					JSONObject json = requestPut.getJSON();
					System.out.println(json.toString());
					if (json.getBoolean("success"))
						return 1;
					else
						return 0;
				}
				return 0;

			} catch (JSONException e) {
				return null;
			}
		}
		
		
		@Override
        protected void onPostExecute(Integer result) {
			switch(result)
			{
			case 1:
				Toast.makeText(getApplicationContext(), "Tournament deleted!", Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(getApplicationContext(), "Tournament deleted locally", Toast.LENGTH_SHORT).show();
				break;
			}
			progress.dismiss();
			finish();
		}
    }
}
