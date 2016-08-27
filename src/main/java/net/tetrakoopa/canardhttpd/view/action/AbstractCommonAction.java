package net.tetrakoopa.canardhttpd.view.action;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;
import net.tetrakoopa.canardhttpd.CanardHTTPDService;
import net.tetrakoopa.canardhttpd.service.Formater;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.mdua.util.ResourcesUtil;
import net.tetrakoopa.mdua.view.component.AbtractViewLayout;

public abstract class AbstractCommonAction extends AbtractViewLayout {

	private final CanardHTTPDActivity canardHTTPDActivity;

	protected final Formater formater;

	public AbstractCommonAction(CanardHTTPDActivity activity, Bundle savedInstanceState, View viewLayout) {
		super(activity, savedInstanceState, viewLayout);
		canardHTTPDActivity = activity;
		formater = new Formater(activity);
	}


	protected CanardHTTPDActivity activity() {
		return canardHTTPDActivity;
	}
	protected Intent activityIntent() {
		return canardHTTPDActivity.getIntent();
	}

	protected CanardHTTPDService service() {
		return canardHTTPDActivity.getService();
	}

	protected SharesManager sharesManager() {
		return canardHTTPDActivity.getFilesManager();
	}

	protected String message(int id) {
		return ResourcesUtil.getString(canardHTTPDActivity, id);
	}
	protected String messagef(int id, Object ...objects) {
		return ResourcesUtil.getStringf(canardHTTPDActivity, id, objects);
	}

}
