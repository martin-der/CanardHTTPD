package net.tetrakoopa.canardhttpd.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.tetrakoopa.canardhttpd.R;

public class MainActivityPreferencesFragment extends PreferenceFragment {

	private static final String PREFERENCES_PREFIX = "main_ui.";
	public static final String SHOW_ADDRESS_AND_SHARE_BUTTON = PREFERENCES_PREFIX+"show_address_and_share_button";
	public static final boolean DEFAULT_SHOW_ADRESS_AND_SHARE_BUTTON = true;
	public static final String SHOW_LOG_AND_ACTIVITY_BUTTONS = PREFERENCES_PREFIX+"show_log_and_activity_buttons";
	public static final boolean DEFAULT_SHOW_LOG_AND_ACTIVITY_BUTTONS = true;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_main_ui);
	}

}
