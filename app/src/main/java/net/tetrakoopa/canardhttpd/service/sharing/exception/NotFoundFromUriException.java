package net.tetrakoopa.canardhttpd.service.sharing.exception;

import android.net.Uri;

public class NotFoundFromUriException extends Exception {

	public NotFoundFromUriException(Uri uri, String expectedType) {
		super("Could not find element of type '"+expectedType+"' from uri '"+uri+"'");
	}
}
