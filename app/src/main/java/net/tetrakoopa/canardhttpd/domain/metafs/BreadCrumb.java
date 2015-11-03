package net.tetrakoopa.canardhttpd.domain.metafs;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;

import java.util.ArrayList;
import java.util.List;


public class BreadCrumb {

	public static class Part {
		
		private final SharedThing thing;
		
		public Part(SharedThing sharedThing) {
			if (sharedThing == null)
				throw new IllegalArgumentException("BreadCrumb.Part built with null 'sharedThing'");
			this.thing = sharedThing;
		}

		public SharedThing getThing() {
			return thing;
		}
		
		public String getLabel() {
			return thing.getName();
		}
		
		public boolean isCollection() {
			return thing instanceof SharedCollection;
		}

	}
	
	private final List<Part> parts = new ArrayList<Part>();

	public List<Part> getParts() {
		return parts;
	}
	
	public String asPath() {
		if (parts.isEmpty()) {
			return "/";
		}
		final StringBuffer path = new StringBuffer();
		for (BreadCrumb.Part part : parts) {
			path.append('/');
			path.append(part.getLabel());
		}
		return path.toString();
	}
	@Override
	public String toString() {
		return "/"+asPath();
	}
}

