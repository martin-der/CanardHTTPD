package net.tetrakoopa.mdu.android.view.util;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.content.Context;

public class DevUtil {

	public static class PromptThenDelegateUncaughtExceptionHandler implements UncaughtExceptionHandler {

		private final Activity activity;
		private final UncaughtExceptionHandler delegatedHandler;

		public PromptThenDelegateUncaughtExceptionHandler(Activity activity, UncaughtExceptionHandler delegatedHandler) {
			this.activity = activity;
			this.delegatedHandler = delegatedHandler;
		}

		@Override
		public void uncaughtException(Thread arg0, Throwable arg1) {
			// TODO Auto-generated method stub

		}

	}

	public static void addDevHandler(Activity activity) {
		Thread.currentThread().setUncaughtExceptionHandler(new PromptThenDelegateUncaughtExceptionHandler(activity, Thread.currentThread().getUncaughtExceptionHandler()));
	}

	public static void showException(Context context, Throwable ex) {
		SystemUIUtil.showOKDialog(context, "Oups!", ex.getMessage(), android.R.drawable.ic_dialog_alert);
	}
}
