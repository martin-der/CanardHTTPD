package net.tetrakoopa.canardhttpd.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.tetrakoopa.canardhttpd.R;

public class MainActivityPreferencesFragment extends PreferenceFragment {

	private static final String PREFERENCES_PREFIX = "main_ui.";
	public static final String SHOW_ADRESS_AND_SHARE_BUTTON = PREFERENCES_PREFIX+"show_adress_and_share_button";
	public static final String SHOW_LOG_AND_ACTIVITY_BUTTONS = PREFERENCES_PREFIX+"show_log_and_activity_buttons";


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_main_ui);
	}

}
