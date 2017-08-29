package com.example.kapil.customerservice.InternetCheck;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNet 
{
	public static boolean IsInternet(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null) 
		{
			if (netInfo.isConnected()) 
			{
				return true;
			}
		}

		return false;
	}

}
