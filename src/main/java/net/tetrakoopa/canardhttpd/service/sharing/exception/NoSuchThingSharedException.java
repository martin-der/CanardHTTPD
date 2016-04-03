package net.tetrakoopa.canardhttpd.service.sharing.exception;

public class NoSuchThingSharedException extends SharingException {

	public NoSuchThingSharedException(String requestUrl) {
		super(requestUrl);
	}
}
