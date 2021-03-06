package net.tetrakoopa.canardhttpd.service.http;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;
import net.tetrakoopa.canardhttpd.CanardHTTPDService;
import net.tetrakoopa.canardhttpd.R;
import net.tetrakoopa.canardhttpd.domain.EventLog;
import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.domain.metafs.BreadCrumb;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedText;
import net.tetrakoopa.canardhttpd.service.CanardLogger;
import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.DirectoryWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.GroupWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.TextWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.GenericStreamWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.ImageWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.VCardWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.sharedthing.file.specific.parent.SpecificSerialisedWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.template.ContentWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.template.PageWriter;
import net.tetrakoopa.canardhttpd.service.http.writer.template.TemplateArg;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.service.sharing.exception.NoSuchThingSharedException;
import net.tetrakoopa.canardhttpd.util.TemporaryMimeTypeUtil;
import net.tetrakoopa.canardhttpd.util.WebUtil;
import net.tetrakoopa.mdu.util.FileUtil;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
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

	private final List<SpecificSerialisedWriter> typeMimeWriters = new ArrayList<>();

	private final CanardHTTPDService service;
	private final CanardLogger logger;
	private final SharesManager sharesManager;

	private TextWriter textWriter;
	private GroupWriter groupWriter;
	private DirectoryWriter directoryWriter;
	private GenericStreamWriter genericWriter;

	private String encoding = CommonHTMLComponent.DEFAULT_ENCODING;
	
	private static final int MAX_IDLE_TIME = 30000;


	private final Map<HttpRequest, SharedThing> downloads = new HashMap<HttpRequest, SharedThing>();

	private final String title;



	public CanardHTTPD(CanardHTTPDService service, SharesManager sharesManager, String hostname, int port, int sercurePort, String keyStorePath, String keyStorePassord) {
		this.sharesManager = sharesManager;
		this.service = service;
		this.logger = service.getLogger();


		this.title = this.service.getString(R.string.app_name);

		init_Jetty_v8(port, sercurePort, keyStorePath, keyStorePassord);
	}

	private void makeWriters(String httpContext) {
		this.directoryWriter = new DirectoryWriter(service, httpContext);
		this.groupWriter = new GroupWriter(service, httpContext);
		this.textWriter = new TextWriter(service, httpContext);
		this.genericWriter = new GenericStreamWriter(service, httpContext);


		this.typeMimeWriters.add(new VCardWriter(service, httpContext));
		this.typeMimeWriters.add(new ImageWriter(service, httpContext));

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
			logger.i(EventLog.Type.WEBUSER_DOWNLOAD_STARTED, new Date(), request.getRemoteAddr(), request.getRequestURI());
			final long begin = System.nanoTime();
			try {
				serve(request, response);
				((Request) request).setHandled(true);
				final long end = System.nanoTime();
				logger.d(EventLog.Type.WEBUSER_DOWNLOAD_COMPLETED, new Date(), request.getRemoteAddr(), request.getRequestURI());
			} catch (Exception ex) {
				serveInternalErrorResponse(request, response, ex);
				final long end = System.nanoTime();
				logger.e(EventLog.Type.WEBUSER_DOWNLOAD_FAILED, new Date(), request.getRemoteAddr(), request.getRequestURI());
			}
		}
	};

	protected InputStream getAsset(String asset) throws IOException {
		return service.getAssets().open(asset);
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


		if (uri.startsWith("/~/_/")) {
			serveDynamicResource(request, response, uri.substring(4));
			return;
		}
		if (uri.startsWith("/~/")) {
			serveWWWStaticResource(request, response, uri.substring(2));
			return;
		}

		if (uri.startsWith("/")) {
			if ( ! serveThing(request, response)) {
				Log.i(CanardHTTPDActivity.TAG, "Could not handle url '" + uri +"'");
			}
			return;
		}

		Log.i(CanardHTTPDActivity.TAG, "Could not handle url '" + uri + "'");
	}

	private boolean serveThing(final HttpServletRequest request, final HttpServletResponse response) throws IOException {


		final String contextPath = request.getContextPath() == null ? "" : request.getContextPath();

		final String uri = request.getRequestURI();

		response.setContentType("text/html");
		response.setCharacterEncoding(CommonHTMLComponent.DEFAULT_ENCODING);
		response.setStatus(HttpServletResponse.SC_OK);


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

		final ContentWriter contentWriter = new ContentWriter(service, contextPath) {

			@Override
			protected void writeThing(Writer destination, SharedThing thing) throws IOException {
				CanardHTTPD.this.writeThing(destination, thing, uri);
			}
		};

		final PageWriter pageWriter = new PageWriter(service, contextPath, request) {
			@Override
			protected void writeThemeName(HttpServletRequest request, Writer destination, TemplateArg arg) throws IOException {
				destination.write(themeName);
			}
			@Override
			protected void writeHead(HttpServletRequest request, Writer destination, TemplateArg arg) throws IOException {
				if (isMobileUser) {
					destination.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n");
					final String title = uri;
					destination.write("<title>" + escapedXml((title == null ? CanardHTTPD.this.title : (title + " - " + CanardHTTPD.this.title))) + "</title></head>");
				}
			}

			@Override
			protected void writeHeader(HttpServletRequest request, Writer destination, TemplateArg arg) throws IOException {
				writeAsset("www/template/piece/header.html", destination);
			}

			@Override
			protected void writeContent(HttpServletRequest request, Writer destination, TemplateArg arg) throws IOException {

				try {
					final BreadCrumb breadCrumb = new BreadCrumb();
					final SharedThing thing;
					try {
						thing = sharesManager.findThingAndBuildBreadCrumb(uri, breadCrumb);
					} catch (NoSuchThingSharedException e) {
						destination.write("Nothing at <span>"+escapedXmlAlsoSpace(uri)+"</span>");
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
					contentWriter.write(destination, thing, breadCrumb);

				} catch (IOException ioex) {
					Log.e(CanardHTTPDService.TAG, "Failed to write thing content (uri='" + uri + "') : " + ioex.getMessage());
				}
			}

			@Override
			protected void writeFooter(HttpServletRequest request, Writer destination, TemplateArg arg) throws IOException {
				writeAsset("www/template/piece/footer.html", destination);
			}
		};


		final PrintWriter writer = new PrintWriter(response.getOutputStream());

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
			SpecificSerialisedWriter specificWriter = null;
			if (sharedFile.getMimeType() != null) {
				specificWriter = SpecificSerialisedWriter.getBestHandler(sharedFile.getMimeType(), typeMimeWriters);
			}
			if (specificWriter != null) {
				specificWriter.write(writer, url, sharedFile);
			} else {
				Log.w(CanardHTTPDActivity.TAG, "No handler found for file with mime '" + sharedFile.getMimeType() + "'");
				//genericWriter.write(writer, url, sharedFile);
			}
		} else if (thing instanceof SharedStream) {
			final SharedStream sharedStream = (SharedStream)thing;
			SpecificSerialisedWriter specificWriter = null;
			if (sharedStream.getMimeType() != null) {
				specificWriter = SpecificSerialisedWriter.getBestHandler(sharedStream.getMimeType(), typeMimeWriters);
			}
			if (specificWriter != null) {
				specificWriter.write(writer, url, sharedStream);
			} else {
				Log.w(CanardHTTPDActivity.TAG, "No handler found for file with mime '" + sharedStream.getMimeType() + "'");
				genericWriter.write(writer, url, sharedStream);
			}
		}
		return true;
	}

	private void serveDynamicResource(HttpServletRequest request, HttpServletResponse response, String resource) throws IOException {
		if (resource.startsWith("/") && resource.length() > 1) {
			final Uri uri = Uri.parse(CommonHTMLComponent.unescapeFromUrl(resource.substring(1)));
			final ContentResolver contentResolver = service.getContentResolver();
			final InputStream input;
			try {
				input = contentResolver.openInputStream(uri);
			} catch(FileNotFoundException fnfex) {
				serveNotFoundResponse(request, response);
				return;
			}
			final String mimeType = contentResolver.getType(uri);
			if (mimeType != null) {
				response.setContentType(mimeType);
			}
			final long length = input.available();
			if (length > 0) {
				response.setHeader("Content-Length", String.valueOf(length));
			}
			makeResponseCached(response);
			FileUtil.copy(input, response.getOutputStream());
		} else {
			serveNotFoundResponse(request, response);
		}
	}


	private void serveWWWStaticResource(HttpServletRequest request, HttpServletResponse response, String resource) throws IOException {
		if (resource.startsWith("/") && resource.length() > 1) {
			makeResponseCached(response);
			final String asset = resource.substring(1);
			final String mimeType = TemporaryMimeTypeUtil.getMimeType(asset);
			serveAssetOrNotFound(request, response, "www/public-resources/" + asset, mimeType);
		} else {
			serveNotFoundResponse(request, response);
		}
	}
	private static void makeResponseCached(HttpServletResponse response) {
		response.addHeader("Cache-Control","max-age=31536000");
	}
	public static void serveNotFoundResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		serveText(request, response, HttpServletResponse.SC_NOT_FOUND, MIME_HTML, "<h1>Error 404 : Not found</h1>");
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
