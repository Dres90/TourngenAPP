package com.tourngen.droid.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.tourngen.droid.R;
import com.tourngen.droid.objects.Fixture;
import com.tourngen.droid.objects.Match;
import com.tourngen.droid.objects.Tournament;
import com.tourngen.droid.utils.DataHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

public class FixtureListActivity extends Activity implements OnItemSelectedListener{
	
	Tournament tournament;
	Fixture fixture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixture_list);
    }

    @Override
    protected void onResume(){
    	super.onResume();
        setTitle("My Tournament: Fixtures");
        Spinner spinner = (Spinner) findViewById(R.id.fixture_selector);
        ArrayAdapter<Fixture> fixtures = new ArrayAdapter<Fixture>(getApplicationContext(), R.layout.fixture_item, DataHolder.getInstance().getTournament().getFixtures());
        fixture = DataHolder.getInstance().getFixture();
        spinner.setAdapter(fixtures);
        spinner.setOnItemSelectedListener(this);
        
        if (fixture!=null)
        {
        	int pos = fixtures.getPosition(fixture);
        	spinner.setSelection(pos);
        }
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
    
    private void fillMatches(Fixture fixture)
    {

    	TableLayout table = (TableLayout) findViewById(R.id.fixture_table);
    	table.removeAllViews();
    	ArrayList<Match> matches = fixture.getMatches();
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
    
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		fillMatches((Fixture)parent.getItemAtPosition(position));
		DataHolder.getInstance().setFixture((Fixture)parent.getItemAtPosition(position));
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if (fixture==null)
			parent.setSelection(1);
		else
			//parent.setSelection(tournament.getFixtures().indexOf(fixture));
			parent.setSelection(2);
	}
	
    public void selectMatch(View view)
    {
    	Intent matchIntent = new Intent(getApplicationContext(),MatchActivity.class);
    	DataHolder.getInstance().setMatch((Match)view.getTag());
        startActivity(matchIntent);
    }

}
