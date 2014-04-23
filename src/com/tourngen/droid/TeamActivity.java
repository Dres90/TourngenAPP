package com.tourngen.droid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TeamActivity extends Activity{
	
	Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team);
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	team = DataHolder.getInstance().getTeam();
    	setTitle("Team: "+team.getName());
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
    	TextView mail = (TextView) findViewById(R.id.team_email);
    	mail.setText(team.getEmail());
    	TextView info = (TextView) findViewById(R.id.team_info);
    	info.setText(team.getInfo());
    	
    	TableLayout table = (TableLayout) findViewById(R.id.match_table);
    	table.removeAllViews();
    	ArrayList<Match> matches = team.getMatches();
    	for(int i=0;i<matches.size();i++)
    	{
    		TableRow row=(TableRow) this.getLayoutInflater().inflate(R.layout.match_row, null);
    		Match match = matches.get(i);
    		((TextView)row.findViewById(R.id.match_num)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.match_home)).setText(match.getHome().getName());
    		if (match.isPlayed())
    		{
    			((TextView)row.findViewById(R.id.match_score)).setText(match.getHomeGoal() + " - " + match.getAwayGoal());
    		}
    		else
    		{
        		((TextView)row.findViewById(R.id.match_score)).setText("  -  ");
        	}	
    		((TextView)row.findViewById(R.id.match_away)).setText(match.getAway().getName());
    		DateFormat dateFormat = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
    		((TextView)row.findViewById(R.id.match_date)).setText(dateFormat.format(match.getDate().getTime()));
    		row.setTag(match);
    		table.addView(row);    	
    	}
    	table.requestLayout();
    }
    
    public void selectMatch(View view)
    {
    	Intent matchIntent = new Intent(getApplicationContext(),MatchActivity.class);
    	DataHolder.getInstance().setMatch((Match)view.getTag());
        startActivity(matchIntent);
    }

}
