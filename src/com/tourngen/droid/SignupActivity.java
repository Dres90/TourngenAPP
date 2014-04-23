package com.tourngen.droid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SignupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id)
        {
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void login(View view)
    {
        Intent login = new Intent(getApplicationContext(),TournamentListActivity.class);
        startActivity(login);
    }
    
    public void register(View view)
    {
    	//TODO Implement register
       finish();
    }

}