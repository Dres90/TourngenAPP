package com.tourngen.droid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TextView;

public class TournamentActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        fillPositions();
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        	case R.id.sync_button:
            	Toast.makeText(getApplicationContext(), "Sync button pressed!", Toast.LENGTH_SHORT).show();
                return true;	
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void fillPositions()
    {
    	
    	TableLayout table = (TableLayout) findViewById(R.id.tournament_table);
    	for(int i =0;i<5;i++)
    	{
    		TableRow row=(TableRow) this.getLayoutInflater().inflate(R.layout.position_row, null);
    		//((TextView)row.findViewById(R.id.position_num)).setText(i+1);
    		table.addView(row);    	
    	}
    	table.requestLayout();
    }
}
