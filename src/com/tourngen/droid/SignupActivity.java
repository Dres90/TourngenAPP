package com.tourngen.droid;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignupActivity extends Activity {
	
	ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }
    
    public void register(View view)
    {
		String username = ((EditText) findViewById(R.id.signup_user)).getText().toString();
		String password = ((EditText) findViewById(R.id.signup_pass)).getText().toString();
		String firstname = ((EditText) findViewById(R.id.signup_name)).getText().toString();
		String lastname = ((EditText) findViewById(R.id.signup_lastname)).getText().toString();
		String email = ((EditText) findViewById(R.id.signup_email)).getText().toString();
		if (username==null||password==null||firstname==null||lastname==null||email==null)
		{
			new AlertDialog.Builder(this).setTitle("Error").setMessage("All fields must be completed").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){}}).show();
		}
		else
		{
	    	progress = new ProgressDialog(this);
	    	progress.setTitle("Signing up");
	    	progress.setMessage("Please wait while we sign you up");
	    	progress.show();
			new SignUpTask().execute(username,password,firstname,lastname,email);
		}
    }
    public void confirmSignup(int result)
    {
    	progress.dismiss();
    	switch(result)
    	{
    	case -2:
			new AlertDialog.Builder(this).setTitle("Invalid E-mail").setMessage("Please input a valid e-mail address").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){}}).show();
    		break;
    	case -1:
			new AlertDialog.Builder(this).setTitle("User Taken").setMessage("Please select a different username").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){}}).show();
    		break;
    	case 0:
			new AlertDialog.Builder(this).setTitle("Error").setMessage("General Error").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){}}).show();
    		break;
    	case 1:
			new AlertDialog.Builder(this).setTitle("Success").setMessage("Signed up successfully!").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){finish();}}).show();
    		break;
    	}
    }
    
    private class SignUpTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
			JSONObject json = new JSONObject();
			String username = params[0];
			String password = params[1];
			String firstname = params[2];
			String lastname = params[3];
			String email = params[4];
			
			json.put("username", username);
			json.put("password", password);
			json.put("name", firstname);
			json.put("lastname", lastname);
			json.put("email", email);
			WSRequest request = new WSRequest(WSRequest.POST,"Login",null,null,json);
				json = request.getJSON();
				return json;
			} catch (JSONException e) {
				return null;
			}
		}
		
		@Override
        protected void onPostExecute(JSONObject result) {
			try {
					if (result==JSONObject.NULL)
					{
						confirmSignup(0);
					}
					else if(!result.getBoolean("success")&&result.getString("code").equals("invalid_email"))
					{	
						confirmSignup(-2);
					}
					else if(!result.getBoolean("success")&&result.getString("code").equals("user_taken"))
					{	
						confirmSignup(-1);
					}
					else if(!result.getBoolean("success"))
					{ 
						confirmSignup(0);
					}
					else
					{
						confirmSignup(1);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
        }
    }
}