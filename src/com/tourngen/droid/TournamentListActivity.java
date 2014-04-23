package com.tourngen.droid;


import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TournamentListActivity extends Activity implements OnItemClickListener{

	ArrayAdapter<String> tournaments;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_list);
    }

    @Override
    protected void onResume()
    {
    	super.onResume();
    	setTitle("My Tournaments");
    	Config config = Config.getInstance();
        tournaments = new ArrayAdapter<String>(getApplicationContext(),R.layout.general_list_row,R.id.list_text,Config.getInstance().getNames());
        ListView listview = (ListView) findViewById(R.id.tournaments);
        listview.setAdapter(tournaments);
        listview.setOnItemClickListener(this);
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
            	Toast.makeText(getApplicationContext(), "Sync button pressed!", Toast.LENGTH_SHORT).show();
                return true;
        	case R.id.new_tournament_button:
        		Intent newTournamentIntent = new Intent(getApplicationContext(),NewTournamentActivity.class);
        		startActivity(newTournamentIntent);
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void logout(View view)
    {
    	finish();
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
        Intent tournamentIntent = new Intent(getApplicationContext(),TournamentActivity.class);
        int tId = Config.getInstance().getIds().get(position);
        DataHolder.getInstance().setTournament(Tournament.getTournament("t"+tId,getApplicationContext()));
        startActivity(tournamentIntent);
	}
	
	private void addTestTournament()
	{
		Tournament tournament = new Tournament("test");
		tournament.setStartDate(Calendar.getInstance());
		tournament.setEndDate(Calendar.getInstance());
		tournament.setHomeandaway(true);
		tournament.setInfo("Test tournament");
		tournament.setIspublic(true);
		
		ArrayList<Team> teams = new ArrayList<Team>();
		for (int i = (int)'A'; i<=(int)'E'; i++)
		{
			Team team = new Team("Team "+(char)i,tournament);
			team.setEmail("team"+(char)i+"@mail.com");
			team.setInfo("Test team "+(char)i);
			teams.add(team);
		}
		
		tournament.setTeams(teams);
		MatchGenerator generator = new MatchGenerator(tournament);
		tournament.setFixtures(generator.getFixtures());
		
		ArrayList<Fixture> fixtures = tournament.getFixtures();
		for (int i = 0; i<fixtures.size()-2; i++)
		{
			Fixture fixture = fixtures.get(i);
			ArrayList<Match> matches = fixture.getMatches();
			for (int j = 0; j< matches.size(); j++)
			{
				matches.get(j).setPlayed(true);
			}
		}
		tournament.store(getApplicationContext());
	}
}
