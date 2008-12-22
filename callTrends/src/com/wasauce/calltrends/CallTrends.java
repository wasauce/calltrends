package com.wasauce.calltrends;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.*;
import android.widget.TextView;
import android.app.NotificationManager;
import android.net.http.*;
import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.impl.cookie.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP; 


public class CallTrends extends Activity {
    /** Called when the activity is first created. */
	TextView textMsg;  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //setContentView(R.layout.httplayout); 
        
        // Associate component with layout
        textMsg=(TextView)findViewById(R.layout.main); 
        
        // Querying for a cursor is like querying for any SQL-Database
        Cursor c = getContentResolver().query(
                  android.provider.CallLog.Calls.CONTENT_URI,
                  null, null, null,
                  android.provider.CallLog.Calls.DATE + " DESC");
        startManagingCursor(c);
        
        // Retrieve the column-indixes of phoneNumber, date and calltype
        int numberColumn = c.getColumnIndex(
                  android.provider.CallLog.Calls.NUMBER);
        int dateColumn = c.getColumnIndex(
                  android.provider.CallLog.Calls.DATE);
        // type can be: Incoming, Outgoing or Missed
        int typeColumn = c.getColumnIndex(
                  android.provider.CallLog.Calls.TYPE);
        
        // Will hold the calls, available to the cursor
        ////ArrayList<String> callList = new ArrayList<String>();
        
        // Loop through all entries the cursor provides to us.
        while (!(c.isAfterLast())){
        	String callerPhoneNumber = c.getString(numberColumn);
            int callDate = c.getInt(dateColumn);
            int callType = c.getInt(typeColumn);
            Date date=new Date(callDate);

            ////callList.add("  | # " + callerPhoneNumber + "  | Call date: " + date + " | Call type:"+ callType);

            DefaultHttpClient httpclient = new DefaultHttpClient(); 
        
            HttpResponse response; 
            try {
        	
            	// Preparing the post operation 
            	HttpPost httpost = new HttpPost("http://calltrends.appspot.com/datain");
            
            	List <NameValuePair> namevaluepair = new ArrayList <NameValuePair>();
            	namevaluepair.add(new BasicNameValuePair("name", "name"));
            	namevaluepair.add(new BasicNameValuePair("password", "somepassword"));
              
            	httpost.setEntity(new UrlEncodedFormEntity(namevaluepair, HTTP.UTF_8));
              
            	// Post, check and show the result (not really spectacular, but works):
            	response = httpclient.execute(httpost);
              
            	Log.d("httpPost", "Response : " + response.getStatusLine());

            	textMsg.setText("Response: " + response.getStatusLine());
              
            } catch (ClientProtocolException e) {
            	Log.e("httpPost", e.getMessage());
            	e.printStackTrace();
            } catch (IOException e) {
            	Log.e("httpPost", e.getMessage());
            	e.printStackTrace();
            } catch (Exception e) {
            	Log.e("httpPost", e.getMessage());
            	e.printStackTrace();
            }
        }      
    }   
}