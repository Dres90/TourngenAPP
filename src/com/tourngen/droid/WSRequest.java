package com.tourngen.droid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

public class WSRequest {
	
	public static final int GET = 0;
	public static final int POST = 1;
	public static final int PUT = 2;
	public static final int DELETE = 3;
	
	public static final int LOGIN = 0;
	public static final int TOURNAMENT = 1;
	public static final int TEAM = 2;
	public static final int FIXTURE = 3;
	public static final int MATCH = 4;
	
	private int method;
	private String entity;
	private String identifier;
	private String querystring;
	private JSONObject json;
	
	
	public WSRequest(int method, String entity, String identifier, String querystring, JSONObject json)
	{
		this.method = method;
		this.entity = entity;
		this.identifier = identifier;
		this.querystring = querystring;
		this.json = json;
	}
	
	public JSONObject getJSON() throws JSONException
	{
		switch(method)
		{
		case WSRequest.GET:
		
			StringBuilder builder = new StringBuilder();
		    HttpClient client = CustomHttpClient.getHttpClient();
		    String url = "https://tourngen.com:2800/"+entity+"/"+identifier;
		    if (querystring!=null)
		    	url=url+"?"+querystring;
		    
		    System.out.println(url);
		    HttpGet httpGet = new HttpGet(url);
			
		    try {
		        HttpResponse response = client.execute(httpGet);
		        StatusLine statusLine = response.getStatusLine();
		        int statusCode = statusLine.getStatusCode();
		        if (statusCode == 200) {
		          HttpEntity entity = response.getEntity();
		          InputStream content = entity.getContent();
		          BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		          String line;
		          while ((line = reader.readLine()) != null) {
		            builder.append(line);
		          }
		        }
		          else
		          {
		        	  System.out.println(String.valueOf(statusCode));
		          }
		      } catch (ClientProtocolException e) {
		        e.printStackTrace();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		    return new JSONObject(builder.toString());
		}
		return null;
	}

}
