package com.tourngen.droid;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewMatchesActivity extends Activity{
	
	ArrayList<Fixture> fixtures;
	ArrayList<Team> teams;
	Tournament tournament;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_matches);
        setTitle("Match List");
        Intent intent = getIntent();
        tournament = (Tournament)intent.getSerializableExtra("tournament");
        teams = tournament.getTeams();
        generateMatches();
        renderListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_matches, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
        	case R.id.shuffle_button:
        		generateMatches();
        		renderListView();
                return true;	
        	case R.id.confirm_edit:
        		tournament.setFixtures(fixtures);
        		tournament.store(getApplicationContext());
        		setResult(RESULT_OK);
        		finish();
                return true;	
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    private void generateMatches()
    {
    	MatchGenerator generator = new MatchGenerator(tournament);
    	fixtures = generator.getFixtures();
    }
    
    private void renderListView()
    {
    	LinearLayout list = (LinearLayout) findViewById(R.id.new_match_list);
    	list.removeAllViews();
    	
    	for (int i = 0; i<fixtures.size();i++)
    	{
    		Fixture fixture = fixtures.get(i);
    		ArrayList<Match> matches = fixture.getMatches();
    		LinearLayout fixtureRow = (LinearLayout) this.getLayoutInflater().inflate(R.layout.new_fixture_row, null);
    		((TextView)fixtureRow.findViewById(R.id.new_fixture_text)).setText("Fixture #" + fixture.getNumber());
    		list.addView(fixtureRow);
    		
    		for (int j = 0; j<matches.size();j++)
    		{
	    		LinearLayout row = (LinearLayout) this.getLayoutInflater().inflate(R.layout.new_match_row, null);
	    		((TextView)row.findViewById(R.id.new_match_home)).setText(matches.get(j).getHome().getName());
	    		((TextView)row.findViewById(R.id.new_match_away)).setText(matches.get(j).getAway().getName());
	    		list.addView(row);
    		}
    	}
    	list.requestLayout();
    }
    
}
