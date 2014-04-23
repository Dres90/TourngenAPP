package com.tourngen.droid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MatchListActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_list);
        setTitle("My Tournament: Matches");
        fillMatches();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id)
        {
        	case R.id.sync_button:
            	Toast.makeText(getApplicationContext(), "Sync button pressed!", Toast.LENGTH_SHORT).show();
                return true;	
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void fillMatches()
    {
    	char letter = 'A';
    	
    	TableLayout table = (TableLayout) findViewById(R.id.match_table);
    	table.removeAllViews();
    	for(int i =0;i<50;i++)
    	{
    		TableRow row=(TableRow) this.getLayoutInflater().inflate(R.layout.match_row, null);
    		((TextView)row.findViewById(R.id.match_num)).setText(String.valueOf(i+1));
    		((TextView)row.findViewById(R.id.match_home)).setText("Team "+letter);
    		((TextView)row.findViewById(R.id.match_score)).setText(2 + " - " + 1);
    		((TextView)row.findViewById(R.id.match_away)).setText("Team "+letter++);
    		((TextView)row.findViewById(R.id.match_date)).setText("2014-01-01");
    		table.addView(row);    	
    	}
    	table.requestLayout();
    }
    public void selectMatch(View view)
    {
    	Intent matchIntent = new Intent(getApplicationContext(),MatchActivity.class);
        startActivity(matchIntent);
    }

}
