package net.tetrakoopa.canardhttpd.service.http;

import android.content.Context;
import android.util.Log;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;
import net.tetrakoopa.canardhttpd.CanardHTTPDService;
import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.domain.metafs.BreadCrumb;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedText;
import net.tetrakoopa.canardhttpd.service.CanardLogger;
import net.tetrakoopa.canardhttpd.service.http.writer.BaseServlet;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.DirectoryWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.GroupWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.TextWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.GenericFileWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.SpecificFileWriter;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.service.sharing.exception.IncorrectUrlException;
import net.tetrakoopa.canardhttpd.util.TemporaryMimeTypeUtil;

import org.apache.http.HttpRequest;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CanardHTTPD extends Server {

	public static final String MIME_PNG = "application/x-png";

	public static final String MIME_HTML = "text/html";

	private final Map<String, List<SpecificFileWriter>> typeMimewriters = new HashMap<String, List<SpecificFileWriter>>();
	private final TextWriter textWriter = new TextWriter();

	private final Context context;
	private final SharesManager sharesManager;
	
	private static final int MAX_IDLE_TIME = 30000;

	private GroupWriter groupWriter;

	private final Map<HttpRequest, SharedThing> downloads = new HashMap<HttpRequest, SharedThing>();

	private final SslSocketConnector channelConnectorSSL;
	private final SelectChannelConnector channelConnector;

	public CanardHTTPD(Context context, SharesManager sharesManager, String hostname, int port, int sercurePort, String keyStorePath, String keyStorePassord) {
		this.sharesManager = sharesManager;
		this.context = context;
		this.groupWriter = new GroupWriter();

		final SslContextFactory sslContextFactory = new SslContextFactory(true);
		sslContextFactory.setKeyStorePassword(keyStorePassord);

		SslSocketConnector maybeChannelConnectorSSL = null;
		try {
			maybeChannelConnectorSSL = new SslSocketConnector(sslContextFactory);
		}catch (Exception ex) {
			Log.e(CanardHTTPDService.TAG, "Could not build SslSocketConnector : "+ex.getMessage(), ex);
		}
		channelConnectorSSL = null; //	maybeChannelConnectorSSL;
		channelConnector = new SelectChannelConnector();

		if (channelConnectorSSL!=null) {
			channelConnectorSSL.setPort(sercurePort);
			channelConnectorSSL.setMaxIdleTime(MAX_IDLE_TIME);
			channelConnectorSSL.setReuseAddress(true);
		}

		channelConnector.setPort(port);
		channelConnector.setMaxIdleTime(MAX_IDLE_TIME);
		channelConnector.setAcceptors(2);
		channelConnector.setConfidentialPort(18083);

		if (channelConnectorSSL!=null) {
			this.setConnectors(new Connector[] { channelConnectorSSL, channelConnector });
		} else {
			this.setConnectors(new Connector[] { channelConnector });
		}
		this.setHandler(handler);
	}


	private final Handler handler = new AbstractHandler()
	{
		@Override
		public void handle(String arg0, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
			CanardLogger.i("Request from '" + request.getRemoteAddr() + "' for '" + request.getRequestURI() + "'");
			final long begin = System.nanoTime();
			try {
				serve(request, response);
				((Request) request).setHandled(true);
				final long end = System.nanoTime();
				CanardLogger.i("Request for '" + request.getRequestURI() + "' served in " + (end - begin) + " ms");
			} catch (Exception ex) {
				final long end = System.nanoTime();
				CanardLogger.e("Failed to serve '" + request.getRequestURI() + "' after " + (end - begin) + " ms");
			}
		}
	};

	protected InputStream getAsset(String asset) throws IOException {
		return context.getAssets().open(asset);
	}

	protected void serveAssetOrNotFound(HttpServletRequest request, HttpServletResponse response, String assetName, String mimeType) throws IOException {
		final InputStream inputStream;
		try {
			inputStream = getAsset(assetName);
		} catch (IOException e) {
			serveNotFoundResponse(request, response);
			return;
		}
		serveFile(request, response, mimeType, inputStream);
	}

	private void serveFile(HttpServletRequest request, HttpServletResponse response, String mimeType, InputStream inputStream) throws IOException {
		response.setContentType(mimeType);
		response.setStatus(HttpServletResponse.SC_OK);
		final OutputStream output = response.getOutputStream();
		byte buffer[] = new byte[1000];
		int len;
		while ((len = inputStream.read(buffer)) > 0) {
			output.write(buffer, 0, len);
		}
		((Request) request).setHandled(true);

	}


	public void serve(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String uri = request.getRequestURI();

		if (request.getRequestURI().equals("/favicon"))
			serveAssetOrNotFound(request, response, "www/public-resources/image/Web-Browser-icon-48.png", MIME_PNG);

		if (uri.startsWith("/~/")) {
			serveWWWStaticResource(request, response, uri.substring(2));
		}

		serveThing(request, response);
	}

	private boolean serveThing(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String uri = request.getRequestURI();
		final PrintStream stream = new PrintStream(response.getOutputStream());
		// response.get

//		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
//		int ii = uri.indexOf("?");
//		if (ii >= 0) {
//			parameters.putAll(decodeParameters(uri.substring(ii + 1)));
//			uri = uri.substring(0, ii);
//		}

		final BaseServlet.Method method = BaseServlet.Method.fromName(request.getMethod());

		Map<String, String> headers = null;
		Map<String, String> params = null;
		;
		Map<String, String> files = null;

		if (uri.startsWith("/")) {
			final BreadCrumb breadCrumb = new BreadCrumb();
			SharedThing thing;
			try {
				thing = sharesManager.findThingAndBuildBreadCrumb(uri, breadCrumb);
			} catch (IncorrectUrlException e) {
				Log.d(CanardHTTPDService.TAG, "Could not find share at '" + uri + "' : " + e.getMessage());
				return false;
			}

			if (thing instanceof SharedCollection) {
				SharedCollection collection = (SharedCollection)thing;
				 if (collection instanceof SharedGroup) {
					 new GroupWriter().write(stream, uri, (SharedGroup)collection, method, headers, params, files);
				 } else {
					 new DirectoryWriter().write(stream, uri, (SharedDirectory)collection, method, headers, params, files);
				 }
			} else if (thing instanceof SharedText) {
				textWriter.write(stream, uri, (SharedText)thing, method, headers, params, files);
			} else if (thing instanceof SharedFile) {
				final SharedFile sharedFile = (SharedFile)thing;
				SpecificFileWriter writer = null;
				if (sharedFile.getMimeType() == null) {
					final List<SpecificFileWriter> writers = typeMimewriters.get(sharedFile.getMimeType());
					if (writers != null) {
						writer = getBestHandler(sharedFile, writers);
					}
				}
				if (writer != null) {
					writer.write(stream, uri, sharedFile, method, headers, params, files);
				} else {
					Log.w(CanardHTTPDActivity.TAG, "No handler found for file with mime '" + sharedFile.getMimeType() + "'");
					new GenericFileWriter().write(stream, uri, sharedFile, method, headers, params, files);
				}
			}
		}

		Log.i(CanardHTTPDActivity.TAG, "Could not handle url '" + uri +"'");

		return false;
	}
	
	private void serveWWWStaticResource(HttpServletRequest request, HttpServletResponse response, String resource) throws IOException {
		if (resource.startsWith("/") && resource.length() > 1) {
			String asset = resource.substring(1);
			String mimeType = //SystemUtil.getMimeTypeFromExtension(asset);
					TemporaryMimeTypeUtil.basicMimeTypeFromExtension(asset);
			serveAssetOrNotFound(request, response, "www/public-resources/" + asset, mimeType);
		} else {
			serveNotFoundResponse(request, response);
		}
	}

	public static void serveNotFoundResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		serveText(request, response, HttpServletResponse.SC_NOT_FOUND, MIME_HTML, "Not found");
	}

	public static void serveInternalErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws IOException {
		serveText(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, MIME_HTML, "Ooops, an internal error (" + ex.getClass().getName() + ") occured : " + ex.getMessage());
	}

	private static void serveText(HttpServletRequest request, HttpServletResponse response, int responseCode, String mime, String text) throws IOException {
		response.setContentType(mime);
		response.setStatus(responseCode);
		response.getWriter().println(text);
	}



	/**
	 * 
	 * @param writers
	 * @return best hanlder, <code>null</code> is none found
	 */
	private SpecificFileWriter getBestHandler(SharedFile sharedFile, List<SpecificFileWriter> writers) {
		SpecificFileWriter bestFittedHandler = null;
		SpecificFileWriter.HandleAffinity bestFittedHandlerAffinity = null;

		for (SpecificFileWriter writer : writers) {
			SpecificFileWriter.HandleAffinity affinity = writer.affinityWith(sharedFile.getMimeType());
			if (affinity == null || affinity != SpecificFileWriter.HandleAffinity.DONT_KNOW)
				continue;

			if (bestFittedHandler == null) {
				bestFittedHandler = writer;
				bestFittedHandlerAffinity = affinity;
			} else {
				if (affinity.ordinal() > bestFittedHandlerAffinity.ordinal()) {
					bestFittedHandler = writer;
					bestFittedHandlerAffinity = affinity;
				}
			}
		}
		return bestFittedHandler;
	}

//	public static void main(String[] args) throws Exception {
//		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
//		server.createContext("/test", new MyHandler());
//		server.setExecutor(null); // creates a default executor
//		server.start();
//	}
//
//	static class MyHandler implements HttpHandler {
//		public void handle(HttpExchange t) throws IOException {
//			String response = "This is the response";
//			t.sendResponseHeaders(200, response.length());
//			OutputStream os = t.getResponseBody();
//			os.write(response.getBytes());
//			os.close();
//		}
//	}

}
