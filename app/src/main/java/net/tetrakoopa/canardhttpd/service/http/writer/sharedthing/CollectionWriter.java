package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

public class CollectionWriter<C extends SharedCollection> extends AbstractSharedThingWriter<C> {

	@Override
	protected final void writeHtmlDetails(PrintStream stream, String uri, C collection,
			Method method, Map<String, String> headers,
			Map<String, String> params, Map<String, String> files)
			throws IOException {

		stream.append("<table class=\"shared-things\">\n");
		int index = 0;
		stream.append("\t<tr>");
		stream.append("<th></th>");
		stream.append("<th>Name</th>");
		stream.append("<th>Comment</th>");
		stream.append("<th></th>");
		stream.append("</tr>\n");
		for (SharedThing thing : collection.getThings()) {
			stream.append("\t<tr class=\"" + (index % 2 == 0 ? "even" : "odd") + "-row\">");
			stream.append("<td></td>");
			stream.append("<td><a href=\"/" + CommonHTMLComponent.escapedXmlAttribut(thing.getName()) + "?v=h" + "\">" + CommonHTMLComponent.escapedXmlContent(thing.getName()) + "</a></td>");
			stream.append("<td>"+(thing.getComment()!=null?CommonHTMLComponent.escapedXmlContent(thing.getComment()):"")+"</td>");
			stream.append("<td><a href=\"/" + CommonHTMLComponent.escapedXmlAttribut(thing.getName()) + "\">" + "&#160;[V]" + "</a></td>");
			stream.append("</tr>\n");
			index++;
		}
		stream.append("</table>\n");
		
	}

}
