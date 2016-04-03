package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.AbstractSharedThingWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.SharedThingWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class SpecificSerialisedWriter<THING extends SharedThing> extends AbstractSharedThingWriter<THING> implements SharedThingWriter<THING> {

    public enum HandleAffinity {
        DOESNT_KNOW, IS_NOT_FOUND_OF, HANDLES_WELL, MASTERIZES, IS_REFERENCE_IMPLEMENTATION;
    }

    public abstract HandleAffinity affinityWith(String mimeType);

    protected SpecificSerialisedWriter(Context context, String httpContext) {
        super(context, httpContext);
    }

    protected abstract InputStream getInputStream(Context context, THING thing) throws IOException;


    final protected static boolean isMimeTypeOneOf(String type, String... types) {
        for (String t : types) {
            if (type.equals(t))
                return true;
        }
        return false;
    }

    /**
     *
     * @param writers
     * @return best hanlder, <code>null</code> is none found
     */
    public final static SpecificSerialisedWriter getBestHandler(String mimeType, List<SpecificSerialisedWriter> writers) {
        SpecificSerialisedWriter bestFittedHandler = null;
        HandleAffinity bestFittedHandlerAffinity = null;

        for (SpecificSerialisedWriter writer : writers) {
            final HandleAffinity affinity = writer.affinityWith(mimeType);

            if (affinity == null)
                continue;

            if ((bestFittedHandler == null) || (affinity.ordinal() > bestFittedHandlerAffinity.ordinal())) {
                bestFittedHandler = writer;
                bestFittedHandlerAffinity = affinity;
            }
        }
        return bestFittedHandler;
    }


}
