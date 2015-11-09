package net.tetrakoopa.canardhttpd.service.http.writer.template;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.metafs.BreadCrumb;
import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;

import java.io.IOException;
import java.io.PrintStream;

public abstract class AbstractContentWriter extends CommonHTMLComponent{

    public AbstractContentWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	
}
