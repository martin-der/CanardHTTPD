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

	}

	
}
