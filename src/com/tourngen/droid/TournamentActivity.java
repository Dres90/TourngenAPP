package com.tourngen.droid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TextView;

public class TournamentActivity extends Activity{
	
	Tournament tournament;
	ArrayList<Position> positions;
	
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
        	case R.id.sync_button:
            	Toast.makeText(getApplicationContext(), "Sync button pressed!", Toast.LENGTH_SHORT).show();
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
    
    private void calcPositions() //TODO debug this code, not correct
    {
    	positions = new ArrayList<Position>();
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
    				if (posHome==0)
    				{
    					homePosition = new Position(home);
    					positions.add(homePosition);
    				}
    				else
    				{
    					homePosition = positions.get(posHome);
    				}
    				System.out.println("Home position is " + posHome);
    				
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
    				if (posAway==0)
    				{
    					awayPosition = new Position(away);
    					positions.add(awayPosition);
    				}
    				else
    				{
    					awayPosition = positions.get(posAway);
    				}
    				System.out.println("Away position is " + posAway);
    				
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
    	return 0;
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
						return favor-another.favor;
				else
					return getDifference()-another.getDifference();	
			else
				return points-another.points;
		}
    }
}
