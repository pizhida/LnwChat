package com.example.instac;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.instac.MainActivity.AsyncTaskParseJson;
import com.example.instac.client.ServerUtilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.app.Activity;
import android.app.ProgressDialog;
//import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ProductActivity extends Activity implements OnItemClickListener
{
	private ArrayList <arrData> arrMain;
	private HorizontalScrollView hsv;
	private static LinearLayout container;
	private static RelativeLayout.LayoutParams parm;
	private boolean gone;
	private static int value;
	private static int value1;
	private String k;
	private static String [] sendm;
	private static String [] sendp;
	public static boolean isLoad = false;
	private static ImageView [] im;
	private static ImageView [] dlim;
	private static LayoutParams lp;
	private static LayoutParams lpd;
	private static LayoutParams lo;
	private static int count;
	private static int [] m;
	private static int p;
	public static int ll;
	private static int st;
	public static int len;
	
	public static ImageLoader iml;
	public static DisplayImageOptions dmp;
	
	// JSON data
	public static String [] jn;
	public static String [] jp;
	public static String [] jpp;
	public static String [] jid;
	
	public static Drawable [] picd;
	
	public static Drawable [] jpd;
	
	public static Drawable dr;
	
	public static Drawable [] dra;
	final String TAG = "ProductAct";
	private String [] arrTitle = { "Slim TPU Red Case","Slim TPU Blue Case","Metallic" , "Metal Bumper", "Frosted Shield" };
	private static RelativeLayout [] selpic;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// Set main view
		setContentView(R.layout.prod_list_item);
		// Set boolean of HorizontalScrollView
		gone = false;
		ll = 3;
		
		
		// Image OS
		
		iml = ImageLoader.getInstance();
		dmp = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().resetViewBeforeLoading()
				.showImageForEmptyUri(R.drawable.product1).build();				
		
		
		st = 0;
		k = getIntent().getStringExtra("Jname");
		String g = getIntent().getStringExtra("Jn");
		
		jn = getIntent().getStringArrayExtra("jsname");
		jp = getIntent().getStringArrayExtra("jsprice");
		jpp = getIntent().getStringArrayExtra("jspic");
		jid = getIntent().getStringArrayExtra("jsid");
		len = getIntent().getIntExtra("Length", 2);
		Log.i(TAG, "ID " + jid[1] );
		boolean [] rCheck = new boolean[len];
		jpd = new Drawable[len];
		dra = new Drawable[len];
		Log.i(TAG,"STTTTTRIINGGG " + k);
		for(int i=0;i<len;i++)
		{
			jpp[i] = jpp[i].replaceAll("\\[", "");
			jpp[i] = jpp[i].replaceAll("\\]", "");
			jpp[i] = jpp[i].replaceAll("\\\\", "");
			jpp[i] = jpp[i].replaceAll("\"", "");
			jpp[i] = jpp[i].replaceAll("200", "");
			jpp[i] = jpp[i].replaceAll("resize_images", "raw");
			rCheck[i] = false;
			st++;
			Log.i(TAG,"Jid " + jid[i] + " " + "Jname " + jn[i] + "Jprice " + jp[i] + "Jpic " + jpp[i] );
		}
		if(getIntent().hasExtra("Jn"))
		{
			Log.i(TAG,"String Extra : " + g + " dsfwsf "+ len);
		}
		Log.i(TAG,"size of json" + ll);
		p = 0;
		
		value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)100, getResources().getDisplayMetrics());
		value1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)600, getResources().getDisplayMetrics());
		parm = new RelativeLayout.LayoutParams(value,value);
		
		dr = (getResources().getDrawable(R.drawable.loading));
		
		String url = "http://l.lnwfile.com/_/l/_resize/200/200/s1/mw/k2.jpg";
		url = url.replaceAll("200", "");
		url = url.replaceAll("resize", "raw");
		Log.i(TAG, "NEW STRING " + jpp[1]);
		
		Log.i(TAG, "gggggg" + dr);
		if(dr == null)
		{
			Log.i(TAG, "dr is null");
		}
		
		sendm = new String[len];
		sendp = new String[len];
		selpic = new RelativeLayout[len];
		im = new ImageView[len];
		dlim = new ImageView[len];
		lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lo = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			
		// Selected item picture ...
		for(int i=0;i<len;i++)
		{
			selpic[i] = new RelativeLayout(this);
			selpic[i].setLayoutParams(lp);
			im[i] = new ImageView(this);
			im[i].setClickable(true);
			int padding = getResources().getDimensionPixelOffset(R.dimen.dip);
			im[i].setPadding(padding, padding, padding, padding);			
			im[i].setImageDrawable(getResources().getDrawable(R.drawable.asuscase1));
			im[i].setLayoutParams(lo);
			selpic[i].addView(im[i]);
			iml.displayImage(jpp[i], im[i], dmp);
			
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams
					(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			dlim[i] = new ImageView(this);
			dlim[i].setImageResource(android.R.drawable.ic_notification_clear_all);
			dlim[i].setLayoutParams(rlp);
			selpic[i].addView(dlim[i]);	
		}
		// Parameter for each relative Layout
		
		// ArrayList for item data
		arrMain = new ArrayList <arrData>();
		arrData data;
		
		for(int i=0;i< len; i++)
		{
			data = new arrData();
			data.setDrawable(dr);
			data.setSelDrawable(dr);
			data.setTitle(jn[i]);
			data.setSubTitle(jp[i]);
			data.setCheck(rCheck[i]);
			data.setProdLay(selpic[i]);
			arrMain.add(data);
		}
		
		// List View Layout
		ListView lsv = (ListView)findViewById(R.id.listV);
		// add
		final adtImageWithMultipleLine adt = new adtImageWithMultipleLine(this,R.layout.product_item,R.id.productName,arrTitle);
		lsv.setAdapter(adt);
		lsv.setOnItemClickListener(this);	
		
		
		// Set Horizontal Scroll View
		HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.hor);
		hsv.setVisibility(View.GONE);
		
		
		// Container in HSV
		container = (LinearLayout) findViewById(R.id.cont);
		
		Log.i(TAG, "ffF" + ll);
		// Cancel and Accept Button
		Button cmd = (Button) findViewById(R.id.canc);
		Button cmd1 = (Button) findViewById(R.id.okay);
		
		cmd.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		cmd1.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{	
				
				for(int i=0;i<len;i++)
				{
					if(arrMain.get(i).isCheck() == true)
					{
						sendm[i] = arrMain.get(i).getTitle();
						sendp[i] = arrMain.get(i).getSubTitle();
					}
				}
				
				Intent intent = new Intent();
				intent.putExtra("name", sendm);
				intent.putExtra("price", sendp);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		for(int i=0;i<len;i++)
		{
			final int j = i;
			dlim[j].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Toast.makeText(ProductActivity.this,"Product that you unselected : " + jn[j], Toast.LENGTH_SHORT).show();
					arrMain.get(j).setSelected(false);
					arrMain.get(j).setCheck(false);
					removeSelectedItem(selpic[j], j);
				}	
			});
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
	{
		Toast.makeText(this,"This is : " + jn[position], Toast.LENGTH_SHORT).show();
	}
	
	/*
	 *  Add selected item into HorizontalScrollView
	 * 
	 */
	public void addSelectedItem(RelativeLayout rl, int position)
	{
		rl.setLayoutParams(parm);
		container.addView(rl);
	}
	
	public void removeSelectedItem(RelativeLayout rl, int position)
	{
		container.removeView(rl);
	}


	/*
	 * Adapter class for each item in list view
	 * 
	 * 
	 */
	private class adtImageWithMultipleLine extends ArrayAdapter<String>
	{

		public adtImageWithMultipleLine(Context context, int resource,int textViewResourceId, String[] objects)
		{
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		private class ListViewCustomItem
		{
			public ImageView imgMain;
			public TextView tvTitle;
			public TextView tvSub;
			public CheckBox box;
		}

		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			return arrMain.size();
		}

		@Override
		public String getItem(int position) 
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) 
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ListViewCustomItem lsvItem = null;
			if(convertView == null)
			{
				convertView = LayoutInflater.from(ProductActivity.this).inflate(R.layout.product_item, null);
				lsvItem = new ListViewCustomItem();
				lsvItem.imgMain = (ImageView)convertView.findViewById(R.id.productPic);
				lsvItem.tvTitle = (TextView)convertView.findViewById(R.id.productName);
				lsvItem.tvSub = (TextView)convertView.findViewById(R.id.price);
				lsvItem.box = (CheckBox)convertView.findViewById(R.id.selected);
				
				// new code
			
				lsvItem.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
				{
				
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						// TODO Auto-generated method stub
						int getPosition = (Integer)buttonView.getTag();
						arrMain.get(getPosition).setSelected(buttonView.isChecked());
						if((arrMain.get(getPosition).isSelected() == true) && (arrMain.get(getPosition).isCheck() == false))
						{
							
							
							Toast.makeText(ProductActivity.this,"Product that you selected : " + jn[getPosition], Toast.LENGTH_SHORT).show();
							arrMain.get(getPosition).setCheck(true);
							hsv = (HorizontalScrollView) findViewById(R.id.hor);
							hsv.setVisibility(View.VISIBLE);

							addSelectedItem(arrMain.get(getPosition).getProdLay(), getPosition);
	
						}
						else if(arrMain.get(getPosition).isSelected() == false  && (arrMain.get(getPosition).isCheck() == true))
						{
							Toast.makeText(ProductActivity.this,"Product that you unselected : " + jn[getPosition], Toast.LENGTH_SHORT).show();
							arrMain.get(getPosition).setCheck(false);
							
							
							removeSelectedItem(arrMain.get(getPosition).getProdLay(), getPosition);
							
							for(int i=0;i<len;i++)
							{
								if(arrMain.get(i).isCheck() == false)
								{
									gone = false;
								}
								else if(arrMain.get(i).isCheck() == true)
								{
									gone = true;
									break;
								}
							}
							if(gone == false)
							{
								hsv.setVisibility(View.GONE);
							}
						}
				}
			});
					
				convertView.setTag(lsvItem);
				convertView.setTag(R.id.productPic, lsvItem.imgMain);
				convertView.setTag(R.id.productName,lsvItem.tvTitle);
				convertView.setTag(R.id.price, lsvItem.tvSub);
				convertView.setTag(R.id.selected, lsvItem.box);
				
			}
			else
			{
				lsvItem = (ListViewCustomItem)convertView.getTag();
			}
			
			// This line is important
			lsvItem.box.setTag(position);
			lsvItem.box.setChecked(arrMain.get(position).isSelected());
			// ADdddd
			
			// getDrawable and other 
			if(arrMain.get(position).getDrawable() != null)
			{
				iml.displayImage(jpp[position], lsvItem.imgMain, dmp);
			}
			
			if(arrMain.get(position).getTitle() != null)
			{
				lsvItem.tvTitle.setText(arrMain.get(position).getTitle());
			}
			
			if(arrMain.get(position).getSubTitle() != null)
			{
				lsvItem.tvSub.setText(arrMain.get(position).getSubTitle());
			}
			
			return convertView;
		}	
	}
	
	public static Drawable createDrawableFromUrl(String imageWebAddress)
	{
	  Drawable drawable = null;

	  try
	  {
	    InputStream inputStream = new URL(imageWebAddress).openStream();
	    drawable = Drawable.createFromStream(inputStream, null);
	    inputStream.close();
	  }
	  catch (MalformedURLException ex) { }
	  catch (IOException ex) { }

	  return drawable;
	}
}
