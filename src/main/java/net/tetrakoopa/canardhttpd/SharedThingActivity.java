package net.tetrakoopa.canardhttpd;

import android.app.Activity;
import android.os.Bundle;

public class SharedThingActivity extends Activity {

	public static final String ARG_SHARED_THING = SharedThingActivity.class.getName() + "-SHARED_THING";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.shared_thing_detail);

		// View mainView = findViewById(R.id.main);
	}

	@Override
	public void onStart() {
		super.onStart();
	}


}
