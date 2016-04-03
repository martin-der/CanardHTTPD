package net.tetrakoopa.canardhttpd.domain.sharing;

import android.net.Uri;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedInode;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;

import java.io.File;
import java.util.List;


public class SharedDirectory extends SharedInode implements SharedCollection {

	private final boolean allowBrowsing;

	public SharedDirectory(Uri uri, String name, boolean allowBrowsing) {
		super(uri, name);
		this.allowBrowsing = allowBrowsing;
	}

	public boolean isAllowBrowsing() {
		return allowBrowsing;
	}

	@Override
	public String getType() {
		return "Directory";
	}

	@Override
	public List<SharedThing> getThings() {
		
		return null;
	}

}
