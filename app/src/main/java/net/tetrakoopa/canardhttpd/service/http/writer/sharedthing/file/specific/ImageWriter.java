package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Map;

public class ImageWriter extends SpecificFileWriter {

	@Override
	public HandleAffinity affinityWith(String mimeType) {
		if (isMimeTypeOneOf(mimeType, "image/jpeg", "image/png", "image/gif")) {
			return HandleAffinity.MASTERIZE;
		}
		return null;
	}

	@Override
	public void writeThing(Writer writer, String uri, SharedFile sharedFile) throws IOException {
		writer.append("<img src=\"" + uri + "\" style=\"width:200px;\"/>");
	}

}
