package net.tetrakoopa.mdu.android.view.component;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbtractViewLayoutHolder<VIEW_OBJECT> {
	
	protected final View viewLayout;
	protected final Activity activity;
	
	public AbtractViewLayoutHolder(Activity activity, int layoutId, int layoutRootElementId) {
		
		this.activity = activity;
		
		viewLayout = activity.getLayoutInflater().inflate(layoutId, (ViewGroup) activity.findViewById(layoutRootElementId));

		internalLookup(viewLayout);
	}

	/**
	 * used to set things up like this : <br/>TextView my_text = (TextView) layout.findViewById(R.id.my_text_id)
	 */
	protected abstract void internalLookup(View viewLayout);

	/**
	 * used to set things up like this : <br/><br/>my_text.setText(viewObject.getSomeText());
	 */
	public abstract void updateViewContent (VIEW_OBJECT viewObject) ;
		

}
