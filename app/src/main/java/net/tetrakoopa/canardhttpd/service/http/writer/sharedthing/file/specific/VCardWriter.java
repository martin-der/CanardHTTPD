package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Map;

public class VCardWriter extends SpecificFileWriter {

	public VCardWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	@Override
	public HandleAffinity affinityWith(String mimeType) {
		return null;
	}

	@Override
	public void writeThing(Writer writer, String uri, SharedFile thing) throws IOException {
		writer.append("This is a VCARD !");
	}

}
