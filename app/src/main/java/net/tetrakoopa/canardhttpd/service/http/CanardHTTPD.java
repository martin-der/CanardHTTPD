package net.tetrakoopa.canardhttpd.service.http;

import android.content.Context;
import android.util.Log;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;
import net.tetrakoopa.canardhttpd.CanardHTTPDService;
import net.tetrakoopa.canardhttpd.R;
import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedText;
import net.tetrakoopa.canardhttpd.service.CanardLogger;
import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.DirectoryWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.GroupWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.TextWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.GenericFileWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.ImageWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.SpecificFileWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.VCardWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.template.ContentWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.template.PageWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.template.TemplateArg;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.util.TemporaryMimeTypeUtil;
import net.tetrakoopa.canardhttpd.util.WebUtil;

import org.apache.http.HttpRequest;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
//import org.eclipse.jetty.server.HttpConfiguration;
//import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CanardHTTPD extends Server {

	public static final String MIME_PNG = "application/x-png";

	public static final String MIME_HTML = "text/html";

	private final List<SpecificFileWriter> typeMimeWriters = new ArrayList<>();

	private final Context context;
	private final SharesManager sharesManager;

	private TextWriter textWriter;
	private GroupWriter groupWriter;
	private DirectoryWriter directoryWriter;
	private GenericFileWriter genericFileWriter;

	private String encoding = CommonHTMLComponent.DEFAULT_ENCODING;
	
	private static final int MAX_IDLE_TIME = 30000;


	private final Map<HttpRequest, SharedThing> downloads = new HashMap<HttpRequest, SharedThing>();

	private final String title;



	public CanardHTTPD(Context context, SharesManager sharesManager, String hostname, int port, int sercurePort, String keyStorePath, String keyStorePassord) {
		this.sharesManager = sharesManager;
		this.context = context;


		this.title = context.getString(R.string.app_name);

		init_Jetty_v8(port, sercurePort, keyStorePath, keyStorePassord);
	}

	private void makeWriters(String httpContext) {
		this.directoryWriter = new DirectoryWriter(context, httpContext);
		this.groupWriter = new GroupWriter(context, httpContext);
		this.textWriter = new TextWriter(context, httpContext);
		this.genericFileWriter = new GenericFileWriter(context, httpContext);


		this.typeMimeWriters.add(new VCardWriter(context, httpContext));
		this.typeMimeWriters.add(new ImageWriter(context, httpContext));

	}

	private void init_Jetty_v8(int port, int sercurePort, String keyStorePath, String keyStorePassord) {
		final Connector connectorSSL;
		final Connector connector;

		final SslContextFactory sslContextFactory = new SslContextFactory(true);
		sslContextFactory.setKeyStorePassword(keyStorePassord);
		//sslContextFactory.setKeyStore(new KeyStore(keyStorePath));

		Connector maybeChannelConnectorSSL = null;


		try {
			//maybeChannelConnectorSSL = new SslSelectChannelConnector(sslContextFactory);
		}catch (Exception ex) {
			Log.e(CanardHTTPDService.TAG, "Could not build SslSocketConnector : "+ex.getMessage(), ex);
		}
		connectorSSL = maybeChannelConnectorSSL;
		connector = new SelectChannelConnector();


		if (connectorSSL !=null) {
			connectorSSL.setPort(sercurePort);
			connectorSSL.setMaxIdleTime(MAX_IDLE_TIME);
		}

		connector.setPort(port);
		connector.setMaxIdleTime(MAX_IDLE_TIME);
		//connector.setAcceptQueueSize(2);

		if (connectorSSL !=null) {
			this.setConnectors(new Connector[] {connectorSSL, connector });
		} else {
			this.setConnectors(new Connector[] { connector });
		}
		this.setHandler(handler);

	}

	private final Handler handler = new AbstractHandler()
	{
		@Override
		public void handle(String arg0, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
			if (textWriter == null) {
				final String contextPath = request.getContextPath();
				makeWriters(contextPath != null ? contextPath: "");
			}
			CanardLogger.i("Request from '" + request.getRemoteAddr() + "' for '" + request.getRequestURI() + "'");
			final long begin = System.nanoTime();
			try {
				serve(request, response);
				((Request) request).setHandled(true);
				final long end = System.nanoTime();
				CanardLogger.d("Request for '" + request.getRequestURI() + "' served in " + ((end - begin)/1000) + " ms");
			} catch (Exception ex) {
				final long end = System.nanoTime();
				CanardLogger.e("Failed to serve '" + request.getRequestURI() + "' ( after " + ((end - begin)/1000) + " ms )");
			}
		}
	};

	protected InputStream getAsset(String asset) throws IOException {
		return context.getAssets().open(asset);
	}

	protected void serveAssetOrNotFound(HttpServletRequest request, HttpServletResponse response, String assetName) throws IOException {
		final String mimeType = findMimeType(assetName);
		serveAssetOrNotFound(request, response, assetName, mimeType);
	}

	private String findMimeType(String assetName) {

		switch(assetName) {
			//case : return "application/x-png";

		}

		return null;
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
		if (mimeType != null) {
			response.setContentType(mimeType);
		} else {
			Log.w(CanardHTTPDActivity.TAG, "Serving resource with unknown contentType");
		}
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

		if (uri.equals("/favicon") || uri.equals("/favicon.ico")) {
			serveAssetOrNotFound(request, response, "www/public-resources/image/Web-Browser-icon-48.png", MIME_PNG);
			return;
		}


		if (uri.startsWith("/~/")) {
			serveWWWStaticResource(request, response, uri.substring(2));
			return;
		}

		if (uri.startsWith("/")) {
			if ( serveThing(request, response)) {
				Log.i(CanardHTTPDActivity.TAG, "Could not handle url '" + uri +"'");
			}
			return;
		}

		Log.i(CanardHTTPDActivity.TAG, "Could not handle url '" + uri + "'");
	}

	private boolean serveThing(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final String contextPath = request.getContextPath() == null ? "" : request.getContextPath();

		final String uri = request.getRequestURI();
		final PrintWriter writer = new PrintWriter(response.getOutputStream());
		// response.get

//		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
//		int ii = uri.indexOf("?");
//		if (ii >= 0) {
//			parameters.putAll(decodeParameters(uri.substring(ii + 1)));
//			uri = uri.substring(0, ii);
//		}

		final Map<String, String> headers = null; //request.getParameterMap();
		final Map<String, String[]> params = request.getParameterMap();

		final String userAgent = request.getHeader("User-Agent");
		final boolean isMobileUser = userAgent != null && WebUtil.isMobileUserAgent(userAgent);

		final Cookie cookies[] = request.getCookies();
		final String themeName;
		String tmpThemeName =  "subtle";
		if (cookies!=null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("theme")) {
					tmpThemeName = cookie.getValue();
					break;
				}
			}
		}
		themeName = tmpThemeName;

		final ContentWriter contentWriter = new ContentWriter(context, contextPath) {

			@Override
			protected void writeThing(Writer destination, SharedThing thing) throws IOException {
				CanardHTTPD.this.writeThing(destination, thing, uri);
			}
		};

		final PageWriter pageWriter = new PageWriter(context, contextPath, request) {
			@Override
			protected void writeThemeName(HttpServletRequest request, Writer destination, TemplateArg arg) {
				writer.write(themeName);
			}
			@Override
			protected void writeHead(HttpServletRequest request, Writer destination, TemplateArg arg) {
				if (isMobileUser) {
					writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n");
					final String title = uri;
					writer.write("<title>" + escapedXml((title == null ? CanardHTTPD.this.title : (title + " - " + CanardHTTPD.this.title))) + "</title></head>");
				}
			}

			@Override
			protected void writeHeader(HttpServletRequest request, Writer destination, TemplateArg arg) {
				writeAsset("www/template/piece/header.html", writer);
			}

			@Override
			protected void writeContent(HttpServletRequest request, Writer destination, TemplateArg arg) {
				try {
					if (!contentWriter.write(destination, sharesManager, uri)) {
						Log.e(CanardHTTPDService.TAG, "Failed to write thing content (uri='" + uri + "')");
					};
				} catch (IOException ioex) {
					Log.e(CanardHTTPDService.TAG, "Failed to write thing content (uri='" + uri + "') : " + ioex.getMessage());
				}
			}

			@Override
			protected void writeFooter(HttpServletRequest request, Writer destination, TemplateArg arg) {
				writeAsset("www/template/piece/footer.html", writer);
			}
		};

		pageWriter.write(writer, null);

		writer.flush();

		return false;
	}

	private boolean writeThing(Writer writer, SharedThing thing, String url) throws IOException {

		if (thing instanceof SharedCollection) {
			SharedCollection collection = (SharedCollection)thing;
			if (collection instanceof SharedGroup) {
				groupWriter.write(writer, url, (SharedGroup) collection);
			} else {
				directoryWriter.write(writer, url, (SharedDirectory) collection);
			}
		} else if (thing instanceof SharedText) {
			textWriter.write(writer, url, (SharedText) thing);
		} else if (thing instanceof SharedFile) {
			final SharedFile sharedFile = (SharedFile)thing;
			SpecificFileWriter specificWriter = null;
			if (sharedFile.getMimeType() != null) {
				specificWriter = SpecificFileWriter.getBestHandler(sharedFile.getMimeType(), typeMimeWriters);
			}
			if (specificWriter != null) {
				specificWriter.write(writer, url, sharedFile);
			} else {
				Log.w(CanardHTTPDActivity.TAG, "No handler found for file with mime '" + sharedFile.getMimeType() + "'");
				genericFileWriter.write(writer, url, sharedFile);
			}
		}
		return true;
	}

	private void serveWWWStaticResource(HttpServletRequest request, HttpServletResponse response, String resource) throws IOException {
		if (resource.startsWith("/") && resource.length() > 1) {
			response.addHeader("Cache-Control","max-age=31536000");
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



	public int getPort(int connectorIndex) {
		return getConnectors()[connectorIndex].getLocalPort();
	}


}
