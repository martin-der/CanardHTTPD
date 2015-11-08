package net.tetrakoopa.canardhttpd.domain.sharing;

import android.net.Uri;

import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;
import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedInode;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;

import java.io.File;
import java.util.List;


public class SharedContact extends CommonSharedThing {

	private final String id;

	public SharedContact(Uri uri, String name, String id) {
		super(uri, name);
		this.id = id;
	}

	@Override
	public String getType() {
		return "Contact";
	}

	public String getId() {
		return id;
	}


}
