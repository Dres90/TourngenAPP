package com.tourngen.droid.utils;

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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
	
	private static final int port = CustomHttpClient.HTTPS_PORT;
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
		
		StringBuilder builder;
		HttpClient client;
		String url;
		
		client = CustomHttpClient.getHttpClient();
		builder = new StringBuilder();
	    url = "https://tourngen.com:"+port+"/";
	    if (entity!=null)
	       	url=url+entity+"/";
	    if (identifier!=null)
	    	url=url+identifier;
	    if (querystring!=null)
	    	url=url+"?"+querystring;
	    
	    Log.v("URL",url);
	    if (json != null)
	    {
	    	Log.v("JSON",json.toString());	
	    }
		switch(method)
		{
		case WSRequest.GET:

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
		    
		case WSRequest.PUT:
			
		    HttpPut httpPut = new HttpPut(url);
			
		    try {
		    	
		    	StringEntity se = new StringEntity(json.toString());  
		    	se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		    	httpPut.setEntity(se);
		    	
		        HttpResponse response = client.execute(httpPut);
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
		    
		case WSRequest.POST:
			
		    HttpPost httpPost = new HttpPost(url);
			
		    try {
		    	
		    	StringEntity se = new StringEntity(json.toString());  
		    	se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		    	httpPost.setEntity(se);
		    	
		        HttpResponse response = client.execute(httpPost);
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

		case WSRequest.DELETE:
			
		    HttpDelete httpDel = new HttpDelete(url);
			
		    try {
		    	
		    	HttpResponse response = client.execute(httpDel);
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
	
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	    	
			StringBuilder builder;
			HttpClient client;
			String url;
			client = CustomHttpClient.getHttpClient();
			builder = new StringBuilder();
		    url = "https://tourngen.com:"+port+"/";
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
					JSONObject jSON = new JSONObject(builder.toString());
					if (jSON.has("status")&&jSON.getInt("status")==1)
						return true;
					else
						return false;
		        }
		          else
		          {
		        	  System.out.println(String.valueOf(statusCode));
		          }
		      } catch (Exception e) {
					return false;
				}
			}
	    return false;
	}
}
