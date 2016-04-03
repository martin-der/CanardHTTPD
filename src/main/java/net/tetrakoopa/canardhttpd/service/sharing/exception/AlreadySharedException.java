package net.tetrakoopa.canardhttpd.service.sharing.exception;

public class AlreadySharedException extends SharingException {

	private static final long serialVersionUID = -7570443778329975251L;

	public AlreadySharedException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public AlreadySharedException(String detailMessage) {
		super(detailMessage);
	}
	public AlreadySharedException(Throwable cause) {
		super(cause);
	}
	public AlreadySharedException() {
		super();
	}
}
