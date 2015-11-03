package net.tetrakoopa.canardhttpd.service.http.writer.template;

import net.tetrakoopa.canardhttpd.domain.metafs.BreadCrumb;

import java.io.IOException;
import java.io.PrintStream;

/** Page with a content<br/>
 * display a breadcrumb...
 */
public abstract class AbstractContentWriter {

	protected abstract void writeContent(PrintStream stream) throws IOException;
	

	private String rootName = "root";
	
	private final BreadCrumb breadCrumb;
	
	public AbstractContentWriter(BreadCrumb breadCrumb) {
		this.breadCrumb = breadCrumb;
	}
	
	public final void write(PrintStream stream) throws IOException {
		
		stream.print("\n<div class=\"breadcrumb sub-container\">\n");
		
		stream.print("<span class=\"breadcrumb\">");
		StringBuffer path = new StringBuffer(); 
		stream.print("/<a href=\"/\">"+rootName+"</a>");
		if (breadCrumb != null) {
			for (BreadCrumb.Part part : breadCrumb.getParts()) {
				path.append('/');
				path.append(part.getLabel());
				stream.print("/<a href=\""+path+"\">"+part.getLabel()+"</a>");
			}
		}
		stream.print("</span>\n");

		stream.print("</div>\n\n");
		
		stream.print("\n<div class=\"sub-container\">");
		writeContent(stream);
		stream.print("</div>\n\n");
	}

	
}
