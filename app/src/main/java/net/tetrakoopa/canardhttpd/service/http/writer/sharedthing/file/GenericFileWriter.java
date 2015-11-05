package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;


public class GenericFileWriter extends FileWriter {

	private static final long serialVersionUID = 2738856182718165033L;

	@Override
	protected void writeThing(PrintStream stream, String uri, SharedFile sharedFile) throws IOException {
		// TODO Auto-generated method stub
		stream.append("Some unknown file");
	}
	
}
