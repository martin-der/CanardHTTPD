package net.tetrakoopa.canardhttpd.service.http.writer;

import android.content.Context;
import android.util.Log;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;


public abstract class BaseWriter extends CommonHTMLComponent {

	protected BaseWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	protected void writeAsset(String assetName, PrintStream stream) {
		final InputStream input;
		try {
			input = getAsset(assetName);
		} catch (IOException e) {
			Log.e(CanardHTTPDActivity.TAG, "Could not get asset '" + assetName + "'", e);
			return;
		}

		byte buffer[] = new byte[200];
		int l;
		try {
			while ((l = input.read(buffer)) > 0) {
				stream.write(buffer, 0, l);
			}
		} catch (IOException e) {
			Log.e(CanardHTTPDActivity.TAG, "Could not read asset '" + assetName + "'", e);
		}
	}

	protected InputStream getAsset(String assertName) throws IOException {
		return context.getAssets().open(assertName);
	}

}
