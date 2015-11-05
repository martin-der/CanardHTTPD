package net.tetrakoopa.canardhttpd.service.http.writer.template;

import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;
import net.tetrakoopa.mdu.text.formater.BufferedEnclosedTextConverter;
import net.tetrakoopa.mdu.text.formater.EnclosedTextConverter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public abstract class PageWriter extends CommonHTMLComponent {

	public enum Method {
		GET, POST;

	   public static Method fromName(String name) {
			if (name==null)
				return null;
			for (Method possibleMatch : Method.values()) {
			if (possibleMatch.name().equals(name.toUpperCase()))
				return possibleMatch;
			}
			throw new IllegalArgumentException();
	   }

	}

	private final static String TITLE = "Canard HTTPD";

//	private final FastEnclosedTextConverter<Map<String, Object>> templatedHtmlWriter = new FastEnclosedTextConverter<Map<String, Object>>();
//	private final FastEnclosedTextConverter.ConverterTools<Map<String, Object>> tools = new FastEnclosedTextConverter.ConverterTools<Map<String, Object>>() {
//		@Override
//		public void convert(Map<String, Object> context, String key, FileWriter oututStream) {
//			// TODO Auto-generated method stub
//
//		}
//	};


	public interface ReplacerTool {
		void write(Writer destination, TemplateArg arg) throws IOException;
	}
	public static class SimpleStringReplacerTool implements ReplacerTool {
		final String string;
		public SimpleStringReplacerTool(String string) {
			this.string = string;
		}
		public void write(Writer destination, TemplateArg arg) throws IOException {
			destination.append(string);
		}
	}

	private final Map<String, ReplacerTool> writerContext = new HashMap<>();


	protected PageWriter() {
		writerContext.put("static-resources.url", new SimpleStringReplacerTool("/~/"));
		writerContext.put("html.head", null);
		writerContext.put("body.header", new ReplacerTool() {
			@Override
			public void write(Writer destination, TemplateArg arg) {
				PageWriter.this.writeHeader(destination, arg);
			}
		});
		writerContext.put("body.content", new ReplacerTool() {
			@Override
			public void write(Writer destination, TemplateArg arg) {
				PageWriter.this.writeContent(destination, arg);
			}
		});
		writerContext.put("body.footer", new ReplacerTool() {
			@Override
			public void write(Writer destination, TemplateArg arg) {
				PageWriter.this.writeFooter(destination, arg);
			}
		});
	}

	private static final EnclosedTextConverter.ConverterTools<Map<String, ReplacerTool>> barberTools = new EnclosedTextConverter.ConverterTools<Map<String, ReplacerTool>>() {

		@Override
		public void convert(Map<String, ReplacerTool> stringReplacerToolMap, String key, Writer destination, int extraMustaches) throws IOException {
			final ReplacerTool replacerTool = stringReplacerToolMap.get(key);
			if (replacerTool != null) {
				replacerTool.write(destination, null);
			}
		}

		@Override
		public Object comment(Map<String, ReplacerTool> stringReplacerToolMap, String subject) {
			return null;
		}
	};


	abstract protected void writeHeader(Writer destination, TemplateArg arg);
	abstract protected void writeContent(Writer destination, TemplateArg arg);
	abstract protected void writeFooter(Writer destination, TemplateArg arg);

	final EnclosedTextConverter<Map<String,ReplacerTool>> replacer = new BufferedEnclosedTextConverter<>();

	private final static TemplateArg NO_PARAM = new TemplateArg();

	public void write(PrintStream stream, TemplateArg arg) throws IOException {

		if (arg == null)
			arg = NO_PARAM;

		final Reader reader = new InputStreamReader(getAsset("www/template/layout/classic_HeaderFooter_Vertical.html"), "UTF-8");

		replacer.process(reader, new PrintWriter(stream), writerContext, barberTools);

	}

	protected String titleMeta(String title) {
		return "<title>" + CommonHTMLComponent.escapedXmlContent((title == null ? TITLE : (title + " - " + TITLE))) + "</title></head>";
	}

	/*

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

	 */

}
