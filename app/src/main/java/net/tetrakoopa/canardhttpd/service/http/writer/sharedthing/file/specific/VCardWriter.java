package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

public class VCardWriter extends SpecificFileWriter {

	@Override
	public HandleAffinity affinityWith(String mimeType) {
		if (isMimeTypeOneOf(mimeType, "image/jpeg", "image/png", "image/gif")) {
			return HandleAffinity.MASTERIZE;
		}
		return null;
	}

	@Override
	public void writeThing(PrintStream stream, String uri, SharedFile thing) throws IOException {
		stream.write("This is a VCARD !".getBytes());
	}

}
