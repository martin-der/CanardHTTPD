package net.tetrakoopa.mdu.android.view.mapping.exception;

public abstract class MappingException extends Exception {

	private static final long serialVersionUID = 3311620677940238723L;

	public MappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public MappingException(String detailMessage) {
		super(detailMessage);
	}
	public MappingException(Throwable cause) {
		super(cause);
	}
	public MappingException() {
		super();
	}
}
