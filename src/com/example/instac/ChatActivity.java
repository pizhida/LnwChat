package com.example.instac;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.instac.client.Constants;
//import com.example.instac.ProductActivity.AsyncTaskParseJson;
import com.example.instac.client.GcmUtil;
import com.example.instac.client.ServerUtilities;

/**
 * @author appsrox.com
 *
 */
public class ChatActivity extends Activity implements MessagesFragment.OnFragmentInteractionListener, EditContactDialog.OnFragmentInteractionListener 
{

	private EditText msgEdit;
	private Button sendBtn;
	private ImageButton prodBtn;
	private String profileId;
	private String profileName;
	private String profileEmail;
	private String [] name;
	private String [] price;
	public static String [] jname;
	public static String [] jprice;
	public static String [] jpic;
	public static String [] jpix;
	public static String [] jid;
	public static Drawable [] jdraw;
	public static int ll;
	private static String kname;
	public static Drawable [] dra;
	private static Intent it;
	
	private GcmUtil gcmUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		kname = "gg";
		profileId = getIntent().getStringExtra(Common.PROFILE_ID);
		
		
		// get Intent from MainActivity
		ll = getIntent().getIntExtra("length", 2);
		jname = getIntent().getStringArrayExtra("jsname");
		jprice = getIntent().getStringArrayExtra("jsprice");
		jpic = getIntent().getStringArrayExtra("jspic");
		jid = getIntent().getStringArrayExtra("jsid");
		
		for(int i=0;i<ll;i++)
		{
			Log.i("chatACt", "name: " + jname[i] 
                    + ", price: " + jprice[i] 
                   + ", pic: " + jpic[i] 
                + " , id " + jid[i]);
		}				
		msgEdit = (EditText) findViewById(R.id.msg_edit);
		sendBtn = (Button) findViewById(R.id.send_btn);
		it = new Intent (getApplicationContext(), ProductActivity.class);
		prodBtn = (ImageButton) findViewById(R.id.prod_btn);
		prodBtn.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String j = "Jjjj";
				it.putExtra("Length", ll);
				it.putExtra("Jname", j);
				it.putExtra("Jn", kname);
				it.putExtra("jsname", jname);
				it.putExtra("jsprice", jprice);
				it.putExtra("jspic", jpic);
				it.putExtra("jsid", jid);
				startActivityForResult(it, 1);		
			}
		});
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Cursor c = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), null, null, null, null);
		if (c.moveToFirst()) {
			profileName = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
			profileEmail = c.getString(c.getColumnIndex(DataProvider.COL_EMAIL));
			actionBar.setTitle(profileName);
		}
		actionBar.setSubtitle("connecting ...");
		registerReceiver(registrationStatusReceiver, new IntentFilter(Common.ACTION_REGISTER));
		gcmUtil = new GcmUtil(getApplicationContext());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			EditContactDialog dialog = new EditContactDialog();
			Bundle args = new Bundle();
			args.putString(Common.PROFILE_ID, profileId);
			args.putString(DataProvider.COL_NAME, profileName);
			dialog.setArguments(args);
			dialog.show(getFragmentManager(), "EditContactDialog");
			return true;
			
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;			
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) 
	{
		switch(v.getId()) {
		case R.id.send_btn:
			send(msgEdit.getText().toString());
			msgEdit.setText(null);
			break;	
		}
	}
	
	@Override
	public void onEditContact(String name) {
		getActionBar().setTitle(name);
	}	
	
	@Override
	public String getProfileEmail() {
		return profileEmail;
	}	
	
	public void send(final String txt) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) 
            {
                String msg = "";
                try {
                    ServerUtilities.send(txt, profileEmail);
                    
        			ContentValues values = new ContentValues(2);
        			values.put(DataProvider.COL_MSG, txt);
        			values.put(DataProvider.COL_TO, profileEmail);
        			// new thing
        			//values.put(DataProvider.COL_NAME, profileName);
        			getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);
        			
                } catch (IOException ex) {
                    msg = "Message could not be sent";
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            	if (!TextUtils.isEmpty(msg)) {
            		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            	}
            }
        }.execute(null, null, null);		
	}	
	
	public void sendPro(final String txt) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    ServerUtilities.send( txt, profileEmail);
                    
        			ContentValues values = new ContentValues(2);
        			values.put(DataProvider.COL_MSG, txt);
        			values.put(DataProvider.COL_TO, profileEmail);
        			// new thing
        			
        			//values.put(DataProvider.COL_NAME, profileName );
        			getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);
        			
                } catch (IOException ex) {
                    msg = "Message could not be sent";
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            	if (!TextUtils.isEmpty(msg)) {
            		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            	}
            }
        }.execute(null, null, null);		
	}	


	@Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    if (data == null) {return;}
	    name = data.getStringArrayExtra("name");
	    price = data.getStringArrayExtra("price");
	    for(int i=0;i<name.length;i++)
	    {
	    	if(name[i] != null)
	    	{
	    		sendPro(name[i] + "\n" + price[i] + " BAHT");
	    	}
	    }
	    
	}
	
	@Override
	protected void onPause() {
		ContentValues values = new ContentValues(1);
		values.put(DataProvider.COL_COUNT, 0);
		getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), values, null, null);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(registrationStatusReceiver);
		gcmUtil.cleanup();
		super.onDestroy();
	}
	
	//--------------------------------------------------------------------------------
	
	private BroadcastReceiver registrationStatusReceiver = new  BroadcastReceiver() 
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && Common.ACTION_REGISTER.equals(intent.getAction())) {
				switch (intent.getIntExtra(Common.EXTRA_STATUS, 100)) {
				case Common.STATUS_SUCCESS:
					getActionBar().setSubtitle("online");
					sendBtn.setEnabled(true);
					break;
					
				case Common.STATUS_FAILED:
					getActionBar().setSubtitle("offline");					
					break;					
				}
			}
		}
	};	
}

