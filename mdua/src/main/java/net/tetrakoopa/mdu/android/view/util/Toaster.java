package net.tetrakoopa.mdu.android.view.util;

import net.tetrakoopa.mdu.android.view.component.AbtractViewLayoutHolder;
import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

public abstract class Toaster<VIEW_OBJECT> extends AbtractViewLayoutHolder<VIEW_OBJECT> {
	
	public Toaster(Activity activity, int layoutId, int layoutRootElementId) {
		super (activity, layoutId, layoutRootElementId);
	}
		

	public void toast(VIEW_OBJECT viewObject) {
		if (viewObject != null) {
			updateViewContent(viewObject);
		}
		Toast toast = new Toast(activity.getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(viewLayout);
		toast.show();
	}
	public void toast() {
		toast(null);
	}
}
