package net.tetrakoopa.mdu.android.view.mapping.exception.accessor;

import net.tetrakoopa.mdu.android.view.mapping.exception.MappingException;

public class IllegalVOMappingException extends MappingException {

	private static final long serialVersionUID = 3311620677940238723L;

	public IllegalVOMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public IllegalVOMappingException(String detailMessage) {
		super(detailMessage);
	}
	public IllegalVOMappingException(Throwable cause) {
		super(cause);
	}
	public IllegalVOMappingException() {
		super();
	}
}
