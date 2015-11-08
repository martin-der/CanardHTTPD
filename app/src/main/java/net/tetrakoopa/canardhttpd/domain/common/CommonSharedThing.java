package net.tetrakoopa.canardhttpd.domain.common;

import android.net.Uri;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class CommonSharedThing implements SharedThing {

	private final Uri uri;

	private final String name;

	private int usersCount;

	private int stock;

	private Date shareDate;

	private String comment;

	private final Set<Tag> tags = new HashSet<Tag>(); 

	private ShareStatus shareStatus;

	public CommonSharedThing(Uri uri, String name) {
		this.uri = uri;
		this.name = name;
		this.shareStatus = ShareStatus.NOT_SHARED;
	}

	public int getUsersCount() {
		return usersCount;
	}

	public void setUsersCount(int usersCount) {
		this.usersCount = usersCount;
	}

	public Uri getUri() {
		return uri;
	}

	public String getName() {
		return name;
	}

	public Date getShareDate() {
		return shareDate;
	}

	public void setShareDate(Date shareDate) {
		this.shareDate = shareDate;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ShareStatus getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(ShareStatus shareStatus) {
		this.shareStatus = shareStatus;
	}
}
