package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.http.writer.BaseServlet.Method;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

public interface SharedThingWriter<THING extends SharedThing> {

	void write(PrintStream stream, String uri, THING thing) throws IOException;

}
