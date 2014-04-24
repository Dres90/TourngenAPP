package com.tourngen.droid;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(getFilesDir());
        File file = new File("config");
        if(file.exists()&&Config.load(getApplicationContext()))
        {
        	((TextView) findViewById(R.id.login_user)).setText(Config.getInstance().getUserName());
        }
        else
        {
        	Config.getInstance().setIds(new ArrayList<Integer>());
        	Config.getInstance().setNames(new ArrayList<String>());
        	Config.store(getApplicationContext());
        } 
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    
    public void login(View view)
    {	
    	new LoginTask().execute("andres","password");
    }
    
    public void confirmLogin(int status)
    {
    	if (status==1)
    	{
            Intent login = new Intent(getApplicationContext(),TournamentListActivity.class);
            String username = ((TextView) findViewById(R.id.login_user)).getText().toString();
            Config.getInstance().setUserName(username);
            Config.store(getApplicationContext());
            startActivity(login);
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
			String identifier = "f379cfcf-e8cd-4990-ad96-e1ecdfc08edb";
			String querystring = "password="+password+"&identifier="+identifier;
			WSRequest request = new WSRequest(WSRequest.GET,"Login",username,querystring,null);
			try {
				json = request.getJSON();
				return json;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
        protected void onPostExecute(JSONObject result) {
			if (result==null)
			System.out.println("Null");
			else
			System.out.println(result.toString());
        }


    }
}
