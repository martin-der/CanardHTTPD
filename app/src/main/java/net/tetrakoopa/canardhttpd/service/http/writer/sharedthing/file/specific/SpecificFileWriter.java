package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific;

import android.content.Context;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.FileWriter;

import java.util.List;


public abstract class SpecificFileWriter extends FileWriter {

	protected SpecificFileWriter(Context context, String httpContext) {
		super(context, httpContext);
	}

	public enum HandleAffinity {
		DOESNT_KNOW, IS_NOT_FOUND_OF, HANDLES_WELL, MASTERIZES, IS_REFERENCE_IMPLEMENTATION;
	}
	
	final protected static boolean isMimeTypeOneOf(String type, String... types) {
		for (String t : types) {
			if (type.equals(t))
				return true;
		}
		return false;
	}

	public abstract HandleAffinity affinityWith(String mimeType);

	/**
	 *
	 * @param writers
	 * @return best hanlder, <code>null</code> is none found
	 */
	public static SpecificFileWriter getBestHandler(String mimeType, List<SpecificFileWriter> writers) {
		SpecificFileWriter bestFittedHandler = null;
		SpecificFileWriter.HandleAffinity bestFittedHandlerAffinity = null;

		for (SpecificFileWriter writer : writers) {
			final SpecificFileWriter.HandleAffinity affinity = writer.affinityWith(mimeType);

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
