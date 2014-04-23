package com.tourngen.droid;

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


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
        Intent teamIntent = new Intent(getApplicationContext(),TeamActivity.class);
        DataHolder.getInstance().setTeam((Team)parent.getAdapter().getItem(position));
        startActivity(teamIntent);
		
	}
}
