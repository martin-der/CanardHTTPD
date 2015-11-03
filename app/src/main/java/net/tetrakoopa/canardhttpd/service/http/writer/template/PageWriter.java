package net.tetrakoopa.canardhttpd.service.http.writer.template;

import android.util.Log;

import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public abstract class PageWriter extends CommonHTMLComponent {

	private final static String TITLE = "Canard HTTPD";

//	private final MustacheShaver<Map<String, Object>> templatedHtmlWriter = new MustacheShaver<Map<String, Object>>();
//	private final MustacheShaver.BarberTools<Map<String, Object>> tools = new MustacheShaver.BarberTools<Map<String, Object>>() {
//		@Override
//		public void shave(Map<String, Object> context, String key, FileWriter oututStream) {
//			// TODO Auto-generated method stub
//
//		}
//	};

	private final static TemplateArg NO_PARAM = new TemplateArg();

	public void writeHeader(PrintStream stream) {
		writeAsset("www/template/piece/header.html", stream);
	}

	public abstract void writePageContent(PrintStream stream) throws IOException;

	public void writeFooter(PrintStream stream) {
		writeAsset("www/template/piece/footer.html", stream);
	}

	public final void write(PrintStream stream, TemplateArg arg) throws IOException {

		if (arg == null)
			arg = NO_PARAM;

		stream.append("<html><head>\n");
		writeAsset("www/template/piece/common-head.html", stream);
		titleMeta(arg.page_title);
		stream.append("\n</head><body>\n");

		if (!arg.hideHeader) {
			stream.print("\n<div class=\"header main-container\" >");
			writeHeader(stream);
			stream.print("</div>\n\n");
		}

		stream.print("\n<div class=\"content main-container\">");
		writePageContent(stream);
		stream.print("</div>\n\n");

		if (!arg.hideFooter) {
			stream.print("\n<div class=\"footer main-container\" style=\"text-align: center;\">");
			writeFooter(stream);
			stream.print("</div>\n\n");
		}

		stream.append("\n</body>");
	}

	public final void dumpToStringBuffer(StringBuffer buffer, TemplateArg arg) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			write(new PrintStream(output), arg);
			final String page;
			try {
				page = new String(output.toByteArray(), DEFAULT_PAGE_ENCODING);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "Page does not fit '" + DEFAULT_PAGE_ENCODING + "' encoding", e);
				return;
			}
			buffer.append(page);
		}
		finally {
			output.close();
		}
	}

	public final String dumpToString(TemplateArg arg) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		dumpToStringBuffer(buffer, arg);
		return buffer.toString();
	}

	protected String titleMeta(String title) {
		return "<title>" + CommonHTMLComponent.escapedXmlContent((title == null ? TITLE : (title + " - " + TITLE))) + "</title></head>";
	}

}
