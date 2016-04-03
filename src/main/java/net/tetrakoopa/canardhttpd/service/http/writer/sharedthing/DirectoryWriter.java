package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;

import java.io.IOException;

public class DirectoryWriter extends CollectionWriter<SharedDirectory> implements SharedThingWriter<SharedDirectory> {

	public DirectoryWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

    @Override
    protected SharedThingTool[] getTools(SharedDirectory thing) throws IOException {
        return null;
    }
}
