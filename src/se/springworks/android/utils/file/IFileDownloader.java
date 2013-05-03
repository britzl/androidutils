package se.springworks.android.utils.file;




public interface IFileDownloader {

	public interface OnFileDownloadListener {
		void onDownloaded(String uri);
		void onFailed(Exception e, String uri);
	}
	
	void setOnFileDownloadListener(OnFileDownloadListener listener);
	
	void download(String uri, String destination, IFileHandler fileHandler);
	
	void download(String uri, String destination, IFileHandler fileHandler, OnFileDownloadListener listener);
	
	boolean isDownloading(String uri);
}
