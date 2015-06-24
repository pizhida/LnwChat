package com.example.instac;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Patterns;

import com.example.instac.client.Constants;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * @author appsrox.com
 *
 */
public class Common extends Application 
{
	
	public static final String PROFILE_ID = "profile_id";
	
	public static final String ACTION_REGISTER = "com.appsrox.instachat.REGISTER";
	public static final String EXTRA_STATUS = "status";
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILED = 0;
	public static final int count = 0;
	//tpublic static final boolean isLoad = false;
	
	//parameters recognized by demo server
	public static final String FROM = "email";
	public static final String REG_ID = "regId";
	public static final String MSG = "msg";
	public static final String TO = "email2";	
	
	
	public static final int img1 = R.drawable.product1;
	public static final int img2 = R.drawable.product2;
	public static final int img3 = R.drawable.product3;
	
	public static String[] email_arr;
	
	public static Drawable [] prodraw;
	
	private static SharedPreferences prefs;
	
	public String profileID;

	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		List<String> emailList = getEmailList();
		email_arr = emailList.toArray(new String[emailList.size()]);
		
		// UniversalImageLoader
		
		// UNIVERSAL IMAGE LOADER SETUP
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc().cacheInMemory()
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();
		
		ImageLoader.getInstance().init(config);
		// END - UNIVERSAL IMAGE LOADER SETUP
	}
	
	private List<String> getEmailList() 
	{
		List<String> lst = new ArrayList<String>();
		Account[] accounts = AccountManager.get(this).getAccounts();
		for (Account account : accounts) {
		    if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
		        lst.add(account.name);
		    }
		}
		return lst;
	}
	
	public static String getPreferredEmail() {
		return prefs.getString("chat_email_id", email_arr.length==0 ? "abc@example.com" : email_arr[0]);
	}
	
	public static String getDisplayName() {
		String email = getPreferredEmail();
		return prefs.getString("display_name", email.substring(0, email.indexOf('@')));
	}
	
	public static boolean isNotify() {
		return prefs.getBoolean("notifications_new_message", true);
	}
	
	public static String getRingtone() {
		return prefs.getString("notifications_new_message_ringtone", android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
	}
	
	public static String getServerUrl() {
		return prefs.getString("server_url_pref", Constants.SERVER_URL);
	}
	
	public static String getSenderId() {
		return prefs.getString("sender_id_pref", Constants.SENDER_ID);
	}	
    	
}

