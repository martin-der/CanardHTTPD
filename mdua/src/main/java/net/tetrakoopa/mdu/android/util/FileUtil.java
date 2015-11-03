package net.tetrakoopa.mdu.android.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtil {

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
