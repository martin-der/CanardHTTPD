package net.tetrakoopa.mdu.android.view.mapping;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

import net.tetrakoopa.mdu.android.view.mapping.annotation.copy.UINumeric;
import net.tetrakoopa.mdu.android.view.mapping.annotation.copy.UIText;
import net.tetrakoopa.mdu.android.view.mapping.exception.UnimplementedMappingException;
import net.tetrakoopa.mdu.android.view.mapping.exception.accessor.IllegalViewMappingException;

public abstract class AbstractViewAnnotationsViewAccessor<VIEW, VIEW_ELEMENT> implements ViewAnnotationsViewAccessor<VIEW, VIEW_ELEMENT> {

	protected abstract <TYPE> TYPE getViewElementDirectValue(VIEW_ELEMENT element, Class<TYPE> type) throws IllegalViewMappingException;

	protected abstract <TYPE> void setViewElementDirectValue(VIEW_ELEMENT element, Class<TYPE> type, TYPE value) throws IllegalViewMappingException;

	@Override
	public final <TYPE> TYPE getViewElementValue(Field field, VIEW_ELEMENT element, Class<TYPE> type) throws IllegalViewMappingException {

		TYPE value = getViewElementDirectValue(element, type);

		if (type.equals(java.lang.String.class)) {

			value = (TYPE) applyStringValueChanges(field, (String) value);

		} else if (type.equals(java.lang.Integer.class)) {

			value = (TYPE) applyNumericValueChanges(field, (Class<Number>) type, (Number) value);

		}

		return value;
	}

	@Override
	public <TYPE> void setViewElementValue(Field field, VIEW_ELEMENT element, Class<TYPE> type, TYPE value) throws IllegalViewMappingException {

		if (type.equals(java.lang.String.class)) {

			value = (TYPE) applyStringValueChanges(field, (String) value);

		} else if (type.equals(java.lang.Integer.class)) {

			value = (TYPE) applyNumericValueChanges(field, (Class<Number>) type, (Number) value);

		}

		setViewElementDirectValue(element, type, value);

	}

	protected String applyStringValueChanges(Field field, String value) {
		UIText uiTextConversion = field.getAnnotation(UIText.class);
		final boolean mapEmptyToNull = uiTextConversion != null && uiTextConversion.mapEmptyToNull();
		if ("".equals(value) && mapEmptyToNull)
			value = null;
		return value;
	}

	@SuppressWarnings("unchecked")
	protected <TYPE extends Number> TYPE applyNumericValueChanges(Field field, Class<TYPE> type, TYPE value) {
		UINumeric uiNumeric = field.getAnnotation(UINumeric.class);

		if (value == null && uiNumeric != null) {
			long defaultLongValue = uiNumeric.defaultValue();

			if (type.equals(java.lang.Byte.class))
				value = (TYPE) Byte.valueOf((byte) defaultLongValue);
			else if (type.equals(java.lang.Double.class))
				value = (TYPE) Double.valueOf(defaultLongValue);
			else if (type.equals(java.lang.Short.class))
				value = (TYPE) Short.valueOf((short) defaultLongValue);

			else if (type.equals(java.lang.Short.class))
				value = (TYPE) Short.valueOf((short) defaultLongValue);
			else if (type.equals(java.lang.Integer.class))
				value = (TYPE) Integer.valueOf((int) defaultLongValue);
			else if (type.equals(java.lang.Long.class))
				value = (TYPE) Long.valueOf(defaultLongValue);

			else if (type.equals(java.math.BigDecimal.class))
				value = (TYPE) BigDecimal.valueOf(defaultLongValue);
			else if (type.equals(java.math.BigInteger.class))
				value = (TYPE) BigInteger.valueOf(defaultLongValue);

			else
				throw new UnimplementedMappingException("Don't know how to instanciate numeric : " + type.getClass().getName());

		}
		return value;
	}

}
