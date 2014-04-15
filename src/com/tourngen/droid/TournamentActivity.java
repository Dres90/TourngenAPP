package com.tourngen.droid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TextView;

public class TournamentActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament);
        setTitle("My Tournament");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        fillPositions();
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
        	case android.R.id.home:
        		NavUtils.navigateUpFromSameTask(this);
        		return true;
        	case R.id.sync_button:
            	Toast.makeText(getApplicationContext(), "Sync button pressed!", Toast.LENGTH_SHORT).show();
                return true;	
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void fillPositions()
    {
    	char letter = 'A';
    	
    	TableLayout table = (TableLayout) findViewById(R.id.tournament_table);
    	table.removeAllViews();
    	for(int i =0;i<25;i++)
    	{
    		TableRow row=(TableRow) this.getLayoutInflater().inflate(R.layout.position_row, null);
    		((TextView)row.findViewById(R.id.position_num)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_team)).setText("Team "+letter);
    		((TextView)row.findViewById(R.id.position_pts)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gp)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gw)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gt)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gl)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gf)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_ga)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.position_gd)).setText(String.valueOf(i+1));
    		
    		letter++;
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
