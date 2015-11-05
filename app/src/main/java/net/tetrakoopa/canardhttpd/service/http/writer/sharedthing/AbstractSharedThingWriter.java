package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.content.Context;
import android.text.format.DateFormat;

import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.http.writer.BaseWriter;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractSharedThingWriter<THING extends SharedThing> extends BaseWriter implements SharedThingWriter<THING> {

	protected AbstractSharedThingWriter(Context context) {
		super(context);
	}

	@Override
	public final void write(Writer writer, final String uri, final THING sharedThing) throws IOException {
		java.text.DateFormat dateFormat = DateFormat.getMediumDateFormat(context);
		writer.append("<div class=\"information\">");

		writer.append("<h1>" + sharedThing.getName() + "</h1>");

		writer.append("<div>Share date : ");
		if (sharedThing.getShareDate() != null) {
			writer.append(dateFormat.format(sharedThing.getShareDate()));
		}
		writer.append("</div>");

		writer.append("<div class=\"detail\">");
		writeThing(writer, uri, sharedThing);
		writer.append("</div></div>");
	}

	protected abstract void writeThing(Writer writer, String uri, final THING thing) throws IOException;

}
