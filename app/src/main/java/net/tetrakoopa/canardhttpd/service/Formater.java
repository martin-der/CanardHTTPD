package net.tetrakoopa.canardhttpd.service;

import android.content.Context;

import java.text.DateFormat;

public class Formater {

	private final Context context;

	private final DateFormat dateFormat;

	public Formater(Context context) {
		dateFormat = android.text.format.DateFormat.getDateFormat(context);
		this.context = context;
	}

	public java.text.DateFormat getDateFormat() {
		return dateFormat;
	}

}
