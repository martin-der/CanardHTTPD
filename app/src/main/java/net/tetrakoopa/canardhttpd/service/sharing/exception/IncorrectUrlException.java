package net.tetrakoopa.canardhttpd.service.sharing.exception;

public class IncorrectUrlException extends Exception {

	private static final long serialVersionUID = -7439206553848988782L;

	public IncorrectUrlException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public IncorrectUrlException(String detailMessage) {
		super(detailMessage);
	}
	public IncorrectUrlException(Throwable cause) {
		super(cause);
	}
	public IncorrectUrlException() {
		super();
	}
}
