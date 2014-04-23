package com.tourngen.droid;

import java.io.File;
import java.util.ArrayList;

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
        Intent login = new Intent(getApplicationContext(),TournamentListActivity.class);
        String username = ((TextView) findViewById(R.id.login_user)).getText().toString();
        Config.getInstance().setUserName(username);
        Config.store(getApplicationContext());
        startActivity(login);
    }
    
    public void confirmLogin(int status)
    {
    	
    }
    
    public void signup(View view)
    {
        Intent signup = new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(signup);
    }
    
    
    private class LoginTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			
			
			
			
			
			
			return 1;
		}
        
        protected int onPostExecute(int result) {
            return result;
        }


    }
}
