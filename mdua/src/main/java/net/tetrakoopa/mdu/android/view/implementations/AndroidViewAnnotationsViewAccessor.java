package net.tetrakoopa.mdu.android.view.implementations;

import net.tetrakoopa.mdu.android.view.mapping.AbstractViewAnnotationsViewAccessor;
import net.tetrakoopa.mdu.android.view.mapping.ViewAnnotationsViewAccessor;
import net.tetrakoopa.mdu.android.view.mapping.annotation.UIElement;
import net.tetrakoopa.mdu.android.view.mapping.exception.accessor.IllegalViewMappingException;
import android.view.View;
import android.widget.TextView;

public class AndroidViewAnnotationsViewAccessor extends AbstractViewAnnotationsViewAccessor<View, View> implements ViewAnnotationsViewAccessor<View, View> {

	@Override
	public View findElement(View view, UIElement uiElement) throws IllegalViewMappingException {
		View element = view.findViewById(uiElement.targetId());
		if (element == null)
			throw new IllegalViewMappingException("No element with id '" + uiElement.targetId() + "(internal value) was found in the view");
		return element;
	}


	@Override
	public <TYPE> TYPE getViewElementDirectValue(View element, Class<TYPE> type) throws IllegalViewMappingException {

		if (type.equals(java.lang.String.class)) {

			if (element instanceof TextView) {
				return (TYPE) ((TextView) element).getText().toString();
			} else {
				throw createCannotGetThisException("String", element);
			}

		} else if (type.equals(java.lang.Integer.class)) {

			final String strValue;
			if (element instanceof TextView) {
				strValue = ((TextView) element).getText().toString();
			} else {
				throw createCannotGetThisException("String (before Integer conversion)", element);
			}

			return strValue.equals("") ? null : (TYPE) new Integer(strValue);

		} else if (type.equals(java.lang.Boolean.class)) {

			throw createCannotGetThisException(type.getName(), element);
		} else {

			throw createCannotGetThisException(type.getName(), element);
		}
	}

	@Override
	protected <TYPE> void setViewElementDirectValue(View element, Class<TYPE> type, TYPE value) throws IllegalViewMappingException {
		if (type.equals(java.lang.String.class)) {

			if (element instanceof TextView) {
				((TextView) element).setText(safeString((String) value));
			} else {
				throw createCannotSetThisException("String", element);
			}

		} else if (type.isAssignableFrom(java.lang.Number.class)) {

			if (element instanceof TextView) {
				((TextView) element).setText(safeNumberToString((Integer) value));
			} else {
				throw createCannotSetThisException("String (before Integer conversion)", element);
			}

		} else if (type.equals(java.lang.Boolean.class)) {

			throw createCannotSetThisException(type.getName(), element);
		} else {

			throw createCannotSetThisException(type.getName(), element);
		}
	}


	private String safeString(String s) {
		return s == null ? "" : s;
	}

	private String safeNumberToString(Number n) {
		return n == null ? "" : n.toString();
	}



	private IllegalViewMappingException createCannotGetThisException(String targetType, View element) {
		IllegalViewMappingException exception = new IllegalViewMappingException("Don't know how to get " + targetType + " from " + element.getClass().getName());
		return exception;
	}

	private IllegalViewMappingException createCannotSetThisException(String targetType, View element) {
		IllegalViewMappingException exception = new IllegalViewMappingException("Don't know how to set " + targetType + " into " + element.getClass().getName());
		return exception;
	}

}
