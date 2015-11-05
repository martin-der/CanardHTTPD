package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;

public class DirectoryWriter extends CollectionWriter<SharedDirectory> implements SharedThingWriter<SharedDirectory> {

	public DirectoryWriter(Context context) {
		super(context);
	}

	protected SharedCollection getSharedCollection() {
		return null;
	}



}
