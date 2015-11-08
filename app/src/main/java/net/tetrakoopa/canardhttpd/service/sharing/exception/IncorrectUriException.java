package net.tetrakoopa.canardhttpd.service.sharing.exception;

public class IncorrectUriException extends Exception {

	private static final long serialVersionUID = -7439206553848988782L;

	public IncorrectUriException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public IncorrectUriException(String detailMessage) {
		super(detailMessage);
	}
	public IncorrectUriException(Throwable cause) {
		super(cause);
	}
	public IncorrectUriException() {
		super();
	}
}
