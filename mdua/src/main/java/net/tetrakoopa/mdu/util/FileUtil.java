package net.tetrakoopa.mdu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FileUtil {

	public static String getAsString(Reader reader) throws IOException {
		final char buffer[] = new char[200];
		int l;
		final StringBuffer stringBufferSource = new StringBuffer();
		while ((l=reader.read(buffer))>0) { stringBufferSource.append(buffer, 0, l); }
		return stringBufferSource.toString();
	}

	public static void copy(InputStream input, OutputStream output) throws IOException {
		byte buffer[] = new byte[200];

		int l;
		while ((l = input.read(buffer)) > 0) {
			output.write(buffer, 0, l);
		}
	}
	public static CharSequence readCharSequence(InputStream inputStream) throws IOException {
		final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');
			return buffer;
		} finally {
			in.close();
		}
	}

	/**
	 * Equivalent to <code>fileSizeAsString(size, "%1.3f %2so.")</code>
	 */
	public static String fileSizeAsString(long size) {
		return fileSizeAsString(size, "%1.3f %2so.");
	}

	/**
	 * @param format format used to display result, accept two place holders
	 *            <table>
	 *                <tr><th>Place Holder</th><th>Type</th><th>example</th></tr>
	 *                <tr><td>size</td><td>Float or Double</td><td><code>%1f</code></td></tr>
	 *                <tr><td>dimension</td><td>String</td><td><code>%2s</code></td></tr>
	 *            </table>
	 */
	public static String fileSizeAsString(long size, String format) {
		final double bytes = size;
		final double kilobytes = (bytes / 1024);
		if (kilobytes <= 0)
			return String.format(format, bytes, "");
		final double megabytes = (kilobytes / 1024);
		if (megabytes <= 0)
			return String.format(format, kilobytes, "k");
		final double gigabytes = (megabytes / 1024);
		if (gigabytes <= 0)
			return String.format(format, megabytes, "m");
		final double terabytes = (gigabytes / 1024);
		if (terabytes <= 0)
			return String.format(format, gigabytes, "g");
		final double petabytes = (terabytes / 1024);
		if (petabytes <= 0)
			return String.format(format, terabytes, "t");
		final double exabytes = (petabytes / 1024);
		if (exabytes <= 0)
			return String.format(format, petabytes, "p");
		final double zettabytes = (exabytes / 1024);
		if (zettabytes <= 0)
			return String.format(format, exabytes, "e");
		final double yottabytes = (zettabytes / 1024);
		if (yottabytes <= 0)
			return String.format(format, zettabytes, "z");
		return String.format(format, yottabytes, "y");

	}

}
