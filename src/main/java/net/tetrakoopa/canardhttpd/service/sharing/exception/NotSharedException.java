package net.tetrakoopa.canardhttpd.service.sharing.exception;

public class NotSharedException extends SharingException {

	private static final long serialVersionUID = 8477749297357687756L;

	public NotSharedException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public NotSharedException(String detailMessage) {
		super(detailMessage);
	}
	public NotSharedException(Throwable cause) {
		super(cause);
	}
	public NotSharedException() {
		super();
	}
}
