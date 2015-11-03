package net.tetrakoopa.mdu.android.view.mapping;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.util.ReflexionUtil;
import net.tetrakoopa.mdu.android.view.mapping.annotation.UIElement;
import net.tetrakoopa.mdu.android.view.mapping.annotation.validation.NotEmpty;
import net.tetrakoopa.mdu.android.view.mapping.annotation.validation.NotNull;
import net.tetrakoopa.mdu.android.view.mapping.annotation.validation.Regex;
import net.tetrakoopa.mdu.android.view.mapping.exception.accessor.IllegalViewMappingException;
import net.tetrakoopa.mdu.android.view.mapping.exception.validator.AccessorFailureValidatorException;

public class ViewAnnotationsValidator<VIEW, VIEW_ELEMENT> extends AbstractScanner {

	private final ViewAnnotationsViewAccessor<VIEW, VIEW_ELEMENT> viewAccessor;

	public ViewAnnotationsValidator(ViewAnnotationsViewAccessor<VIEW, VIEW_ELEMENT> viewAccessor) {
		this.viewAccessor = viewAccessor;
	}

	public final <VO> boolean validate(VIEW view, VO vo, ValidationStatus status) {

		if (vo == null) {
			return true;
		}

		@SuppressWarnings("unchecked")
		Class<VO> voClass = (Class<VO>) vo.getClass();

		try {
			return validate(view, voClass, status);
		} catch (AccessorFailureValidatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	protected <VO> boolean validate(VIEW view, Class<VO> voClass, ValidationStatus status) throws AccessorFailureValidatorException {

		for (Field field : getViewMappedFields(voClass)) {
			UIElement uiElement = field.getAnnotation(UIElement.class);

			if (uiElement == null)
				continue;

			final Class<?> type = ReflexionUtil.inboxIfPrimitive(field.getType());

			final Object value;

			try {
				final VIEW_ELEMENT element = viewAccessor.findElement(view, uiElement);
				value = viewAccessor.getViewElementValue(field, element, type);
			} catch (IllegalViewMappingException aex) {
				throw new AccessorFailureValidatorException(aex);
			}

			if (value == null) {
				NotNull notNullValidation = field.getAnnotation(NotNull.class);
				if (notNullValidation != null && value == null) {
					status.addError(voClass, field, uiElement, notNullValidation);
				}
				// if value is null then there is no need to check any more validation rule
				continue;
			}

			NotEmpty notEmptyValidation = field.getAnnotation(NotEmpty.class);
			if (notEmptyValidation != null) {
				if (type.equals(java.lang.String.class) && "".equals(value))
					status.addError(voClass, field, uiElement, notEmptyValidation);
				else if (type.isAssignableFrom(java.lang.Number.class) && ((java.lang.Number) value).longValue() == 0)
					status.addError(voClass, field, uiElement, notEmptyValidation);
			}

			Regex regexValidation = field.getAnnotation(Regex.class);
			if (regexValidation != null) {
				if (type.equals(java.lang.String.class)) {
					if (!((String) value).matches(regexValidation.value())) {
						status.addError(voClass, field, uiElement, regexValidation);
					}
				}
			}

		}

		return status.isOk();
	}


}