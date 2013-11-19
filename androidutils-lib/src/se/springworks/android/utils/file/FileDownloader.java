package se.springworks.android.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.threading.AsyncVoidTask;

import com.google.inject.Inject;

public class FileDownloader implements IFileDownloader {
	
	
	private class DownloadTask extends AsyncVoidTask {
		
		private List<OnFileDownloadListener> listeners = new ArrayList<OnFileDownloadListener>();
		
		private String uri;
		
		private String destination;
		
		private IFileHandler fileHandler;
		
		private Exception exception;
		
		public DownloadTask(String uri, String destination, IFileHandler fileHandler) {
			this.uri = uri;
			this.destination = destination;
			this.fileHandler = fileHandler;
		}
		
		public void addListener(OnFileDownloadListener listener) {
			listeners.add(listener);
		}

		@Override
		protected void onPostExecute() {
			logger.debug("onPostExecute() downloaded %s", uri);
			activeTasks.remove(uri);
			for(OnFileDownloadListener listener : listeners) {
				if(exception != null) {
					listener.onFailed(exception, uri);
				}
				else {
					listener.onDownloaded(uri);
				}
			}
		}

		@Override
		protected void performTask() {
			logger.debug("performTask() download %s", uri);
			try {
				InputStream in = httpClient.get(uri);
				if(in != null) {
					fileHandler.save(destination, in);
				}
				else {
					exception = new RuntimeException("No inputstream");
				}
			}
			catch(IOException e) {
				logger.error("performTask() io exception", e);
				exception = e;
			}
		}
	}
	
	@InjectLogger
	private Logger logger;
	
	@Inject
	private ISimpleHttpClient httpClient;
	
	private Map<String, DownloadTask> activeTasks = new HashMap<String, DownloadTask>();
	
	private OnFileDownloadListener downloadListener;
	
	@Override
	public void setOnFileDownloadListener(OnFileDownloadListener listener) {
		this.downloadListener = listener;
	}

	@Override
	public boolean isDownloading(String uri) {
		return activeTasks.containsKey(uri);
	}
	
	
	@Override
	public void download(final String uri, final String destination, final IFileHandler fileHandler) {
		download(uri, destination, fileHandler, downloadListener);
	}


	@Override
	public void download(final String uri, final String destination, final IFileHandler fileHandler, final OnFileDownloadListener listener) {
		logger.debug("download() %s", uri);
		synchronized (activeTasks) {
			DownloadTask task = activeTasks.get(uri);
			if(task != null) {
				logger.debug("download() task already exist for this uri. adding listener");
				task.addListener(listener);
			}
			else {
				logger.debug("download() creating task and downloading %s", uri);
				task = new DownloadTask(uri, destination, fileHandler);
				task.addListener(listener);
				activeTasks.put(uri, task);
				task.execute();
			}
		}
	}

	@Override
	public void cancel(String uri) {
		if(!isDownloading(uri)) {
			return;
		}
		synchronized (activeTasks) {
			DownloadTask task = activeTasks.get(uri);
			task.cancel(true);
			activeTasks.remove(uri);
		}
	}

	@Override
	public void cancelAll() {
		synchronized (activeTasks) {
			for(DownloadTask task : activeTasks.values()) {
				task.cancel(true);
			}
			activeTasks.clear();
		}
	}
}
