package net.tetrakoopa.mdu.android.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtil {
	

	public static String getIPAddress(boolean useIPV4) throws SocketException {
		for (NetworkInterface interfaace : Collections.list(NetworkInterface.getNetworkInterfaces())) {
			for (InetAddress address : Collections.list(interfaace.getInetAddresses())) {
				if (address.isLoopbackAddress())
					continue;
				String addressStr = address.getHostAddress().toString();
				if (useIPV4) {
					if (isIPV4(address)) {
						return addressStr;
					}
				} else {
					if (!isIPV4(address)) {
						return addressStr;
					}
				}
			}
		}
		return null;
	}

	private static boolean isIPV4(InetAddress address) {
		return !address.isLoopbackAddress() && (address instanceof Inet4Address);
	}

	public static String excuteHttpRequest(String targetURL, Map<String, Object> parameters) {
		URL url;
		HttpURLConnection connection = null;

		try {

			final String urlParameters = parameters != null ? createHttpParameter(parameters) : "";

			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static String createHttpParameter(Map<String, Object> parameters) {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			if (first)
				first=false;
			else
				buffer.append('&');
			try {
				buffer.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue() != null ? entry.getValue().toString() : null, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return buffer.toString();
	}

	public static abstract class ExternalIPProvider {
		public abstract String getIP();
	}

	public static abstract class HTTPExternalIPProvider extends ExternalIPProvider {

		private final String uri;

		public HTTPExternalIPProvider(String uri) {
			this.uri = uri;
		}

		protected abstract String extractIP(String response);

		public final String getIP() {
			return extractIP(excuteHttpRequest(uri, null));
		}
	}

	private static class DyndnsExternalIPProvider extends HTTPExternalIPProvider {

		public DyndnsExternalIPProvider() {
			super("checkip.dyndns.org");
		}

		@Override
		public String extractIP(String response) {
			Pattern p = Pattern.compile("<body>Current IP Address: (.*)</body>");
			Matcher m = p.matcher(response);
			if (m.find())
				return m.group(1);
			return null;
		}

	}

	private static class ICanHazIPIPProviver extends HTTPExternalIPProvider {
		public ICanHazIPIPProviver() {
			super("icanhazip.com");
		}

		@Override
		protected String extractIP(String response) {
			return response;
		}
	}

	private static class ExternalIPDotNetIPProviver extends HTTPExternalIPProvider {
		public ExternalIPDotNetIPProviver() {
			super("api.externalip.net/ip");
		}

		@Override
		protected String extractIP(String response) {
			return response;
		}
	}

	public static String getIPAddressFromExternalProvider() {

		ExternalIPProvider providers[] = { new DyndnsExternalIPProvider(), new ICanHazIPIPProviver(), new ExternalIPDotNetIPProviver() };
		for ( ExternalIPProvider provider : providers ) {
			String response = provider.getIP();
			if (response != null)
				return response;
		}
		return null;
	}
}
