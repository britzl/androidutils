package se.springworks.android.utils.market;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class GooglePlayUtil {

	/**
	 * Show a specific app in Google Play
	 * @param a
	 * @param packageId Unique package id of the app to show
	 */
	public static void showAppInGooglePlay(Activity a, String packageId) {
		try {
			a.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageId)));
		}
		catch (android.content.ActivityNotFoundException anfe) {
			a.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageId)));
		}		
	}

	/**
	 * Show all apps by a publisher
	 * @param a
	 * @param publisherName
	 */
	public static void showPublisherInGooglePlay(Activity a, String publisherName) {
		try {
			a.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + publisherName)));
		}
		catch (android.content.ActivityNotFoundException anfe) {
			a.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:" + publisherName)));
		}		
	}
	
	/**
	 * Perform a generic search in Google Play
	 * @param a
	 * @param searchQuery
	 */
	public static void searchInGooglePlay(Activity a, String searchQuery) {
		try {
			a.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + searchQuery)));
		}
		catch (android.content.ActivityNotFoundException anfe) {
			a.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=<query>" + searchQuery)));
		}				
	}
}
