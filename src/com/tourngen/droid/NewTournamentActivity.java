package com.tourngen.droid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.CheckBox;

public class NewTournamentActivity extends Activity{
	
	public Calendar start;
	public Calendar end;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tournament);
        setTitle("New Tournament");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        
		TextView textview = (TextView) findViewById(R.id.new_tournament_sDate);
		DateFormat dateFormat = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
		textview.setText(dateFormat.format(start.getTime()));
		
		TextView textview1 = (TextView) findViewById(R.id.new_tournament_eDate);
		DateFormat dateFormat1 = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
		textview1.setText(dateFormat1.format(end.getTime()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
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
        	case R.id.confirm_edit:
        		Tournament tournament = new Tournament(((TextView) findViewById(R.id.new_tournament_name)).getText().toString());
        		tournament.setStartDate(start);
        		tournament.setEndDate(end);
        		tournament.setIspublic(!((CheckBox)findViewById(R.id.new_private_box)).isChecked());
        		tournament.setHomeandaway(((CheckBox)findViewById(R.id.new_haw_box)).isChecked());
        		tournament.setInfo(((TextView) findViewById(R.id.new_tournament_info)).getText().toString());
        		Intent addTeamsIntent = new Intent(getApplicationContext(), AddTeamsActivity.class);
        		addTeamsIntent.putExtra("tournament", tournament);
        		startActivity(addTeamsIntent);
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void showDatePicker(View view)
    {
    	switch(view.getId())
    	{
    	case R.id.new_tournament_sDate:
        	DialogFragment startFragment = new DatePickerFragment();
            startFragment.show(getFragmentManager(), "start");
    		break;
    	case R.id.new_tournament_eDate:
        	DialogFragment endFragment = new DatePickerFragment();
            endFragment.show(getFragmentManager(), "end");
    		break;
    	}
    }

}
