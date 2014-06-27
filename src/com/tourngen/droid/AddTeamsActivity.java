package com.tourngen.droid;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;


public class AddTeamsActivity extends Activity implements OnClickListener{
	
	public ArrayAdapter<CharSequence> teamnames;
	public Tournament tournament;
	public ArrayList<Team> teams;
	static final int NEW_TOURNAMENT_REQUEST = 1; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teams);
        setTitle("Add teams");
        ListView list = (ListView) this.findViewById(R.id.new_team_list);
        teamnames  = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.team_row,R.id.new_team_text);
        list.setAdapter(teamnames);
        Intent intent = getIntent();
        tournament = (Tournament)intent.getSerializableExtra("tournament");
        teams = tournament.getTeams();
        updateButtonListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_teams, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
        	case R.id.new_team_button:
        		addTeam();
        		return true;
        	case R.id.confirm_edit:
        		Intent newMatchesIntent = new Intent(getApplicationContext(), NewMatchesActivity.class);
        		tournament.setTeams(teams);
        		newMatchesIntent.putExtra("tournament", tournament);
        		startActivityForResult(newMatchesIntent,NEW_TOURNAMENT_REQUEST);
        		return true;
        		
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    private void addTeam()
    {
    	DialogFragment startFragment = new NewTeamFragment();
        startFragment.show(getFragmentManager(), "new team");
    }
    
    public void addButtonListener()
    {
		ListView list = (ListView) this.findViewById(R.id.new_team_list);
		int i = list.getChildCount();
		if(i>0)
		{
			
			View view = list.getChildAt(i-1);
			ImageButton button = (ImageButton) view.findViewById(R.id.new_team_delete);
			button.setOnClickListener(this);
			button.setTag(teamnames.getItem(i-1));
		}
    }

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.new_team_delete:
			deleteTeam((CharSequence)v.getTag());
			break;
		}


	}

	private void deleteTeam(CharSequence name)
	{
		ListView list = (ListView) this.findViewById(R.id.new_team_list);
		int count = teamnames.getCount();
		int pos = teamnames.getPosition(name);
		for (int i=pos;i<count-1;i++)
		{
			View view = list.getChildAt(i);
			View nextView = list.getChildAt(i+1);
			ImageButton button = (ImageButton) view.findViewById(R.id.new_team_delete);
			ImageButton nextButton = (ImageButton) nextView.findViewById(R.id.new_team_delete);
			button.setTag(nextButton.getTag());
		}
		teamnames.remove(name);
		teams.remove(teams.get(pos));
	}
    
    private void updateButtonListener()
    {
		ListView list = (ListView) this.findViewById(R.id.new_team_list);
		for (int i = 0; i< list.getChildCount();i++)
		{
			View view = list.getChildAt(i);
			ImageButton button = (ImageButton) view.findViewById(R.id.new_team_delete);
			button.setOnClickListener(this);
			button.setTag(teamnames.getItem(i));
		}

    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == NEW_TOURNAMENT_REQUEST) {
	        if (resultCode == RESULT_OK) {
	        	setResult(RESULT_OK);
	        	finish();
	        }
	    }
	}
}