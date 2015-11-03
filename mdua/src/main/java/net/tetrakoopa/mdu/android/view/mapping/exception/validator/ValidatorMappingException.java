package net.tetrakoopa.mdu.android.view.mapping.exception.validator;

import net.tetrakoopa.mdu.android.view.mapping.exception.MappingException;

public abstract class ValidatorMappingException extends MappingException {

	private static final long serialVersionUID = -1527004254560522985L;

	public ValidatorMappingException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}
	public ValidatorMappingException(String detailMessage) {
		super(detailMessage);
	}
	public ValidatorMappingException(Throwable cause) {
		super(cause);
	}
	public ValidatorMappingException() {
		super();
	}
}
