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
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;

public abstract class CommonHTMLComponent {

	public final static String DEFAULT_ENCODING = "UTF-8";

	public enum Method {
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

	protected final Context context;
	protected final String httpContext;

	protected CommonHTMLComponent(Context context, String httpContext) {
		this.context = context;
		this.httpContext = httpContext;
	}

	public final static String escapeToUrl(String string) {
		try {
			return URLEncoder.encode(string, CommonHTMLComponent.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException uee) {
			throw new IllegalArgumentException(uee);
		}
	}
	public final static String unescapeFromUrl(String string) {
		try {
			return URLDecoder.decode(string, CommonHTMLComponent.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException uee) {
			throw new IllegalArgumentException(uee);
		}
	}

	public final static String escapedXmlAlsoSpace(String string) {
		return escapedXml(string).replace(" ", "&#160;");
	}
	public final static String escapedXmlAlsoSpaceAndCR(String string) {
		return escapedXml(string).replace(" ", "&#160;").replace("\n","<br/>");
	}

	public final static String escapedXml(String string) {
		return string.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	protected void writeAsset(String assetName, Writer writer) throws IOException {
		final Reader input;
		try {
			input = new InputStreamReader(getAsset(assetName));
		} catch (IOException e) {
			Log.e(CanardHTTPDActivity.TAG, "Could not get asset '" + assetName + "'", e);
			return;
		}

		char buffer[] = new char[200];
		int l;
		while ((l = input.read(buffer)) > 0) {
			writer.write(buffer, 0, l);
		}
	}

	protected InputStream getAsset(String assertName) throws IOException {
		return context.getAssets().open(assertName);
	}
}
