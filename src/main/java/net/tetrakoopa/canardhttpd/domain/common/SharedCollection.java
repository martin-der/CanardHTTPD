package net.tetrakoopa.canardhttpd.domain.common;

import java.util.List;


public interface SharedCollection extends SharedThing {
	
	List<SharedThing> getThings();

}
