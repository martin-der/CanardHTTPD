package net.tetrakoopa.mdu.android.view.mapping.exception;

public class SecurityMappingException extends InternalErrorMappingException {

	private static final long serialVersionUID = -938131416452088844L;

	public SecurityMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public SecurityMappingException(String detailMessage) {
		super(detailMessage);
	}
	public SecurityMappingException(Throwable cause) {
		super(cause);
	}
	public SecurityMappingException() {
		super();
	}
}
