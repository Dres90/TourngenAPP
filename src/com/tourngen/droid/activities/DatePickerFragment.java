package com.tourngen.droid.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.tourngen.droid.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment
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
			Calendar start = Calendar.getInstance();
			start.set(year, month, day, 0, 0, 0);
			((NewTournamentActivity) getActivity()).start = start;
			TextView textview = (TextView)this.getActivity().findViewById(R.id.new_tournament_sDate);
			DateFormat dateFormat = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
			textview.setText(dateFormat.format(start.getTime()));
		}
		else if (this.getTag()=="end")
		{
			Calendar end = Calendar.getInstance();
			end.set(year, month, day, 0, 0, 0);
			((NewTournamentActivity) getActivity()).end = end;
			TextView textview = (TextView)this.getActivity().findViewById(R.id.new_tournament_eDate);
			DateFormat dateFormat = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
			textview.setText(dateFormat.format(end.getTime()));
		}
	}
}

