package net.tetrakoopa.canardhttpd.preference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import net.tetrakoopa.canardhttpd.R;

public class ShareIntentPreferencesFragment extends PreferenceFragment {

	private static final String PREFERENCES_PREFIX = "share_intent.";

	public static final String SHOW_MAIN_UI = PREFERENCES_PREFIX+"show_main_ui";
	public static boolean DEFAULT_SHOW_MAIN_UI = false;
	public static final String AUTOMATIC_FILE_SHARING = PREFERENCES_PREFIX+"automatic_file_sharing";
	public static final String AUTOMATIC_TEXT_SHARING = PREFERENCES_PREFIX+"automatic_text_sharing";

	private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals(SHOW_MAIN_UI)) {
				final boolean checked = sharedPreferences.getBoolean(key, DEFAULT_SHOW_MAIN_UI);
				updateAutomaticThingsSharing(checked);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_share_intent);

		final SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(getActivity());
		preferenceManager.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

		updateAutomaticThingsSharing(preferenceManager.getBoolean(SHOW_MAIN_UI, DEFAULT_SHOW_MAIN_UI));
	}

	private void updateAutomaticThingsSharing(boolean showMainUi) {

		getPreferenceScreen().findPreference(AUTOMATIC_FILE_SHARING).setEnabled(showMainUi);
		getPreferenceScreen().findPreference(AUTOMATIC_TEXT_SHARING).setEnabled(showMainUi);
	}


}
