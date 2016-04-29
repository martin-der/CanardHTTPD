package net.tetrakoopa.canardhttpd;

import android.preference.PreferenceActivity;

import net.tetrakoopa.canardhttpd.preference.MainActivityPreferencesFragment;
import net.tetrakoopa.canardhttpd.preference.ServerAccessPreferencesFragment;

import java.util.List;

public class CanardHTTPDPreferenceActivity extends /*AppCompat*/PreferenceActivity {

	@Override
	public void onBuildHeaders(List<PreferenceActivity.Header> target)
	{
		loadHeadersFromResource(R.xml.preferences_headers, target);
	}

	@Override
	protected boolean isValidFragment(String fragmentName)
	{
		return ServerAccessPreferencesFragment.class.getName().equals(fragmentName)
				|| MainActivityPreferencesFragment.class.getName().equals(fragmentName);
	}

}
