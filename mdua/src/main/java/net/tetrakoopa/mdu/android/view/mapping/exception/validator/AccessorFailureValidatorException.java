package net.tetrakoopa.mdu.android.view.mapping.exception.validator;

import net.tetrakoopa.mdu.android.view.mapping.exception.accessor.AccessorMappingException;

public class AccessorFailureValidatorException extends ValidatorMappingException {

	private static final long serialVersionUID = -4306903968844385362L;

	public AccessorFailureValidatorException(String detailMessage, AccessorMappingException cause) {
		super(detailMessage, cause);
	}

	public AccessorFailureValidatorException(AccessorMappingException cause) {
		super(cause);
	}

	public AccessorMappingException getAccessorException() {
		return (AccessorMappingException) getCause();
	}

}
