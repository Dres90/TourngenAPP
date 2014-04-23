package com.tourngen.droid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MatchActivity extends Activity{
	
	Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
        Intent intent = getIntent();
        match = (Match)intent.getSerializableExtra("match");
        setTitle(match.getTournament().getName());
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
    	((TextView) findViewById(R.id.match_fixture)).setText(String.valueOf(match.getFixture().getNumber()));
    	DateFormat dateFormat = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
		((TextView) findViewById(R.id.match_date)).setText(dateFormat.format(match.getDate().getTime()));
		((ToggleButton) findViewById(R.id.match_played)).setChecked(match.isPlayed());
		((TextView) findViewById(R.id.match_hTeam)).setText(match.getHome().getName());
		((EditText) findViewById(R.id.home_score)).setText(String.valueOf(match.getHomeGoal()));
		((TextView) findViewById(R.id.match_aTeam)).setText(match.getAway().getName());
		((EditText) findViewById(R.id.away_score)).setText(String.valueOf(match.getAwayGoal()));
    }
    
    public void onClick(View view)
    {
    	EditText home = (EditText) findViewById(R.id.home_score);
    	EditText away = (EditText) findViewById(R.id.away_score);
    	int goal;
    	switch(view.getId())
    	{
    	case R.id.increase_home:
    		goal = Integer.valueOf(home.getText().toString());
    		goal++;
    		match.setHomeGoal(goal);
    		home.setText(String.valueOf(goal));
    		break;
    	case R.id.decrease_home:
    		goal = Integer.valueOf(home.getText().toString());
    		if(goal>0)
    		{
    		goal--;
    		match.setHomeGoal(goal);
    		home.setText(String.valueOf(goal));
    		}
    		break;
    	case R.id.increase_away:
    		goal = Integer.valueOf(away.getText().toString());
    		goal++;
    		match.setAwayGoal(goal);
    		away.setText(String.valueOf(goal));
    		break;
    	case R.id.decrease_away:
    		goal = Integer.valueOf(away.getText().toString());
    		if (goal>0)
    		{
    		goal--;
    		match.setAwayGoal(goal);
    		away.setText(String.valueOf(goal));
    		}
    		break;
    	}	
    }
}
