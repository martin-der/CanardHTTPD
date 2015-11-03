package net.tetrakoopa.mdu.android.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferenceUtil {


	/** @throws IllegalArgumentException if there is no preference with this id */	
		public static <TYPE> TYPE getExistingValue(Context context, String id) {
			@SuppressWarnings("unchecked")
			TYPE value = (TYPE)getUntypedValue(context, id);
			if (value == null) {
				throw new IllegalArgumentException("No preference such preference '"+id);
			}
			return value;
		}
		public static <TYPE> TYPE getValue(Context context, String id, TYPE defaultz) {
			@SuppressWarnings("unchecked")
			TYPE value = (TYPE)getUntypedValue(context, id);
			if (value == null) {
				return (defaultz != null) ? defaultz : null;
			}
			return value;
		}

		private static Object getUntypedValue(Context context, String id) {
			if (id == null) {
				throw new IllegalArgumentException("(String)id cannot be null");
			}
			return PreferenceManager.getDefaultSharedPreferences(context).getAll().get(id);
		}
}
