package net.tetrakoopa.mdu.android.view.implementations;

import net.tetrakoopa.mdu.android.view.mapping.ViewAnnotationsCopyProcessor;
import android.view.View;

public class AndroidViewAnnotationsCopyProcessor extends ViewAnnotationsCopyProcessor<View, View> {

	public AndroidViewAnnotationsCopyProcessor() {
		super(new AndroidViewAnnotationsViewAccessor());
	}


}
