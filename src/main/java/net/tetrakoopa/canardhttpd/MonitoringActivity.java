package net.tetrakoopa.canardhttpd;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.tetrakoopa.canardhttpd.view.action.MonitoringAction;

public class MonitoringActivity extends AppCompatActivity {

	public final static String TAG = CanardHTTPDActivity.TAG + "-" + "Monitoring";

	private MonitoringAction action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canard_httpd);
		final ActionBar actionBar = getActionBar();
		if (actionBar!=null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		View mainView = findViewById(R.id.main);

		action = new MonitoringAction(null, savedInstanceState, mainView);
		action.doPrepareLayoutAndStuff();

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.canard_httpd, menu);

		return true;
	}*/

	@Override
	public void onStart() {
		super.onStart();

	}
	@Override
	public void onStop() {
		super.onStop();
	}


}
