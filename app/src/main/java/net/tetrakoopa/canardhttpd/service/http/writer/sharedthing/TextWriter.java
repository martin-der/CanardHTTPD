package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedText;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Map;

public class TextWriter extends AbstractSharedThingWriter<SharedText> implements SharedThingWriter<SharedText> {


	@Override
	public void writeThing(Writer writer, String uri, SharedText sharedText) throws IOException {
		writer.append("<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">" + escapedXmlContent(sharedText.getText()) + "</pre>");
	}


	// @Override
//	protected final void writeRaw(OutputStream stream, SharedText sharedText, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) throws UnsupportedEncodingException, IOException {
//		stream.write(sharedText.getText().getBytes(CommonHTMLComponent.DEFAULT_PAGE_ENCODING));
//	}

}
