package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.AbstractSharedThingWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.SharedThingWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.template.PageWriter;
import net.tetrakoopa.mdu.util.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public abstract class StreamWriter extends AbstractSharedThingWriter<SharedStream> implements SharedThingWriter<SharedStream> {

	protected StreamWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

}
