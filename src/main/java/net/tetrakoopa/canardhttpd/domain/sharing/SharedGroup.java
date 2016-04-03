package net.tetrakoopa.canardhttpd.domain.sharing;

import android.net.Uri;

import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;
import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;

import java.util.ArrayList;
import java.util.List;

public class SharedGroup extends CommonSharedThing implements SharedCollection {

	public SharedGroup(Uri uri, String name) {
		super(uri, name);
	}

	private final List<SharedThing> sharedThings = new ArrayList<SharedThing>();

	@Override
	public String getType() {
		return "Group";
	}

	public List<SharedThing> getThings() {
		return sharedThings;
	}
	
}
