package com.tourngen.droid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ArrayAdapter;


public class AddTeamsActivity extends Activity implements NewTeamFragment.NewTeamDialogListener{
	
	public ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teams);
        setTitle("Add teams");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ListView list = (ListView) this.findViewById(R.id.new_team_list);
        adapter  = new ArrayAdapter<String>(getApplicationContext(),R.layout.team_row);
        list.setAdapter(adapter);
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


	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		
		
	}


	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		
		
	}
    
}