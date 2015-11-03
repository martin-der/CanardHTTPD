package net.tetrakoopa.mdu.android.util;

import android.content.Context;

public class ResourcesUtil {
	
	public static String getString(Context context, int stringId) {
		return context.getResources().getString(stringId);
	}
	
	public static String getNamefromId(Context context, int id) {
		return "android.resource://"+context.getPackageName() +"/"+id;
	}


}
