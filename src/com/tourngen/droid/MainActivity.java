package com.tourngen.droid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	private int tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        startActivity(login);
    }
    
    public void signup(View view)
    {
        Intent signup = new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(signup);
    }

}
