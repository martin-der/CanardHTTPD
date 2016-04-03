package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedContact;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.AbstractSharedThingWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent.SpecificFileWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent.SpecificStreamWriter;

import java.io.IOException;
import java.io.Writer;

public class ContactWriter extends SpecificStreamWriter {

	public ContactWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	@Override
	public HandleAffinity affinityWith(String mimeType) {
		if (mimeType.equals(SharedContact.ANDROID_MIME_TYPE)) {
			return HandleAffinity.HANDLES_WELL;
		}
		return null;
	}

	@Override
	public void writeThing(Writer writer, String uri, SharedStream thing) throws IOException {
		writer.append("This is a Contact !");
	}

    @Override
    protected SharedThingTool[] getTools(SharedStream thing) throws IOException {
        return null;
    }

}
