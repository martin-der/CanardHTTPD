package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.text.format.DateFormat;

import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.http.writer.BaseServlet;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractSharedThingWriter<THING extends SharedThing> extends BaseServlet implements SharedThingWriter<THING> {

	@Override
	public final void write(PrintStream stream, final String uri, final THING sharedThing, final Method method, final Map<String, String> headers, final Map<String, String> params, final Map<String, String> files) throws IOException {
		java.text.DateFormat dateFormat = DateFormat.getMediumDateFormat(getContext());
		stream.append("<div class=\"information\">");

		stream.append("<h1>" + sharedThing.getName() + "</h1>");

		stream.append("<div>Share date : ");
		if (sharedThing.getShareDate() != null) {
			stream.append(dateFormat.format(sharedThing.getShareDate()));
		}
		stream.append("</div>");

		stream.append("<div class=\"detail\">");
		writeHtmlDetails(stream, uri, sharedThing, method, headers, params, files);
		stream.append("</div></div>");
	}

	protected abstract void writeHtmlDetails(PrintStream stream, String uri, final THING thing, Method method, Map<String, String> headers, Map<String, String> params, Map<String, String> files) throws IOException;

	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) {
		final String uri = request.getRequestURI();
		// final OutputStream stream = response.getOutputStream();
		// write(new PrintStream(response.getOutputStream()), uri);
	}

	@Override
	public final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}

}
