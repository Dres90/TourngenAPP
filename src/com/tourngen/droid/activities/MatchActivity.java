package com.tourngen.droid.activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.tourngen.droid.R;
import com.tourngen.droid.objects.Match;
import com.tourngen.droid.utils.Config;
import com.tourngen.droid.utils.DataHolder;
import com.tourngen.droid.utils.EscapeUtils;
import com.tourngen.droid.utils.SyncUtils;
import com.tourngen.droid.utils.WSRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MatchActivity extends Activity{
	
	Match match;
	int p;
	ProgressDialog progress;
	private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        match = DataHolder.getInstance().getMatch();
        p = match.getTournament().getPrivilege(); 
        if (p>=1&&p<=2)
        {
        	setContentView(R.layout.match);
        }
        else
        	setContentView(R.layout.match_no_priv);
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
        match = DataHolder.getInstance().getMatch();
        setTitle(match.getTournament().getName());
        renderViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
   

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
        	case R.id.sync_button:
        		progress = new ProgressDialog(this);
        		progress.setTitle("Saving Results");
        		progress.setMessage("Please wait while we sync your results");
        		progress.show();
            	new SyncMatchTask().execute();
                return true;	
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void renderViews()
    {
    	((TextView) findViewById(R.id.match_fixture)).setText(String.valueOf(match.getFixture().getNumber()));
    	DateFormat dateFormat = new SimpleDateFormat("dd/LLL/yyyy", Locale.US);
		((TextView) findViewById(R.id.match_date)).setText(dateFormat.format(match.getDate().getTime()));
		((ToggleButton) findViewById(R.id.match_played)).setChecked(match.isPlayed());
		((TextView) findViewById(R.id.match_hTeam)).setText(match.getHome().getName());
		((TextView) findViewById(R.id.match_aTeam)).setText(match.getAway().getName());
		((TextView) findViewById(R.id.match_info)).setText(match.getInfo());
        if (p>=1&&p<=2)
        {
        	EditText homeEdit = (EditText) findViewById(R.id.home_score);
        	EditText awayEdit = (EditText) findViewById(R.id.away_score);
    		homeEdit.setText(String.valueOf(match.getHomeGoal()));
    		awayEdit.setText(String.valueOf(match.getAwayGoal()));
    		TextWatcher watcher= new TextWatcher() {
                public void afterTextChanged(Editable s) {
                	EditText homeEdit = (EditText) findViewById(R.id.home_score);
                	EditText awayEdit = (EditText) findViewById(R.id.away_score);
                	try{
                		match.setHomeGoal(Integer.valueOf(homeEdit.getText().toString()));
                		match.setAwayGoal(Integer.valueOf(awayEdit.getText().toString()));
                	}
                	catch (NumberFormatException e){
                		match.setHomeGoal(0);
                		match.setAwayGoal(0);
                	}
            		match.setLast_updated(Calendar.getInstance());
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                      //Do something or nothing.                
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Do something or nothing
                }
            };
            homeEdit.addTextChangedListener(watcher);
            awayEdit.addTextChangedListener(watcher);
        }
        else
        {
    		((TextView) findViewById(R.id.home_score)).setText(String.valueOf(match.getHomeGoal()));
    		((TextView) findViewById(R.id.away_score)).setText(String.valueOf(match.getAwayGoal()));
        }
        
        
    }
   
    public void onClick(View view)
    {
    	EditText home = (EditText) findViewById(R.id.home_score);
    	EditText away = (EditText) findViewById(R.id.away_score);
    	int goal;
    	switch(view.getId())
    	{
    	case R.id.increase_home:
    		goal = Integer.valueOf(home.getText().toString());
    		goal++;
    		match.setHomeGoal(goal);
    		home.setText(String.valueOf(goal));
        	match.setLast_updated(Calendar.getInstance());
    		break;
    	case R.id.decrease_home:
    		goal = Integer.valueOf(home.getText().toString());
    		if(goal>0)
    		{
    		goal--;
    		match.setHomeGoal(goal);
    		home.setText(String.valueOf(goal));
        	match.setLast_updated(Calendar.getInstance());
    		}
    		break;
    	case R.id.increase_away:
    		goal = Integer.valueOf(away.getText().toString());
    		goal++;
    		match.setAwayGoal(goal);
    		away.setText(String.valueOf(goal));
        	match.setLast_updated(Calendar.getInstance());
    		break;
    	case R.id.decrease_away:
    		goal = Integer.valueOf(away.getText().toString());
    		if (goal>0)
    		{
    		goal--;
    		match.setAwayGoal(goal);
    		away.setText(String.valueOf(goal));
        	match.setLast_updated(Calendar.getInstance());
    		}
    		break;
    	}

    }
    public void changePlayed(View view){
    	match.setPlayed(((ToggleButton) findViewById(R.id.match_played)).isChecked());
    	match.setLast_updated(Calendar.getInstance());
    }
    
    @Override
    protected void onPause(){
    	DataHolder.getInstance().getTournament().store(getApplicationContext());
    	super.onPause();
    }
    
    private class SyncMatchTask extends AsyncTask<Void, Void, Integer> {
    	
    	private String token;

    	
		@Override
		protected Integer doInBackground(Void... params) {
			
			JSONObject json;
			token = Config.getInstance().getToken();
			String querystring = "token="+EscapeUtils.encodeURIComponent(token);
			WSRequest request = new WSRequest(WSRequest.GET,"Match",String.valueOf(match.getExtId()),querystring,null);
			TimeZone tz = TimeZone.getDefault();
			int offset = tz.getOffset(Calendar.getInstance().getTimeInMillis())/1000/60/60;
			int updated = 0;
			try {
				if (WSRequest.isOnline(getApplicationContext()))
				{
					json = request.getJSON();
					Log.v("JSON",json.toString());
					if(json.getBoolean("success"))
					{
						if(json.has("last_updated"))
						{
							String lastupdString = json.getString("last_updated");
							if (!lastupdString.equals("null"))
							{
								Calendar lupd = Calendar.getInstance();
								lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
								lupd.add(Calendar.HOUR_OF_DAY, offset);
								updated = match.getLast_updated().compareTo(lupd);
							}
						}
						int result = 0;
						switch (updated)
						{
						case -1:
							if(json.has("last_updated"))
							{
								String lastupdString = json.getString("last_updated");
								if (!lastupdString.equals("null"))
								{
									Calendar lupd = Calendar.getInstance();
									lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
									lupd.add(Calendar.HOUR_OF_DAY, offset);
									match.setLast_updated(lupd);
								}
							}
							if(json.has("date"))
							{
								String dateString = json.getString("date");
								if (!dateString.equals("null"))
								{
									Calendar date = Calendar.getInstance();
									date.setTime(ISO8601DATEFORMAT.parse(dateString));
									match.setDate(date);
								}
							}
							match.setInfo(json.getString("info"));
							match.setHomeGoal(json.getInt("score_home"));
							match.setAwayGoal(json.getInt("score_away"));
							if (json.getInt("played")==1)
								match.setPlayed(true);
							else
								match.setPlayed(false);
							match.getTournament().store(getApplicationContext());
							result = -1;
							break;
						case 1:
					        if (p>=1&&p<=2)
					        {
					        	if(SyncUtils.sendMatch(match.getTournament(), match.getExtId()))
					        	{
					        		result = 1;
					        	}
					        	else
								{
									result = 2;
								}
								break;
					        }
					        else
					        {
								if(json.has("last_updated"))
								{
									String lastupdString = json.getString("last_updated");
									if (!lastupdString.equals("null"))
									{
										Calendar lupd = Calendar.getInstance();
										lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
										lupd.add(Calendar.HOUR_OF_DAY, offset);
										match.setLast_updated(lupd);
									}
								}
								if(json.has("date"))
								{
									String dateString = json.getString("date");
									if (!dateString.equals("null"))
									{
										Calendar date = Calendar.getInstance();
										date.setTime(ISO8601DATEFORMAT.parse(dateString));
										match.setDate(date);
									}
								}
								match.setInfo(json.getString("info"));
								match.setHomeGoal(json.getInt("score_home"));
								match.setAwayGoal(json.getInt("score_away"));
								if (json.getInt("played")==1)
									match.setPlayed(true);
								else
									match.setPlayed(false);
								match.getTournament().store(getApplicationContext());
								result = -1;
								break;
					        }

						default:
							break;
						}
						return result;
				}
				return -3;
			}
			else return -2;
			} catch (JSONException e) {
				return 3;
			} catch (ParseException e) {
				return 4;
			}
		}

		
		@Override
        protected void onPostExecute(Integer result) {
				renderViews();
				progress.dismiss();
				switch (result)
				{
				case -1:
					Toast.makeText(getApplicationContext(), "New match data loded!", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Toast.makeText(getApplicationContext(), "New match data sent!", Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(getApplicationContext(), "Match data is up to date!", Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(getApplicationContext(), "Connection problem, please try again later", Toast.LENGTH_LONG).show();
					break;
				default:
					Toast.makeText(getApplicationContext(), "Error! "+result, Toast.LENGTH_SHORT).show();
					break;
				}
		}

    }	
}
