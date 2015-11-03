package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;

public class DirectoryWriter extends CollectionWriter<SharedDirectory> implements SharedThingWriter<SharedDirectory> {

	protected SharedCollection getSharedCollection() {
		return null;
	}



}
