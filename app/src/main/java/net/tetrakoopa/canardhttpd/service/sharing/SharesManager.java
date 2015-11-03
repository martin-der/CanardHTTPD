package net.tetrakoopa.canardhttpd.service.sharing;

import android.net.Uri;

import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;
import net.tetrakoopa.canardhttpd.domain.common.SharedCollection;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.domain.metafs.BreadCrumb;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedGroup;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedText;
import net.tetrakoopa.canardhttpd.service.sharing.exception.AlreadySharedException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.BadShareTypeException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.IncorrectUrlException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.NotSharedException;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharesManager {

	private final SharedGroup sharedGroup = new SharedGroup("ROOT");

	private final Set<CommonSharedThing.Tag> tags = new HashSet<CommonSharedThing.Tag>();

	public final CommonSharedThing.Tag ST_TAG_PUBLIC = new CommonSharedThing.Tag(CommonSharedThing.Tag.SystemAttribute.PUBLIC);

	private int textIndexSequenceCounter = 0;
	
	public SharesManager() {
		tags.add(ST_TAG_PUBLIC);
	}

	public synchronized void addText(String name, String text) {
		if (name == null) {
			name = "Some Text";
			if (textIndexSequenceCounter > 0) {
				name = name + " " + textIndexSequenceCounter;
			}
			textIndexSequenceCounter++;
		}
		addThing(new SharedText(name, text));
	}

	public synchronized SharedThing remove(int index) throws NotSharedException {
		try {
			return sharedGroup.getThings().remove(index);
		} catch (IndexOutOfBoundsException ex) {
			throw new NotSharedException("No element #" + index + " was shared");
		}
	}

	public synchronized SharedThing remove(SharedThing thing) throws NotSharedException {
		int index = sharedGroup.getThings().indexOf(thing);
		if (index < 0)
			throw new NotSharedException("No element {'" + thing.getName() + "', " + thing.getClass().getSimpleName() + "} was shared");
		return remove(index);
	}

	public synchronized void removeFile(File file) throws NotSharedException {
		SharedFile sharedFile = findFile(file);
		if (sharedFile == null) {
			throw new NotSharedException("File '" + file.getAbsolutePath() + "' was not shared");
		}
		sharedGroup.getThings().remove(sharedFile);
	}

	public synchronized void addInode(File file, String mimeType)
			throws AlreadySharedException, BadShareTypeException {

		throw new BadShareTypeException("Don't know what to do with '" + file.getAbsolutePath() + "'");
	}

	public synchronized void addFile(File file, String mimeType) throws AlreadySharedException, BadShareTypeException {

		if (!file.isFile())
			throw new BadShareTypeException("'" + file.getAbsolutePath() + "' is not a regular file");

		if (findFile(file) != null) {
			throw new AlreadySharedException("File '" + file.getAbsolutePath() + "' is already shared");
		}
		addThing(new SharedFile(file.getName(), file, mimeType, null));
	}

	public synchronized void addFolder(File file) throws AlreadySharedException, BadShareTypeException {
		
		if (!file.isDirectory())
			throw new BadShareTypeException("'" + file.getAbsolutePath() + "' is not a directory");

		if (findFolder(file) != null) {
			throw new AlreadySharedException("File '" + file.getAbsolutePath() + "' is already shared");
		}
		addThing(new SharedDirectory(file.getName(), file, true));
	}

	private synchronized void addThing(SharedThing thing) {
		thing.setShareDate(new Date());
		sharedGroup.getThings().add(thing);
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
	
	public SharedThing findThingAndBuildBreadCrumb(String url, final BreadCrumb breadCrumb) throws IncorrectUrlException {
		
		if (!url.startsWith("/"))
			throw new IncorrectUrlException("Urls start with '/'");
		
		url = url.substring(1,url.length());
		
		if ("".equals(url)) {
			return sharedGroup;
		}
		
		SharedThing parentSharedThing = sharedGroup;
		
		final String pathParts [] = url.split("/");
		final List<BreadCrumb.Part> breadCrumbParts = breadCrumb.getParts();
		
		for (String pathPart : pathParts) {
			
			if (parentSharedThing instanceof SharedCollection) 
				throw new IncorrectUrlException("'"+breadCrumb.asPath()+"' is not a directory");
			
			final SharedCollection collection =(SharedCollection)parentSharedThing;
			SharedThing match = null;
			
			for (SharedThing possibleMatch : collection.getThings()) {
				if (pathPart.equals(possibleMatch.getName())) {
					match = possibleMatch;
					break;
				}
			}
			
			if (match == null) {
				throw new IncorrectUrlException("No element '"+pathPart+" could be found in '"+breadCrumb.asPath()+"'");
			}
			
			breadCrumbParts.add(new BreadCrumb.Part(match));

			parentSharedThing = match;
			
		}
		
		return parentSharedThing;
	}


}
