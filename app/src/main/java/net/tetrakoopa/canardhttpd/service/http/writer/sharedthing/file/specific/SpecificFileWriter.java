package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific;

import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.FileWriter;


public abstract class SpecificFileWriter extends FileWriter {

	public static enum HandleAffinity {
		DONT_KNOW, NOT_FOUND_OF, HANDLE_WELL, MASTERIZE, IS_REFERENCE_IMPLEMENTATION;
	}
	
	final protected static boolean isMimeTypeOneOf(String type, String... types) {
		for (String t : types) {
			if (type.equals(t))
				return true;
		}
		return false;
	}

	public abstract HandleAffinity affinityWith(String mimeType);

}
