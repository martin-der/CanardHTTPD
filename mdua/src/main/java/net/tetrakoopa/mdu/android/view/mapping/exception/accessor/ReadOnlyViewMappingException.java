package net.tetrakoopa.mdu.android.view.mapping.exception.accessor;


public class ReadOnlyViewMappingException extends AccessorMappingException {

	private static final long serialVersionUID = -1090858079831895317L;

	public ReadOnlyViewMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public ReadOnlyViewMappingException(String detailMessage) {
		super(detailMessage);
	}
	public ReadOnlyViewMappingException(Throwable cause) {
		super(cause);
	}
	public ReadOnlyViewMappingException() {
		super();
	}
}
