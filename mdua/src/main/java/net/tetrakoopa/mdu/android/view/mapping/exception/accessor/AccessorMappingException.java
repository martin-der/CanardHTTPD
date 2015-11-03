package net.tetrakoopa.mdu.android.view.mapping.exception.accessor;

import net.tetrakoopa.mdu.android.view.mapping.exception.MappingException;

public abstract class AccessorMappingException extends MappingException {

	private static final long serialVersionUID = 2104065296211756588L;

	public AccessorMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public AccessorMappingException(String detailMessage) {
		super(detailMessage);
	}
	public AccessorMappingException(Throwable cause) {
		super(cause);
	}
	public AccessorMappingException() {
		super();
	}
}
