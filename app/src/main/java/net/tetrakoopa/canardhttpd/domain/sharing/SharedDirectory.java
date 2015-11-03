package net.tetrakoopa.canardhttpd.domain.sharing;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedInode;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;

import java.io.File;
import java.util.List;


public class SharedDirectory extends SharedInode implements SharedCollection {

	private final boolean allowBrowsing;

	public SharedDirectory(String name, File file, boolean allowBrowsing) {
		super(name, file);
		this.allowBrowsing = allowBrowsing;
	}

	public boolean isAllowBrowsing() {
		return allowBrowsing;
	}

	@Override
	public List<SharedThing> getThings() {
		
		return null;
	}

}
