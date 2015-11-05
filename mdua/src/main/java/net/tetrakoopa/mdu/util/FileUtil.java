package net.tetrakoopa.mdu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

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

}
