package net.tetrakoopa.mdu.android.view.mapping.exception.accessor;


public class IllegalViewMappingException extends AccessorMappingException {

	private static final long serialVersionUID = 3311620677940238723L;

	public IllegalViewMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public IllegalViewMappingException(String detailMessage) {
		super(detailMessage);
	}
	public IllegalViewMappingException(Throwable cause) {
		super(cause);
	}
	public IllegalViewMappingException() {
		super();
	}
}
