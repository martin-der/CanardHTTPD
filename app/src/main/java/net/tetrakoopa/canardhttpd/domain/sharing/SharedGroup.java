package net.tetrakoopa.canardhttpd.domain.sharing;

import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;
import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;

import java.util.ArrayList;
import java.util.List;

public class SharedGroup extends CommonSharedThing implements SharedCollection {

	public SharedGroup(String name) {
		super(name);
	}

	private final List<SharedThing> sharedThings = new ArrayList<SharedThing>();

	public List<SharedThing> getThings() {
		return sharedThings;
	}
	
}
