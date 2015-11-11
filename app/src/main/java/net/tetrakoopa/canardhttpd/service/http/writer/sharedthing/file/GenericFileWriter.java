package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.AbstractSharedThingWriter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Map;


public final class GenericFileWriter extends FileWriter {

	public GenericFileWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	@Override
	protected void writeThing(Writer writer, String uri, SharedFile sharedFile) throws IOException {
		// TODO Auto-generated method stub
		writer.append("Some unknown file");
	}

    @Override
    protected SharedThingTool[] getTools(SharedFile thing) throws IOException {
        return null;
    }

}
