package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;

import java.io.IOException;

public class GroupWriter extends CollectionWriter<SharedGroup> implements SharedThingWriter<SharedGroup> {

	public GroupWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

    @Override
    protected SharedThingTool[] getTools(SharedGroup thing) throws IOException {
        return null;
    }

}
