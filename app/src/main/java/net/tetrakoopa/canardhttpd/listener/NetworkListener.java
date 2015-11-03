package net.tetrakoopa.canardhttpd.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkListener extends BroadcastReceiver {

	// public static void registerStorageListener(Context context) {
	// IntentFilter filter = new IntentFilter();
	// filter.addAction(Intent.EV);
	// NetworkListener listener = new NetworkListener();
	// context.registerReceiver(listener, filter);
	// }

	@Override
	public void onReceive(Context context, Intent arg1) {
		Toast.makeText(context, "Network changed !", Toast.LENGTH_SHORT).show();
	}

}
