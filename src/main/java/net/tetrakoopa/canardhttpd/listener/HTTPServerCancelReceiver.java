package net.tetrakoopa.canardhttpd.listener;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.tetrakoopa.canardhttpd.CanardHTTPDService;

public class HTTPServerCancelReceiver extends BroadcastReceiver {
		 
		@Override
		 public void onReceive(Context context, Intent intent) {
		   Intent service = new Intent();
		   service.setComponent(new ComponentName(context,CanardHTTPDService.class));
		   context.stopService(service);
			Toast.makeText(context, "Clicked some button", Toast.LENGTH_SHORT).show();
		 }
}
