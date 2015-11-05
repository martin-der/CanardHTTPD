package net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file;

import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.AbstractSharedThingWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.SharedThingWriter;
import net.tetrakoopa.mdu.util.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public abstract class FileWriter extends AbstractSharedThingWriter<SharedFile> implements SharedThingWriter<SharedFile> {

	public final void writeRaw(OutputStream stream, SharedFile sharedFile, Method method, Map<String, String[]> headers, Map<String, String[]> parms) throws IOException {
		final InputStream inputStream = new FileInputStream(sharedFile.getFile());
		FileUtil.copy(inputStream, stream);
	}

//	@Override
//	public final void doGet(HttpServletRequest request, HttpServletResponse response) {
//		write(new PrintStream(response.getOutputStream()), request.get);
//	}
//
//	@Override
//	public final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		super.doGet(request, response);
//	}

}
