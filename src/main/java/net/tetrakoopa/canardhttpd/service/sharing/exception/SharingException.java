package net.tetrakoopa.canardhttpd.service.sharing.exception;

public abstract class SharingException extends Exception {

	private static final long serialVersionUID = -1081875620451939830L;

	public SharingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public SharingException(String detailMessage) {
		super(detailMessage);
	}
	public SharingException(Throwable cause) {
		super(cause);
	}
	public SharingException() {
		super();
	}
}
