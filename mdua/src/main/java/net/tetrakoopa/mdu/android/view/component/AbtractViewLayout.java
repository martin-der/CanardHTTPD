package net.tetrakoopa.mdu.android.view.component;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbtractViewLayout {
	
	protected final View viewLayout;
	protected final Activity activity;
	
	public AbtractViewLayout(Activity activity, int viewLayoutId) {

		this(activity,activity.findViewById(viewLayoutId));
	}
	public AbtractViewLayout(Activity activity, int layoutId, int layoutRootElementId) {
		
		this(activity,activity.getLayoutInflater().inflate(layoutId, (ViewGroup) activity.findViewById(layoutRootElementId)));
	}
	
	public AbtractViewLayout(Activity activity, View viewLayout) {
		this.activity = activity;
		
		this.viewLayout = viewLayout;
	}

	public void doPrepareLayoutAndStuff() {
		internalLayout(viewLayout);
	}
	/**
	 * used to set things up like this : <br/>TextView my_text = (TextView) layout.findViewById(R.id.my_text_id)
	 */
	protected abstract void internalLayout(View viewLayout);
	

}
