package com.tourngen.droid.utils;

import java.util.ArrayList;
import java.util.Collections;

import com.tourngen.droid.objects.Fixture;
import com.tourngen.droid.objects.Match;
import com.tourngen.droid.objects.Team;
import com.tourngen.droid.objects.Tournament;


public class MatchGenerator {
	
	ArrayList<Team> teams;
	Tournament tournament;
	boolean homeAndAway;
	
	public MatchGenerator(Tournament tournament)
	{
		this.tournament = tournament;
		this.teams = tournament.getTeams();
		this.homeAndAway = tournament.isHomeandaway();
		
	}

	public ArrayList<Fixture> getFixtures()
	{
		
    	int teamCount = teams.size();
    	int matchCount = (teamCount*(teamCount-1))/2;
    	int fixtureCount;
    	if (teamCount%2==0)
    		fixtureCount=teamCount-1;
    	else
    		fixtureCount=teamCount;
    	int gamesPerFixture = matchCount/fixtureCount;
    	
    	System.out.println("Team Count "+ teamCount);
    	System.out.println("Match Count "+ matchCount);
    	System.out.println("Fixture Count "+ fixtureCount);
    	System.out.println("Games per Fixture "+ gamesPerFixture);
    	
    	ArrayList<Team> tempTeams =  new ArrayList<Team>(teams);
    	
    	if(teams.size()%2!=0)
    		tempTeams.add(null);
    	
    	ArrayList<Team> home =  new ArrayList<Team>();
    	ArrayList<Team> away =  new ArrayList<Team>();
    	
    	for (int i = 0; i<tempTeams.size();i++)
    	{
    		if (i<tempTeams.size()/2)
    			home.add(tempTeams.get(i));
    		else
    			away.add(tempTeams.get(i));
    	}
    	
    	ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
    	
    	for (int i =0; i<fixtureCount; i++)
    	{
    		Fixture fixture = new Fixture(tournament,i+1);
    		ArrayList<Match> matches = new ArrayList<Match>();
    		
    		for (int j=0;j<home.size();j++)
    		{
    			Team hTeam = home.get(j);
    			Team aTeam = away.get(j);
    			if (hTeam==null||aTeam==null)
    			{
    				continue;
    			}
    			else
    			{
    				Match match = new Match(tournament,hTeam,aTeam);
    				matches.add(match);
    			}
    		}
    		Collections.shuffle(matches);
    		fixture.setMatches(matches);
    		fixtures.add(fixture);
    		Team movedH = home.remove(home.size()-1);
    		Team movedA = away.remove(0);
    		if (teams.size()>2)
    		{
    		home.add(1,movedA);
    		}
    		away.add(movedH);
    	}
    	
    	Collections.shuffle(fixtures);
    	int i = 1;
    	for (int j = 0;j<fixtures.size();j++)
    	{
    		Fixture fixture = fixtures.get(j);
    		fixture.setNumber(j+1);
    		for (int k = 0;k<fixture.getMatches().size();k++)
    		{
    			Match match = fixture.getMatches().get(k);
    			match.setFixture(fixture);
    			if (i%2==0)
    				match.revertTeams();
    			i++;
    		}
    		
    	}
    	if (homeAndAway)
    	{
    		int size = fixtures.size();
        	for (int j = 0;j<size;j++)
        	{
        		Fixture fixture = fixtures.get(j);
        		Fixture newFixture = new Fixture(tournament,2*size-j);
        		ArrayList<Match> matches = new ArrayList<Match>();
        		for (int k = 0;k<fixture.getMatches().size();k++)
        		{
        			Match match = fixture.getMatches().get(k).getRevertedMatch();
        			match.setFixture(newFixture);
        			matches.add(match);
        		}
        		newFixture.setMatches(matches);
        		fixtures.add(size,newFixture);
        	}

    	}

		return fixtures;
	}
	
}

