package com.tourngen.droid;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;


public class AddTeamsActivity extends Activity implements OnClickListener{
	
	public ArrayAdapter<CharSequence> teamnames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teams);
        setTitle("Add teams");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ListView list = (ListView) this.findViewById(R.id.new_team_list);
        teamnames  = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.team_row,R.id.new_team_text);
        list.setAdapter(teamnames);
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
        	case android.R.id.home:
        		NavUtils.navigateUpFromSameTask(this);
        		return true;
        	case R.id.new_team_button:
        		addTeam();
        		return true;
        	case R.id.confirm_edit:
        		Toast.makeText(getApplicationContext(), "Save button pressed!", Toast.LENGTH_SHORT).show();
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
		
		
	}
    
}