package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;
import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.AbstractSharedThingWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent.SpecificStreamWriter;

import java.io.IOException;
import java.io.Writer;

public class ImageWriter extends SpecificStreamWriter {

	public ImageWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	@Override
	public HandleAffinity affinityWith(String mimeType) {
		if (isMimeTypeOneOf(mimeType, "image/jpeg", "image/gif", "image/png")) {
			return HandleAffinity.MASTERIZES;
		};
		return null;
	}

	@Override
	protected void writeThing(Writer writer, String uri, SharedStream thing) throws IOException {
		writer.append("<img src=\""+httpContext+"/~/_/" + CommonHTMLComponent.escapeToUrl(thing.getUri().toString()) + "\" style=\"width:200px;\"/>");
	}

    @Override
    protected SharedThingTool[] getTools(SharedStream thing) throws IOException {
        return null;
    }

}
