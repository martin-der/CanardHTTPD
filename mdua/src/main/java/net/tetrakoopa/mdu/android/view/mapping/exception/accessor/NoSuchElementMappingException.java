package net.tetrakoopa.mdu.android.view.mapping.exception.accessor;


public class NoSuchElementMappingException extends IllegalViewMappingException {

	private static final long serialVersionUID = 5517531199567996991L;

	public NoSuchElementMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public NoSuchElementMappingException(String detailMessage) {
		super(detailMessage);
	}
	public NoSuchElementMappingException(Throwable cause) {
		super(cause);
	}
	public NoSuchElementMappingException() {
		super();
	}
}
