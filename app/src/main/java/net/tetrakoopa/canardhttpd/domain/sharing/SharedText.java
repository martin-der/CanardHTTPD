package net.tetrakoopa.canardhttpd.domain.sharing;

import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;



public class SharedText extends CommonSharedThing {

	private final String text;
	
	public SharedText(String name, String text) {
		super(name);
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
