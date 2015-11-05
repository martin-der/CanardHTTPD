package net.tetrakoopa.mdu.android.view.component;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbtractViewLayout {
	
	protected final View viewLayout;
	protected final Activity activity;
	protected final Bundle savedInstanceState;


	public AbtractViewLayout(Activity activity, Bundle savedInstanceState, int viewLayoutId) {

		this(activity,savedInstanceState,activity.findViewById(viewLayoutId));
	}
	public AbtractViewLayout(Activity activity, Bundle savedInstanceState, int layoutId, int layoutRootElementId) {
		
		this(activity,savedInstanceState,activity.getLayoutInflater().inflate(layoutId, (ViewGroup) activity.findViewById(layoutRootElementId)));
	}
	
	public AbtractViewLayout(Activity activity, Bundle savedInstanceState, View viewLayout) {
		this.activity = activity;
		this.savedInstanceState = savedInstanceState;
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
