package net.tetrakoopa.canardhttpd.service.http.writer;

import android.content.Context;
import android.util.Log;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

public abstract class CommonHTMLComponent {

	private final Context context;

	protected final static String TAG = CanardHTTPDActivity.TAG;

	public final static String DEFAULT_ENCODING = "UTF-8";
	public final static String DEFAULT_PAGE_ENCODING = DEFAULT_ENCODING;
	private final static String DEFAULT_ASSET_ENCODING = DEFAULT_ENCODING;

	protected CommonHTMLComponent(Context context) {
		this.context = context;
	}

	public final static String escapedXmlContent(String string) {
		return string.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	public final static String escapedXmlAttribut(String string) {
		return string.replace("\"", "\\\"");
	}

	protected void writeAsset(String assetName, Writer writer) {
		final Reader input;
		try {
			input = new InputStreamReader(getAsset(assetName));
		} catch (IOException e) {
			Log.e(CanardHTTPDActivity.TAG, "Could not get asset '" + assetName + "'", e);
			return;
		}

		char buffer[] = new char[200];
		int l;
		try {
			while ((l = input.read(buffer)) > 0) {
				writer.write(buffer, 0, l);
			}
		} catch (IOException e) {
			Log.e(CanardHTTPDActivity.TAG, "Could not read asset '" + assetName + "'", e);
		}
	}

	protected InputStream getAsset(String assertName) throws IOException {
		return context.getAssets().open(assertName);
	}
}
