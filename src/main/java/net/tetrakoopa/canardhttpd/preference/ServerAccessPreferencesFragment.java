package net.tetrakoopa.canardhttpd.preference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import net.tetrakoopa.canardhttpd.R;

public class ServerAccessPreferencesFragment extends PreferenceFragment {

	private static final String PREFERENCES_PREFIX = "server_access.";
	public static final String USE_EXTERNAL_IP = PREFERENCES_PREFIX+"use_external_ip";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_server_access);
	}

}
