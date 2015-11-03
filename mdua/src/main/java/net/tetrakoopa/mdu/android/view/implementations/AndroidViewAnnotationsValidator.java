package net.tetrakoopa.mdu.android.view.implementations;

import net.tetrakoopa.mdu.android.view.mapping.ViewAnnotationsValidator;
import android.view.View;

public class AndroidViewAnnotationsValidator extends ViewAnnotationsValidator<View, View> {

	public AndroidViewAnnotationsValidator() {
		super(new AndroidViewAnnotationsViewAccessor());
	}

}