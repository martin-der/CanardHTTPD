package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.content.Context;
import android.text.format.DateFormat;

import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractSharedThingWriter<THING extends SharedThing> extends CommonHTMLComponent implements SharedThingWriter<THING> {

	protected AbstractSharedThingWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	public static abstract class SharedThingTool {
		private final String label;
		private final String iconName;

		protected SharedThingTool(String label, String iconName) {
			this.label = label;
			this.iconName = iconName;
		}

		public String getLabel() {
			return label;
		}

		public String getIconName() {
			return iconName;
		}

		public abstract String getUrl(String thingUri);

		public abstract String getJsAction(String thingUri);
	}

	@Override
	public final void write(Writer writer, final String uri, final THING sharedThing) throws IOException {
		final java.text.DateFormat dateFormat = DateFormat.getMediumDateFormat(context);
		java.text.DateFormat timeFormat = DateFormat.getTimeFormat(context);
		writer.append("<div class=\"presentation\">");

		writer.append("<div class=\"information\">");

		writer.append("<h1>" + sharedThing.getName() + "</h1>");

		writer.append("<div>Share date : ");
		if (sharedThing.getShareDate() != null) {
			writer.append(dateFormat.format(sharedThing.getShareDate()));
			writer.append("&#160;");
			writer.append(timeFormat.format(sharedThing.getShareDate()));
		}
		writer.append("</div>");

		writer.append("</div>");

		writer.append("<div class=\"tool\">");
		writeTools(writer, uri, sharedThing);
		writer.append("</div>");

		writer.append("<div class=\"float-clear\"></div>");

		writer.append("<hr/>");

		writer.append("<div class=\"thing\">");
		writeThing(writer, uri, sharedThing);
		writer.append("</div></div>");
	}

	protected abstract void writeThing(Writer writer, String uri, final THING thing) throws IOException;

	private void writeTools(Writer writer, String uri, final THING thing) throws IOException {
		writer.append("<ul>");
		final SharedThingTool[] extraTools = getTools(thing);
		writeTool(downloadTool, writer, uri, thing);
		writeTool(downloadAsZipTool, writer, uri, thing);
		if (extraTools!=null) {
			for (SharedThingTool tool : extraTools) {
				writeTool(tool, writer, uri, thing);
			}
		}
		writer.append("</ul>");
	}
	private void writeTool(SharedThingTool tool, Writer writer, String uri, final THING thing) throws IOException {
		writer.write("<li>");
		final String url = tool.getUrl(uri);
		if (url!=null) {
			writer.write("<a href=\""+httpContext+"/"+url+"\">");
		}

		writer.write("<img class=\"tool\" src=\""+httpContext+"/~/image/tool/"+tool.getIconName()+"\" />");

		if (url!=null) {
			writer.write("</a>");
		}
		writer.write("</li>");
	}


	protected abstract SharedThingTool[] getTools(final THING thing) throws IOException;

	private static final SharedThingTool downloadAsZipTool = new SharedThingTool("Download As Zip", "download-zip") {
		@Override
		public String getUrl(String thingUri) {
			return "/"+thingUri+"?t=a";
		}

		@Override
		public String getJsAction(String thingUri) {
			return null;
		}
	};
	private static final SharedThingTool downloadTool = new SharedThingTool("Download", "download") {
		@Override
		public String getUrl(String thingUri) {
			return "/"+thingUri+"?t=r";
		}

		@Override
		public String getJsAction(String thingUri) {
			return null;
		}
	};
}
