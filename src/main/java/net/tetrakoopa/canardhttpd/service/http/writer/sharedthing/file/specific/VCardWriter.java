package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.AbstractSharedThingWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent.SpecificFileWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent.SpecificStreamWriter;

import java.io.IOException;
import java.io.Writer;

public class VCardWriter extends SpecificStreamWriter {

	public VCardWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	@Override
	public HandleAffinity affinityWith(String mimeType) {
		return null;
	}

	@Override
	public void writeThing(Writer writer, String uri, SharedStream thing) throws IOException {
		writer.append("This is a VCARD !");
	}

    @Override
    protected SharedThingTool[] getTools(SharedStream thing) throws IOException {
        return null;
    }

}
