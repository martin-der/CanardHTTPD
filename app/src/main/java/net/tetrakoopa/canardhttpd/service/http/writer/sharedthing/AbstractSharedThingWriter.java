package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import android.text.format.DateFormat;

import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.http.writer.BaseServlet;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractSharedThingWriter<THING extends SharedThing> extends BaseServlet implements SharedThingWriter<THING> {

	@Override
	public final void write(Writer writer, final String uri, final THING sharedThing) throws IOException {
		java.text.DateFormat dateFormat = DateFormat.getMediumDateFormat(getContext());
		writer.append("<div class=\"information\">");

		writer.append("<h1>" + sharedThing.getName() + "</h1>");

		writer.append("<div>Share date : ");
		if (sharedThing.getShareDate() != null) {
			writer.append(dateFormat.format(sharedThing.getShareDate()));
		}
		writer.append("</div>");

		writer.append("<div class=\"detail\">");
		writeThing(writer, uri, sharedThing);
		writer.append("</div></div>");
	}

	protected abstract void writeThing(Writer writer, String uri, final THING thing) throws IOException;

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
