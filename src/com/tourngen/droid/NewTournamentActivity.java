package com.tourngen.droid;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.widget.TextView;

public class NewTournamentActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tournament);
        setTitle("New Tournament");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
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
        		Intent addTeamsIntent = new Intent(getApplicationContext(), AddTeamsActivity.class);
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

public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
			if (this.getTag()=="start")
			{
				TextView textview = (TextView)this.getActivity().findViewById(R.id.new_tournament_sDate);
				textview.setText(String.valueOf(day)+'-'+String.valueOf(month+1)+'-'+String.valueOf(year));
			}
			else if (this.getTag()=="end")
			{
				TextView textview = (TextView)this.getActivity().findViewById(R.id.new_tournament_eDate);
				textview.setText(String.valueOf(day)+'-'+String.valueOf(month+1)+'-'+String.valueOf(year));
			}
		}
	}
}
