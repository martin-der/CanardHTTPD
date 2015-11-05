package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;

public class GroupWriter extends CollectionWriter<SharedGroup> implements SharedThingWriter<SharedGroup> {

	public GroupWriter(Context context) {
		super(context);
	}

	protected SharedCollection getSharedCollection() {
		return null;
	}


}
