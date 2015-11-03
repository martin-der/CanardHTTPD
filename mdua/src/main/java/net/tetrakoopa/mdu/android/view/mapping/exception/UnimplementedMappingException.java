package net.tetrakoopa.mdu.android.view.mapping.exception;

public class UnimplementedMappingException extends InternalErrorMappingException {

	private static final long serialVersionUID = 8174316082045246788L;

	public UnimplementedMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public UnimplementedMappingException(String detailMessage) {
		super(detailMessage);
	}
	public UnimplementedMappingException(Throwable cause) {
		super(cause);
	}
	public UnimplementedMappingException() {
		super();
	}
}
