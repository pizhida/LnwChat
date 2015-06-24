package com.example.instac;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



//import com.example.instac.ChatActivity.AsyncTaskParseJson;

//import com.example.androidjsonparsing.MainActivity.AsyncTaskParseJson;

//import com.example.androidjsonparsing.JsonParser;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * @author pizhida
 *
 */
public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>
{	
	private SimpleCursorAdapter adapter;
	private ContactCursorAdapter cadapter;
	public static String [] jsn ;
	public static String [] jspr ;
	public static String [] jsp ;
	public static String [] jsid;
	public static int length;
	public static int init;
	// Add
	public static PhotoCache photoCache;
	//private ActionBar actionBar;
	//ListView listview;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		
		// Add from instachatX
		//setContentView(R.layout.activity_main);
		
	    //listview = (ListView) findViewById(R.id.contactslist);
		//listview.setOnItemClickListener(this);
		cadapter = new ContactCursorAdapter(this, null);
		//listview.setAdapter(cadapter);
		
		setListAdapter(cadapter);
		photoCache = new PhotoCache(this);
		
		// Previous Code		
		length = 0;
		init = 0;
		if(init == 0)
		{
			AsyncTaskParseJson asp = new AsyncTaskParseJson();
			asp.execute();
		}
		
		adapter = new SimpleCursorAdapter(this, 
				R.layout.main_list_item, 
				null, 
				new String[]{DataProvider.COL_NAME, DataProvider.COL_COUNT, DataProvider.COL_EMAIL}, 
				new int[]{R.id.text1, R.id.text2 , R.id.textEmail},
				0);
		
		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() 
		{
			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch(view.getId()) {
				case R.id.text2:
					int count = cursor.getInt(columnIndex);
					if (count > 0) {
						((TextView)view).setText(String.format("%d new message%s", count, count==1 ? "" : "s"));
					}
					return true;
					
				}
				return false;
			}
		});
		
		//setListAdapter(adapter);	
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.show();	
		actionBar.setTitle("You Are");
		actionBar.setSubtitle(Common.getPreferredEmail());		
		getLoaderManager().initLoader(0, null, this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			AddContactDialog dialog = new AddContactDialog();
			dialog.show(getFragmentManager(), "AddContactDialog");
			return true;
			
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;			
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra(Common.PROFILE_ID, String.valueOf(id));
		
		intent.putExtra("length", length);
		intent.putExtra("jsname", jsn);
		intent.putExtra("jsprice", jspr);
		intent.putExtra("jspic", jsp);
		intent.putExtra("jsid", jsid);		
		startActivity(intent);
	}	
	
	//----------------------------------------------------------------------------

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(this, 
				DataProvider.CONTENT_URI_PROFILE, 
				new String[]{DataProvider.COL_ID, DataProvider.COL_NAME, DataProvider.COL_COUNT, DataProvider.COL_EMAIL}, 
				null, 
				null, 
				DataProvider.COL_ID + " DESC"); 
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		//adapter.swapCursor(data);
		
		// new
		cadapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		//adapter.swapCursor(null);
		
		// new
		cadapter.swapCursor(null);
	}	
	
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> 
    {
        final String TAG = "AsyncTaskParseJson.java";
        private int lenn = 10;
        // set your json string url here
        String yourJsonStringUrl = "http://www.case-d.com/json/showroom/recommend";   
        private String [] jsonName;
        private String [] jsonPrice;
        private String [] jsonPic;
        private String [] jsonId;
        private Drawable [] jsonDraw;
        // contacts JSONArray
        JSONArray dataJsonArr = null; 
        @Override
        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... arg0) 
        {
            try { 
                // instantiate our json parser
                JsonParser jParser = new JsonParser(); 
                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);
                // get the array of users
                dataJsonArr = json.getJSONArray("products");
                lenn = dataJsonArr.length();
                length = dataJsonArr.length();                
                jsonName = new String[length];
                jsonPrice = new String[length];
                jsonPic = new String[length];
                jsonDraw = new Drawable[length];
                jsonId = new String[length];
                String iwa = "http://l.lnwfile.com/_resize_images/200/200/s1/mw/k2.jpg";               
                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) 
                {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    // Storing each json item in variable
                    String id = c.getString("id");
                    String firstname = c.getString("images");
                    String lastname = c.getString("price");
                    String status = c.getString("name");                    
                    jsonName[i] = status;
                    jsonPrice[i] = lastname;
                    jsonPic[i] = firstname;
                    jsonId[i] = id;   
 
                    // show the values in our logcat
                    Log.e(TAG, "id: " + firstname 
                            + ", price: " + lastname 
                           + ", status: " + status
                           + " , id: " + id);
                }
 
            } catch (JSONException e) {
                e.printStackTrace();
            } 
            return null;
        }
 
        @Override
        protected void onPostExecute(String strFromDoInBg) 
        {
        	Log.i(TAG, "ffff " + length);
        	for(int i=0;i<length;i++)
        	{
        		Log.i(TAG, "name: " + jsonName[i] 
                            + ", price: " + jsonPrice[i] 
                           + ", pic: " + jsonPic[i] 
                        + " , id " + jsonId[i]);
        	}
        	jsn = jsonName;
        	jspr = jsonPrice;
        	jsp = jsonPic;
        	jsid = jsonId;
        	for(int i=0;i<length;i++)
        	{
        		jsp[i] = jsp[i].replaceAll("\\[", "");
    			jsp[i] = jsp[i].replaceAll("\\]", "");
    			jsp[i] = jsp[i].replaceAll("\\\\", "");
    			jsp[i] = jsp[i].replaceAll("\"", "");
    			jsp[i] = jsp[i].replaceAll("200", "");
    			jsp[i] = jsp[i].replaceAll("resize_images", "raw");
        		Log.i("JSONAA", "name: " + jsn[i] 
                            + ", price: " + jspr[i] 
                           + ", pic: " + jsp[i] 
                        + " , id " + jsid[i]);
        	}
        	
        	
        }
        
    }
       
    // Adding Photo 
    
    
	public class ContactCursorAdapter extends CursorAdapter 
	{

		private LayoutInflater mInflater;

		public ContactCursorAdapter(Context context, Cursor c) 
		{
			super(context, c, 0);
			this.mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override public int getCount() 
		{
			return getCursor() == null ? 0 : super.getCount();
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) 
		{
			View itemLayout = mInflater.inflate(R.layout.main_list_item, parent, false);
			ViewHolder holder = new ViewHolder();
			itemLayout.setTag(holder);
			holder.tex1 = (TextView) itemLayout.findViewById(R.id.text1);
			holder.tex2 = (TextView) itemLayout.findViewById(R.id.text2);
			holder.texEmail = (TextView) itemLayout.findViewById(R.id.textEmail);
			holder.avatar = (ImageView) itemLayout.findViewById(R.id.avatar);
			return itemLayout;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) 
		{
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.tex1.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME)));
			holder.texEmail.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_EMAIL)));
			int count = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_COUNT));
			if (count > 0)
			{
				holder.tex2.setVisibility(View.VISIBLE);
				holder.tex2.setText(String.format("%d new message%s", count, count==1 ? "" : "s"));
			}
			else
				holder.tex2.setVisibility(View.GONE);

			photoCache.DisplayBitmap(requestPhoto(cursor.getString(cursor.getColumnIndex(DataProvider.COL_EMAIL))), holder.avatar);

		}
	}
    
	private static class ViewHolder 
	{
		TextView tex1;
		TextView tex2;
		TextView texEmail;
		ImageView avatar;
	}
	
	@SuppressLint("InlinedApi")
	private Uri requestPhoto(String email)
	{
		Cursor emailCur = null;
		Uri uri = null;
		try{
			int SDK_INT = android.os.Build.VERSION.SDK_INT;
			if(SDK_INT >= 11)
			{
				String[] projection = { ContactsContract.CommonDataKinds.Email.PHOTO_URI };
				ContentResolver cr = getContentResolver();
				emailCur = cr.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection,
						ContactsContract.CommonDataKinds.Email.ADDRESS + " = ?", 
								new String[]{email}, null);
				if (emailCur != null && emailCur.getCount() > 0) 
				{	
					if (emailCur.moveToNext()) {
						String photoUri = emailCur.getString( emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.PHOTO_URI));
						if(photoUri != null)
							uri = Uri.parse(photoUri);
					}
				}
			}else if(SDK_INT < 11) 
			{
				String[] projection = { ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
				ContentResolver cr = getContentResolver();
				emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
						projection,
						ContactsContract.CommonDataKinds.Email.ADDRESS + " = ?",
						new String[]{email}, null);
				if (emailCur.moveToNext()) {
					int columnIndex = emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.CONTACT_ID);
					long contactId = emailCur.getLong(columnIndex);
					uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,	contactId);
					uri = Uri.withAppendedPath(uri,	ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(emailCur != null)
					emailCur.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return uri;
	}

	
	// new
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) 
//	{
//		Intent intent = new Intent(this, ChatActivity.class);
//		intent.putExtra(Common.PROFILE_ID, String.valueOf(id));
//		
//		intent.putExtra("length", length);
//		intent.putExtra("jsname", jsn);
//		intent.putExtra("jsprice", jspr);
//		intent.putExtra("jspic", jsp);
//		intent.putExtra("jsid", jsid);
//		startActivity(intent);
//		
//	}
	

}