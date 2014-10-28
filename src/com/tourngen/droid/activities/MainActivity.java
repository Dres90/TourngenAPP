package com.tourngen.droid.activities;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.tourngen.droid.R;
import com.tourngen.droid.utils.Config;
import com.tourngen.droid.utils.WSRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
        if(!Config.load(getApplicationContext()))
        {
        	Config.getInstance().setIds(new ArrayList<Integer>());
        	Config.getInstance().setNames(new ArrayList<String>());
        	Config.getInstance().setPrivileges(new ArrayList<Integer>());
        	Config.store(getApplicationContext());
        }
    	if(Config.getInstance().getToken()!=null)
    	{
    		Intent login = new Intent(getApplicationContext(),TournamentListActivity.class);
    		startActivity(login);
    	}
    }

    
    public void login(View view)
    {	
    	progress = new ProgressDialog(this);
    	progress.setTitle("Logging in");
    	progress.setMessage("Please wait while we log you in");
    	progress.show();
    	new LoginTask().execute(((TextView) findViewById(R.id.login_user)).getText().toString(),((TextView) findViewById(R.id.login_password)).getText().toString());
    }
    
    public void confirmLogin(int status)
    {
    	progress.dismiss();
    	if (status==1)
    	{
            Intent login = new Intent(getApplicationContext(),TournamentListActivity.class);
            String username = ((TextView) findViewById(R.id.login_user)).getText().toString();
            Config.getInstance().setUserName(username);
            Config.store(getApplicationContext());
            startActivity(login);
    	}
    	if (status==0)
    	{
    		new AlertDialog.Builder(this).setTitle("Error").setMessage("Log in error").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which){}}).show();    		
    	}
    	
    }
    
    public void signup(View view)
    {
        Intent signup = new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(signup);
    }
    
    private class LoginTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			
			JSONObject json;
			String username = params[0];
			String password = params[1];
			String identifier = UUID.randomUUID().toString();
			String querystring = "password="+password+"&identifier="+identifier;
			WSRequest request = new WSRequest(WSRequest.GET,"Login",username,querystring,null);
			try {
				json = request.getJSON();
				return json;
			} catch (JSONException e) {
				return null;
			}
		}
		
		@Override
        protected void onPostExecute(JSONObject result) {
			try {
					if (result==null)
						confirmLogin(0);
					else if(!result.getBoolean("success"))
					{	
						confirmLogin(0);
					}
					else
					{
						Config.getInstance().setToken(result.getString("token"));
						confirmLogin(1);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
        }
    }
}
