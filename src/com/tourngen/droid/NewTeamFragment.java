package com.tourngen.droid;
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
			((AddTeamsActivity) this.getActivity()).teamnames.add(teamname);
			break;
		}
		
	}
}