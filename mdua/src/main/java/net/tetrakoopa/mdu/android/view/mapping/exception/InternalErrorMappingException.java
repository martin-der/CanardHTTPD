package net.tetrakoopa.mdu.android.view.mapping.exception;

public abstract class InternalErrorMappingException extends RuntimeException {

	private static final long serialVersionUID = -9088516756955531204L;

	public InternalErrorMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public InternalErrorMappingException(String detailMessage) {
		super(detailMessage);
	}
	public InternalErrorMappingException(Throwable cause) {
		super(cause);
	}
	public InternalErrorMappingException() {
		super();
	}
}
