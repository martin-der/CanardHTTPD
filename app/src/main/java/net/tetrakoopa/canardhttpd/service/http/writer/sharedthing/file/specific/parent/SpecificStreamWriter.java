package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public abstract class SpecificStreamWriter extends SpecificSerialisedWriter<SharedStream> {

	protected SpecificStreamWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	protected InputStream getInputStream(Context context, SharedStream thing) throws IOException {
		return context.getContentResolver().openInputStream(thing.getUri());
	}

}
