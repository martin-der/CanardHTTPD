package net.tetrakoopa.canardhttpd.service.sharing.exception;

public class BadShareTypeException extends SharingException {

	private static final long serialVersionUID = -4789293870171690400L;

	public BadShareTypeException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public BadShareTypeException(String detailMessage) {
		super(detailMessage);
	}
	public BadShareTypeException(Throwable cause) {
		super(cause);
	}
	public BadShareTypeException() {
		super();
	}
}
