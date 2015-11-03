package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;

public class GroupWriter extends CollectionWriter<SharedGroup> implements SharedThingWriter<SharedGroup> {

	protected SharedCollection getSharedCollection() {
		return null;
	}


}
