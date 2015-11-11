package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;

import java.io.IOException;
import java.io.Writer;


public final class GenericStreamWriter extends StreamWriter {

	public GenericStreamWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	@Override
	protected void writeThing(Writer writer, String uri, SharedStream thing) throws IOException {
		// TODO Auto-generated method stub
		writer.append("Some unknown content");
	}
	
}
