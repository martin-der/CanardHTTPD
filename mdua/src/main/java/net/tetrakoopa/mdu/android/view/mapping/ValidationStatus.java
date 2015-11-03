package net.tetrakoopa.mdu.android.view.mapping;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.tetrakoopa.mdu.android.view.mapping.annotation.UIElement;

public class ValidationStatus {
	
	public class Error {
		public final Class<?> voClass;
		public final Field field;
		public final UIElement uiElement;
		public final Object failedRule;

		public Error(Class<?> voClass, Field field, UIElement uiElement, Object failedRule) {
			this.voClass = voClass;
			this.field = field;
			this.uiElement = uiElement;
			this.failedRule = failedRule;
		}
	}

	private final AbstractScanner scanner;

	private final List<Error> errors = new ArrayList<Error>();

	public ValidationStatus(AbstractScanner scanner) {
		this.scanner = scanner;
	}

	public boolean isOk() {
		return errors.isEmpty();
	}

	public void addError(Class<?> voClass, Field field, UIElement uiElement, Object failedRule) {
		Error error = new Error(voClass, field, uiElement, failedRule);
		errors.add(error);
	}

	public List<Error> getErrors() {
		return errors;
	}

	public AbstractScanner getScanner() {
		return scanner;
	}
}
