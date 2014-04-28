package com.tourngen.droid;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MatchActivity extends Activity{
	
	Match match;
	ProgressDialog progress;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
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
		((EditText) findViewById(R.id.home_score)).setText(String.valueOf(match.getHomeGoal()));
		((TextView) findViewById(R.id.match_aTeam)).setText(match.getAway().getName());
		((EditText) findViewById(R.id.away_score)).setText(String.valueOf(match.getAwayGoal()));
		((TextView) findViewById(R.id.match_info)).setText(match.getInfo());
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
    		break;
    	case R.id.decrease_home:
    		goal = Integer.valueOf(home.getText().toString());
    		if(goal>0)
    		{
    		goal--;
    		match.setHomeGoal(goal);
    		home.setText(String.valueOf(goal));
    		}
    		break;
    	case R.id.increase_away:
    		goal = Integer.valueOf(away.getText().toString());
    		goal++;
    		match.setAwayGoal(goal);
    		away.setText(String.valueOf(goal));
    		break;
    	case R.id.decrease_away:
    		goal = Integer.valueOf(away.getText().toString());
    		if (goal>0)
    		{
    		goal--;
    		match.setAwayGoal(goal);
    		away.setText(String.valueOf(goal));
    		}
    		break;
    	}
    	match.setLast_updated(Calendar.getInstance());
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
    	private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    	SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(dateFormat, Locale.US);
    	
		@Override
		protected Integer doInBackground(Void... params) {
			
			JSONObject json;
			token = Config.getInstance().getToken();
			String querystring = "token="+EscapeUtils.encodeURIComponent(token);
			WSRequest request = new WSRequest(WSRequest.GET,"Match",String.valueOf(match.getExtId()),querystring,null);
			int updated = 0;
			try {
				json = request.getJSON();
				System.out.println(json);
				if(json.getBoolean("success"))
				{
					if(json.has("last_updated"))
					{
						String lastupdString = json.getString("last_updated");
						if (!lastupdString.equals("null"))
						{
							Calendar lupd = Calendar.getInstance();
							lupd.setTime(ISO8601DATEFORMAT.parse(lastupdString));
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
						JSONObject jsonPut = new JSONObject();
						
						jsonPut.put("token", token);
						
						JSONObject jMatch = new JSONObject();
						jMatch.put("score_home",match.getHomeGoal());
						jMatch.put("score_away",match.getAwayGoal());
						jMatch.put("score_home",match.getHomeGoal());
						jsonPut.put("match", jMatch);

						WSRequest requestPut = new WSRequest(WSRequest.PUT,"Match",String.valueOf(match.getExtId()),null,jsonPut);
							json = requestPut.getJSON();
							System.out.println(json);
							if (json.getBoolean("success"))
								result = 1;
							else
								result = 2;
						break;
					default:
						break;
					}
					return result;
				}
				return 2;
				
			} catch (JSONException e) {
				return 2;
			} catch (ParseException e) {
				return 2;
			}
		}
		
		
		@Override
        protected void onPostExecute(Integer result) {
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
				default:
					Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
					break;
				}
		}

    }	
}
