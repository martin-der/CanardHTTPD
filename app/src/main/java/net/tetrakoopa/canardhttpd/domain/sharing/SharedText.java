package net.tetrakoopa.canardhttpd.domain.sharing;

import android.net.Uri;

import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;



public class SharedText extends CommonSharedThing {

	private final String text;
	
	public SharedText(String name, String text) {
		super(Uri.EMPTY, name);
		this.text = text;
	}

	@Override
	public String getType() {
		return "Text";
	}

	public String getText() {
		return text;
	}

}
