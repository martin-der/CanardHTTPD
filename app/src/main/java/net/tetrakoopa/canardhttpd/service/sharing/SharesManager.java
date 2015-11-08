package net.tetrakoopa.canardhttpd.service.sharing;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;
import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.domain.metafs.BreadCrumb;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedContact;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedStream;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedText;
import net.tetrakoopa.canardhttpd.service.sharing.exception.AlreadySharedException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.BadShareTypeException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.IncorrectUriException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.NotFoundFromUriException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.NotSharedException;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharesManager {

	private final SharedGroup sharedGroup = new SharedGroup(Uri.EMPTY, "ROOT");

	private final Set<CommonSharedThing.Tag> tags = new HashSet<CommonSharedThing.Tag>();

	public final CommonSharedThing.Tag ST_TAG_PUBLIC = new CommonSharedThing.Tag(CommonSharedThing.Tag.SystemAttribute.PUBLIC);

	private int textIndexSequenceCounter = 0;
	
	public SharesManager() {
		tags.add(ST_TAG_PUBLIC);
	}

	public synchronized SharedThing remove(int index) throws NotSharedException {
		try {
			return sharedGroup.getThings().remove(index);
		} catch (IndexOutOfBoundsException ex) {
			throw new NotSharedException("No element #" + index + " was shared");
		}
	}

	public synchronized void remove(SharedThing thing) throws NotSharedException {
		int index = sharedGroup.getThings().indexOf(thing);
		if (index < 0)
			throw new NotSharedException("No element {'" + thing.getName() + "', " + thing.getClass().getSimpleName() + "} was shared");
		remove(index);
	}

	public synchronized void removeFile(File file) throws NotSharedException {
		SharedFile sharedFile = findFile(file);
		if (sharedFile == null) {
			throw new NotSharedException("File '" + file.getAbsolutePath() + "' was not shared");
		}
		sharedGroup.getThings().remove(sharedFile);
	}

	public synchronized SharedThing add(Context context, Uri uri) throws NotFoundFromUriException, BadShareTypeException, AlreadySharedException {
		final ContentResolver contentResolver = context.getContentResolver();
		final String type = contentResolver.getType(uri);

		if (type == null) {
			throw new BadShareTypeException("Don't know this kind of object : '"+type+"'");
		}

		if (type.equals("vnd.android.cursor.item/contact")) {
			return addContact(contentResolver, uri);
		}
		if (type.startsWith("image/")) {
			return addImage(contentResolver, uri, type);
		}
		throw new BadShareTypeException("Don't know this kind of object : '"+type+"'");

	}

	public synchronized SharedText addText(String name, String text) {
		if (name == null) {
			name = "Some Text";
			if (textIndexSequenceCounter > 0) {
				name = name + " " + textIndexSequenceCounter;
			}
			textIndexSequenceCounter++;
		}
		return addThing(new SharedText(name, text));
	}

	private SharedContact addContact(ContentResolver contentResolver, Uri uri) throws NotFoundFromUriException {
		final Cursor cursor = contentResolver.query(uri, null, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				return addContact(uri, name, id);
			} else {
				throw new NotFoundFromUriException(uri, "Contact");
			}
		} finally {
			cursor.close();
		}
	}
	public synchronized  SharedContact addContact(Uri uri, String name, String id) {
		return addThing(new SharedContact(uri, name, id));
	}

	private SharedStream addImage(ContentResolver contentResolver, Uri uri, String mimeType) throws BadShareTypeException, AlreadySharedException {
		return addStream(contentResolver, uri, mimeType);
	}

	private SharedStream addStream(ContentResolver contentResolver, Uri uri, String mimeType) throws BadShareTypeException, AlreadySharedException {
		final String path = getPath(contentResolver, uri);
		return addThing(new SharedStream(uri, mimeType, path != null ? extractFilename(path) : uri.getPath()));
	}

	public synchronized SharedFile addFile(Uri uri, File file, String mimeType) throws AlreadySharedException, BadShareTypeException {

		if (!file.isFile())
			throw new BadShareTypeException("'" + file.getAbsolutePath() + "' is not a regular file");

		if (findFile(file) != null) {
			throw new AlreadySharedException("File '" + file.getAbsolutePath() + "' is already shared");
		}
		return addThing(new SharedFile(uri, file.getName(), mimeType, null));
	}

	public synchronized SharedDirectory addFolder(Uri uri) throws AlreadySharedException, BadShareTypeException {
		final File file = new File(uri.getPath());

		if (!file.isDirectory())
			throw new BadShareTypeException("'" + file.getAbsolutePath() + "' is not a directory");

		if (findFolder(file) != null) {
			throw new AlreadySharedException("File '" + file.getAbsolutePath() + "' is already shared");
		}
		return addThing(new SharedDirectory(uri, file.getName(), true));
	}

	private synchronized <THING extends SharedThing> THING addThing(THING thing) {
		thing.setShareDate(new Date());
		sharedGroup.getThings().add(thing);
		return thing;
	}

	private static String getPath(ContentResolver contentresolver, Uri uri) {
		// Will return "image:x*"
		final String wholeID = DocumentsContract.getDocumentId(uri);

		// Split at colon, use second item in the array
		final String id = wholeID.split(":")[1];

		final String[] column = { MediaStore.Images.Media.DATA };

		// where id is equal to
		final String sel = MediaStore.Images.Media._ID + "=?";

		final Cursor cursor = contentresolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				column, sel, new String[]{id}, null);
		try {

			int columnIndex = cursor.getColumnIndex(column[0]);

			if (cursor.moveToFirst()) {
				return cursor.getString(columnIndex);
			}
			return null;
		} finally {
			cursor.close();
		}
	}

	public synchronized SharedFile findFile(File file) {
		return findFile(Uri.fromFile(file));
	}

	public synchronized SharedFile findFile(Uri uri) {
		return findSharedFile(uri.toString());
	}

	private synchronized SharedFile findSharedFile(String uri) {
		for (SharedThing sharedThing : sharedGroup.getThings()) {
			if (sharedThing instanceof SharedFile) {
				SharedFile sharedFile = (SharedFile) sharedThing;
				if (sharedFile.getUriAsString().equals(uri))
					return sharedFile;
				continue;
			}
		}
		return null;
	}

	public synchronized SharedDirectory findFolder(File file) {
		return findFolder(Uri.fromFile(file));
	}

	public synchronized SharedDirectory findFolder(Uri uri) {
		return findSharedFolder(uri.toString());
	}

	private synchronized SharedDirectory findSharedFolder(String uri) {
		for (SharedThing sharedThing : sharedGroup.getThings()) {
			if (sharedThing instanceof SharedDirectory) {
				SharedDirectory sharedDirectory = (SharedDirectory) sharedThing;
				if (sharedDirectory.getUriAsString().equals(uri))
					return sharedDirectory;
				continue;
			}
		}
		return null;
	}

	public final List<SharedThing> getThings() {
		return sharedGroup.getThings();
	}
	
	public SharedThing findThingAndBuildBreadCrumb(String url, final BreadCrumb breadCrumb) throws IncorrectUriException {
		
		if (!url.startsWith("/"))
			throw new IncorrectUriException("Urls start with '/'");
		
		url = url.substring(1,url.length());
		
		if ("".equals(url)) {
			return sharedGroup;
		}
		
		SharedThing parentSharedThing = sharedGroup;
		
		final String pathParts [] = url.split("/");
		final List<BreadCrumb.Part> breadCrumbParts = breadCrumb.getParts();
		
		for (String pathPart : pathParts) {
			
			if (parentSharedThing instanceof SharedCollection) 
				throw new IncorrectUriException("'"+breadCrumb.asPath()+"' is not a directory");
			
			final SharedCollection collection =(SharedCollection)parentSharedThing;
			SharedThing match = null;
			
			for (SharedThing possibleMatch : collection.getThings()) {
				if (pathPart.equals(possibleMatch.getName())) {
					match = possibleMatch;
					break;
				}
			}
			
			if (match == null) {
				throw new IncorrectUriException("No element '"+pathPart+" could be found in '"+breadCrumb.asPath()+"'");
			}
			
			breadCrumbParts.add(new BreadCrumb.Part(match));

			parentSharedThing = match;
			
		}
		
		return parentSharedThing;
	}

	private static String extractFilename(String path) {
		int p = path.lastIndexOf('/');
		if (p<0) {
			return path;
		}
		return path.substring(p+1);
	}

}
