package net.tetrakoopa.canardhttpd;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.tetrakoopa.mdu.android.util.ResourcesUtil;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class LogActivity extends AppCompatActivity {

	public final static String MANIFEST_ACTIVITY = CanardHTTPDActivity.MANIFEST_PACKAGE+".LogActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		final ActionBar actionBar = getActionBar();
		if (actionBar!=null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		//View mainView = findViewById(R.id.main);

	}

	private String message(int id) {
		return ResourcesUtil.getString(this, id);
	}

}
