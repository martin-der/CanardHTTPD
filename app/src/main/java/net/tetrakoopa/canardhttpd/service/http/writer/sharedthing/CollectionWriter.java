package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;

import java.io.IOException;
import java.io.Writer;

public abstract class 	CollectionWriter<C extends SharedCollection> extends AbstractSharedThingWriter<C> {

	protected CollectionWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	@Override
	protected final void writeThing(Writer writer, String uri, C collection) throws IOException {

		writer.append("<table class=\"shared-things\">\n");
		int index = 0;
		writer.append("\t<tr>");
		writer.append("<th></th>");
		writer.append("<th>Name</th>");
		writer.append("<th>Comment</th>");
		writer.append("<th></th>");
		writer.append("</tr>\n");
		for (SharedThing thing : collection.getThings()) {
			writer.append("\t<tr class=\"" + (index % 2 == 0 ? "even" : "odd") + "-row\">");
			writer.append("<td></td>");
			writer.append("<td><a href=\""+httpContext+"/" + CommonHTMLComponent.escapedXml(thing.getName()) + "?v=h" + "\">" + CommonHTMLComponent.escapedXmlAlsoSpace(thing.getName()) + "</a></td>");
			writer.append("<td>" + (thing.getComment() != null ? CommonHTMLComponent.escapedXml(thing.getComment()) : "") + "</td>");
			writer.append("<td><a href=\""+httpContext+"/" + CommonHTMLComponent.escapedXml(thing.getName()) + "\">" + "&#160;[V]" + "</a></td>");
			writer.append("</tr>\n");
			index++;
		}
		writer.append("</table>\n");
		
	}

}
