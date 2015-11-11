package net.tetrakoopa.canardhttpd.service.http.writer.template;

import android.content.Context;
import android.util.Log;

import net.tetrakoopa.canardhttpd.CanardHTTPDService;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.domain.metafs.BreadCrumb;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.service.sharing.exception.IncorrectUrlException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.NoSuchThingSharedException;
import net.tetrakoopa.mdu.text.formater.BufferedEnclosedTextConverter;
import net.tetrakoopa.mdu.text.formater.EnclosedTextConverter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public abstract class ContentWriter extends AbstractContentWriter {

	public ContentWriter(Context context, String httpContext) {
		super(context, httpContext);
		this.context.put("content.bread-crumb", new ReplacerTool() {
			@Override
			public void write(Writer destination, BreadCrumb breadCrumb, SharedThing thing) throws IOException {
				ContentWriter.this.writeBreadCrumb(destination, breadCrumb);
			}
		});
		this.context.put("content.thing", new ReplacerTool() {
			@Override
			public void write(Writer destination, BreadCrumb breadCrumb, SharedThing thing) throws IOException {
				ContentWriter.this.writeThing(destination, thing);
			}
		});
	}

	protected abstract void writeThing(Writer destination, SharedThing thing) throws IOException;

	public interface ReplacerTool {
		void write(Writer destination, BreadCrumb breadCrumb, SharedThing thing) throws IOException;
	}
	private final Map<String, ReplacerTool> context = new HashMap<>();

	public void write(Writer destination, final SharedThing thing, final BreadCrumb breadCrumb) throws IOException {

		final EnclosedTextConverter.ConverterTools<Map<String, ReplacerTool>> converterTools = new EnclosedTextConverter.ConverterTools<Map<String, ReplacerTool>>() {

			@Override
			public void convert(Map<String, ReplacerTool> stringReplacerToolMap, String key, Writer destination, int extraMustaches) throws IOException {
				final ReplacerTool replacerTool = stringReplacerToolMap.get(key);
				if (replacerTool != null) {
					replacerTool.write(destination, breadCrumb, thing);
				}
			}

			@Override
			public Object comment(Map<String, ReplacerTool> stringReplacerToolMap, String subject) {
				return null;
			}
		};

		final EnclosedTextConverter<Map<String,ReplacerTool>> replacer = new BufferedEnclosedTextConverter<>();

		final Reader reader = new InputStreamReader(getAsset("www/template/piece/content.html"), "UTF-8");

		replacer.process(reader, destination, context, converterTools);
	}


	private void writeBreadCrumb(Writer writer, BreadCrumb breadCrumb) throws IOException {
		writer.write("<a href=\""+httpContext+"/\">"+"ROOT"+"</a>");
		StringBuffer url = new StringBuffer();
		for (BreadCrumb.Part part : breadCrumb.getParts()) {
			url.append(part.getLabel());
			writer.write("/<a href=\""+httpContext+"/"+url.toString()+"\">"+escapedXmlAlsoSpace(part.getLabel())+"</a>");
		}
	}

}
