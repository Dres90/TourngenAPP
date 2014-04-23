package com.tourngen.droid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    	
    	ArrayList<Team> teams = tournament.getTeams(); 

    	TableLayout table = (TableLayout) findViewById(R.id.tournament_table);
    	table.removeAllViews();
    	for(int i =0;i<teams.size();i++)
    	{
    		TableRow row=(TableRow) this.getLayoutInflater().inflate(R.layout.position_row, null);
    		((TextView)row.findViewById(R.id.position_num)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_team)).setText(teams.get(i).getName());
    		((TextView)row.findViewById(R.id.position_pts)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gp)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gw)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gt)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gl)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gf)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_ga)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gd)).setText(String.valueOf(i+1));
    		
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
            startActivity(fixtures);
    		break;
    	}
    }
}
