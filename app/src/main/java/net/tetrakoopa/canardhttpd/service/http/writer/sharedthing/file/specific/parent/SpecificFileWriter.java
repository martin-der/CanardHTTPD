package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public abstract class SpecificFileWriter extends SpecificSerialisedWriter<SharedFile> {

	protected SpecificFileWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	protected InputStream getInputStream(Context context, SharedFile thing) throws IOException {
		return new FileInputStream(thing.getFile());
	}

}
