package net.tetrakoopa.mdu.android.view.mapping.exception;

public class BadValueException extends MappingException {

	private static final long serialVersionUID = 3311620677940238723L;

	public BadValueException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public BadValueException(String detailMessage) {
		super(detailMessage);
	}
	public BadValueException(Throwable cause) {
		super(cause);
	}
	public BadValueException() {
		super();
	}
}
