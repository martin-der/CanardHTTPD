package net.tetrakoopa.mdu.android.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import net.tetrakoopa.mdu.android.exception.MissingNeededException;

public class ContentUtil {

    public static class MediaInfo {
        final String title;
        final String mimeType;
        final long size;
        public MediaInfo(String title,String mimeType,long size) {
            this.title=title;
            this.mimeType=mimeType;
            this.size=size;
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static MediaInfo getMediaInfo(ContentResolver contentresolver, Uri uri) {
        // Will return "image:x*"
        final String wholeID = DocumentsContract.getDocumentId(uri);

        final String id = wholeID.split(":")[1];

        final String[] column = { MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.SIZE };

        final String selector = MediaStore.Images.Media._ID + "=?";

        final Cursor cursor = contentresolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, selector, new String[]{id}, null);
        try {

            final String title = DataBaseUtil.getString(cursor, MediaStore.MediaColumns.TITLE);
            final String mimeType = DataBaseUtil.getString(cursor, MediaStore.MediaColumns.MIME_TYPE);
            final long size = DataBaseUtil.getLong(cursor, MediaStore.MediaColumns.SIZE);

            if (cursor.moveToFirst()) {
                return new MediaInfo(title,mimeType,size);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    /**
     *
     * @return path on external storage
     * @throws MissingNeededException, if path can't be know because of missing 'android.permission.READ_EXTERNAL_STORAGE' permission
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getMediaPath(ContentResolver contentresolver, Uri uri) throws MissingNeededException {
        // Will return "image:x*"
        final String wholeID = DocumentsContract.getDocumentId(uri);

        final String id = wholeID.split(":")[1];

        final String[] column = { MediaStore.MediaColumns.DATA };

        final String selector = MediaStore.Images.Media._ID + "=?";

        try {
            final Cursor cursor = contentresolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, selector, new String[]{id}, null);
            try {

                if (cursor.moveToFirst()) {
                    return DataBaseUtil.getString(cursor, MediaStore.MediaColumns.DATA);
                }
                return null;
            } finally {
                cursor.close();
            }
        }catch(SecurityException securityException) {
            throw new MissingNeededException("android.permission.READ_EXTERNAL_STORAGE");
        }
    }
}
