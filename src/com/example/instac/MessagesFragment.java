package com.example.instac;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//import android.R;
import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 * 
 * @author appsrox.com
 */
public class MessagesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat[] df = new DateFormat[] {
		DateFormat.getDateInstance(), DateFormat.getTimeInstance()};

	private OnFragmentInteractionListener mListener;
	private SimpleCursorAdapter adapter;
	private Date now;
	// aDDD
	public static ImageLoader iml;
	public static DisplayImageOptions dmp;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
		}
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		now = new Date();
		
		
		// adDD
		iml = ImageLoader.getInstance();
		dmp = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().resetViewBeforeLoading()
				.showImageForEmptyUri(R.drawable.product1).build();	
		
		// Adapter object
		adapter = new SimpleCursorAdapter(getActivity(), 
				//R.layout.chat_list_item, 
				R.layout.chat_pro,
				null, 
				new String[]{DataProvider.COL_MSG, DataProvider.COL_AT}, 
				new int[]{R.id.text1, R.id.text2},
				0);
		
		
		// Adapter function
				adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() 
				{
			
						@Override
							public boolean setViewValue(View view, Cursor cursor, int columnIndex) 
							{
						switch(view.getId()) 
						{
							case R.id.text1:
							TextView tt = (TextView) view;
							tt.setText("FFF");	
							LinearLayout root = (LinearLayout) view.getParent().getParent();
							LinearLayout lin = (LinearLayout) root.findViewById(R.id.lin);
							ImageView imgv = (ImageView) root.findViewById(R.id.orderPro);
							Drawable d = getResources().getDrawable(R.drawable.box2);
							Drawable dr = getResources().getDrawable(R.drawable.box);
							int width = 500;
							int height = 500;
							LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
							LinearLayout.LayoutParams parms1 = new LinearLayout.LayoutParams(0,0);
														
							for(int i=0; i<ChatActivity.ll;i++)
							{
								final int j = i;
								if(cursor.getString(columnIndex).contains(ChatActivity.jname[i]))
								{
									iml.displayImage(ChatActivity.jpic[i], imgv, dmp);
									imgv.setLayoutParams(parms);
									
									lin.setOnLongClickListener(new OnLongClickListener(){

										@Override
										public boolean onLongClick(View v) 
										{
											Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.case-d.com/product/" + ProductActivity.jid[j]));
											startActivity(browserIntent);
											return false;
										}
										
									});
									break;
								}
								else
								{
									imgv.setLayoutParams(parms1);
									imgv.setImageDrawable(null);
								}
							}
							if (cursor.getString(cursor.getColumnIndex(DataProvider.COL_FROM)) == null) 
							{
								root.setGravity(Gravity.RIGHT);
								root.setPadding(50, 10, 10, 10);
								lin.setBackground(dr);
							} else 
							{
								root.setGravity(Gravity.LEFT);
								root.setPadding(10, 10, 50, 10);
								lin.setBackground(d);
							}
							break;
							
							
								
							case R.id.text2:
							TextView tv = (TextView) view;
							tv.setText(getDisplayTime(cursor.getString(columnIndex)));
							return true;					
						}
						return false;
					}
				});		
		
		setListAdapter(adapter);
	}	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		getListView().setDivider(null);
		
		Bundle args = new Bundle();
		args.putString(DataProvider.COL_EMAIL, mListener.getProfileEmail());
		getLoaderManager().initLoader(0, args, this);
	}

	@Override
	public void onDetach() 
	{
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener 
	{
		public String getProfileEmail();
	}
	
	private String getDisplayTime(String datetime) 
	{
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date dt = sdf.parse(datetime);
			TimeZone tz = TimeZone.getDefault();
			df[0].setTimeZone(tz);
			df[1].setTimeZone(tz);
			if (now.getYear()==dt.getYear() && now.getMonth()==dt.getMonth() && now.getDate()==dt.getDate()) {
				return df[1].format(dt);
			}
			return df[0].format(dt);
		} catch (ParseException e) {
			return datetime;
		}
	}
	
	//----------------------------------------------------------------------------

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) 
	{
		String profileEmail = args.getString(DataProvider.COL_EMAIL);
		CursorLoader loader = new CursorLoader(getActivity(), 
				DataProvider.CONTENT_URI_MESSAGES, 
				null, 
				DataProvider.COL_FROM + " = ? or " + DataProvider.COL_TO + " = ?",
				new String[]{profileEmail, profileEmail}, 
				DataProvider.COL_AT + " DESC"); 
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) 
	{
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) 
	{
		adapter.swapCursor(null);
	}

}
