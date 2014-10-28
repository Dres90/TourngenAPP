package com.tourngen.droid.activities;

import com.tourngen.droid.R;
import com.tourngen.droid.objects.Team;
import com.tourngen.droid.objects.Tournament;
import com.tourngen.droid.utils.DataHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TeamListActivity extends Activity implements OnItemClickListener{
	
	Tournament tournament;
	ArrayAdapter<Team> teams;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_list);
    }
    @Override
    protected void onResume()
    {
    	super.onResume();
        tournament = DataHolder.getInstance().getTournament();
        setTitle(tournament.getName()+": Teams");
        ListView listview = (ListView) findViewById(R.id.teams);
        teams = new ArrayAdapter<Team>(getApplicationContext(),R.layout.general_list_row,R.id.list_text,tournament.getTeams());
        listview.setAdapter(teams);
        listview.setOnItemClickListener(this);
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
        Intent teamIntent = new Intent(getApplicationContext(),TeamActivity.class);
        DataHolder.getInstance().setTeam((Team)parent.getAdapter().getItem(position));
        startActivity(teamIntent);
		
	}
}
