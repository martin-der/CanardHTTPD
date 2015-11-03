package net.tetrakoopa.canardhttpd.service.http.writer;

import android.content.Context;
import android.util.Log;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {

	public static enum Method {
		GET, POST;
		
		
		public static Method fromName(String name) {
			if (name==null) 
				return null;
			for (Method possibleMatch : Method.values()) {
				if (possibleMatch.name().equals(name.toUpperCase()))
					return possibleMatch;
			}
			throw new IllegalArgumentException();
		}
		
	}

	private static Context context;

	protected final static String TAG = CanardHTTPDActivity.TAG;

	public final static String DEFAULT_ENCODING = "UTF-8";
	public final static String DEFAULT_PAGE_ENCODING = DEFAULT_ENCODING;
	private final static String DEFAULT_ASSET_ENCODING = DEFAULT_ENCODING;

	public final static String escapedXmlContent(String string) {
		return string.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	public final static String escapedXmlAttribut(String string) {
		return string.replace("\"", "\\\"");
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

	public static void setContext(Context context) {
		BaseServlet.context = context;
	}

	protected static Context getContext() {
		return context;
	}

}
