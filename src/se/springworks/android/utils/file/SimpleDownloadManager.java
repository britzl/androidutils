package se.springworks.android.utils.file;

import java.lang.ref.WeakReference;

import se.springworks.android.utils.database.CursorUtils;
import se.springworks.android.utils.file.IFileDownloader.OnFileDownloadListener;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import com.google.inject.Inject;

public class SimpleDownloadManager extends BroadcastReceiver {

	@InjectLogger
	private Logger logger;
	
	@Inject
	private Context context;
	
	@Inject
	private DownloadManager downloadManager;
	
	private WeakReference<OnFileDownloadListener> listenerReference;
	
	public void setOnFileDownloadListener(OnFileDownloadListener listener) {
		this.listenerReference = new WeakReference<IFileDownloader.OnFileDownloadListener>(listener);
	}
	
	public void downloadToExternalFiles(Uri uri, String path) {
		if(isDownloading(uri)) {
			return;
		}
		
		Request request = new Request(uri);
		request.setDestinationInExternalFilesDir(context, null, path);
		downloadManager.enqueue(request);
		
		context.registerReceiver(this, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	public void downloadToExternalPublicFiles(Uri uri, String path) {
		if(isDownloading(uri)) {
			return;
		}
		
		Request request = new Request(uri);
		request.setDestinationInExternalPublicDir(null, path);
		downloadManager.enqueue(request);
		
		context.registerReceiver(this, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	public boolean isDownloading(final Uri uri) {
		switch(getDownloadStatus(uri)) {
			case DownloadManager.STATUS_PAUSED:
			case DownloadManager.STATUS_PENDING:
			case DownloadManager.STATUS_RUNNING:
				return true;
			case DownloadManager.STATUS_FAILED:
			case DownloadManager.STATUS_SUCCESSFUL:
			default:
				return false;
		}
	}

	private int getDownloadStatus(Uri uri) {		
		Cursor c = downloadManager.query(new Query());
		int position = CursorUtils.findPosition(c, c.getColumnIndex(DownloadManager.COLUMN_URI), uri.toString());
		if(position != -1)
		{
			c.moveToPosition(position);
			return c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
		}
		return -1;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		logger.debug("onReceive()");
		long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
		Cursor c = downloadManager.query(new Query());
		int position = CursorUtils.findPosition(c, c.getColumnIndex(DownloadManager.COLUMN_ID), id);
		if(position != -1)
		{
			c.moveToPosition(position);
			OnFileDownloadListener listener = this.listenerReference.get();
			if(listener != null) {
				listener.onDownloaded(c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI)));
			}
		}		
	}
}
