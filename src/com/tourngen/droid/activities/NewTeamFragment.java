package com.tourngen.droid.activities;
import com.tourngen.droid.R;
import com.tourngen.droid.objects.Team;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;


public class NewTeamFragment extends DialogFragment implements DialogInterface.OnClickListener
{
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.new_team, null))
               .setPositiveButton(android.R.string.ok, this)
               .setNegativeButton(android.R.string.cancel, this);
        return builder.create();
    }
    
    @Override
    public void onDetach()
    {
    	((AddTeamsActivity) this.getActivity()).addButtonListener();
    	super.onDetach();
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which)
		{
		case DialogInterface.BUTTON_POSITIVE:
			CharSequence teamname;
			getDialog().findViewById(R.id.new_team_info).requestFocus();
			EditText nameView = (EditText) getDialog().findViewById(R.id.new_team_name);
			teamname = nameView.getText();
			if (teamname.toString().equals(""))
				return;
			AddTeamsActivity parent = ((AddTeamsActivity) this.getActivity());
			parent.teamnames.add(teamname);
			Team team = new Team(teamname.toString(), parent.tournament);
			EditText emailView = (EditText) getDialog().findViewById(R.id.new_team_email);
			CharSequence email = emailView.getText();
			team.setEmail(email.toString());
			EditText infoView = (EditText) getDialog().findViewById(R.id.new_team_info);
			CharSequence info = infoView.getText();
			team.setInfo(info.toString());
			parent.teams.add(team);
			break;
		}
		
	}
}