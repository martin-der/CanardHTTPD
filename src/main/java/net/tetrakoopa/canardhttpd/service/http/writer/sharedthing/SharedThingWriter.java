package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing;

import net.tetrakoopa.canardhttpd.domain.common.SharedThing;

import java.io.IOException;
import java.io.Writer;

public interface SharedThingWriter<THING extends SharedThing> {

	void write(Writer writer, String uri, THING thing) throws IOException;

}
