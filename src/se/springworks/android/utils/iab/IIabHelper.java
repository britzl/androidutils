package se.springworks.android.utils.iab;

import java.util.List;

import android.app.Activity;
import android.content.Intent;

public interface IIabHelper {

	/**
	 * Callback that notifies when a consumption operation finishes.
	 */
	public interface OnConsumeFinishedListener {
		/**
		 * Called to notify that a consumption has finished.
		 * 
		 * @param purchase
		 *            The purchase that was (or was to be) consumed.
		 * @param result
		 *            The result of the consumption operation.
		 */
		public void onConsumeFinished(Purchase purchase, IabResult result);
	}

	/**
	 * Callback that notifies when a multi-item consumption operation finishes.
	 */
	public interface OnConsumeMultiFinishedListener {
		/**
		 * Called to notify that a consumption of multiple items has finished.
		 * 
		 * @param purchases
		 *            The purchases that were (or were to be) consumed.
		 * @param results
		 *            The results of each consumption operation, corresponding
		 *            to each sku.
		 */
		public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results);
	}

	/**
	 * Callback that notifies when a purchase is finished.
	 */
	public interface OnIabPurchaseFinishedListener {
		/**
		 * Called to notify that an in-app purchase finished. If the purchase
		 * was successful, then the sku parameter specifies which item was
		 * purchased. If the purchase failed, the sku and extraData parameters
		 * may or may not be null, depending on how far the purchase process
		 * went.
		 * 
		 * @param result
		 *            The result of the purchase.
		 * @param info
		 *            The purchase information (null if purchase failed)
		 */
		public void onIabPurchaseFinished(IabResult result, Purchase info);
	}

	/**
	 * Callback for setup process. This listener's {@link #onIabSetupFinished}
	 * method is called when the setup process is complete.
	 */
	public interface OnIabSetupFinishedListener {
		/**
		 * Called to notify that setup is complete.
		 * 
		 * @param result
		 *            The result of the setup process.
		 */
		public void onIabSetupFinished(IabResult result);
	}

	/**
	 * Listener that notifies when an inventory query operation completes.
	 */
	public interface QueryInventoryFinishedListener {
		/**
		 * Called to notify that an inventory query operation completed.
		 * 
		 * @param result
		 *            The result of the operation.
		 * @param inv
		 *            The inventory.
		 */
		public void onQueryInventoryFinished(IabResult result, Inventory inv);
	}

	/**
	 * Starts the setup process. This will start up the setup process
	 * asynchronously. You will be notified through the listener when the setup
	 * process is complete. This method is safe to call from a UI thread.
	 * 
	 * @param listener
	 *            The listener to notify when the setup process is complete.
	 */
	void startSetup(final OnIabSetupFinishedListener listener);

	/** Returns whether subscriptions are supported. */
	boolean subscriptionsSupported();

	void launchPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener);

	void launchPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener,
			String extraData);

	void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode,
			OnIabPurchaseFinishedListener listener);

	void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode,
			OnIabPurchaseFinishedListener listener, String extraData);

	/**
	 * Initiate the UI flow for an in-app purchase. Call this method to initiate
	 * an in-app purchase, which will involve bringing up the Google Play
	 * screen. The calling activity will be paused while the user interacts with
	 * Google Play, and the result will be delivered via the activity's
	 * {@link android.app.Activity#onActivityResult} method, at which point you
	 * must call this object's {@link #handleActivityResult} method to continue
	 * the purchase flow. This method MUST be called from the UI thread of the
	 * Activity.
	 * 
	 * @param act
	 *            The calling activity.
	 * @param sku
	 *            The sku of the item to purchase.
	 * @param itemType
	 *            indicates if it's a product or a subscription (ITEM_TYPE_INAPP
	 *            or ITEM_TYPE_SUBS)
	 * @param requestCode
	 *            A request code (to differentiate from other responses -- as in
	 *            {@link android.app.Activity#startActivityForResult}).
	 * @param listener
	 *            The listener to notify when the purchase process finishes
	 * @param extraData
	 *            Extra data (developer payload), which will be returned with
	 *            the purchase data when the purchase completes. This extra data
	 *            will be permanently bound to that purchase and will always be
	 *            returned when the purchase is queried.
	 */
	void launchPurchaseFlow(Activity act, String sku, String itemType, int requestCode,
			OnIabPurchaseFinishedListener listener, String extraData);

	/**
	 * Handles an activity result that's part of the purchase flow in in-app
	 * billing. If you are calling {@link #launchPurchaseFlow}, then you must
	 * call this method from your Activity's {@link android.app.Activity
	 * @onActivityResult} method. This method MUST be called from the UI thread
	 * of the Activity.
	 * 
	 * @param requestCode
	 *            The requestCode as you received it.
	 * @param resultCode
	 *            The resultCode as you received it.
	 * @param data
	 *            The data (Intent) as you received it.
	 * @return Returns true if the result was related to a purchase flow and was
	 *         handled; false if the result was not related to a purchase, in
	 *         which case you should handle it normally.
	 */
	boolean handleActivityResult(int requestCode, int resultCode, Intent data);
	
	/**
	 * Queries the inventory. This will query all owned items from the server,
	 * as well as information on additional skus, if specified. This method may
	 * block or take long to execute. Do not call from a UI thread. For that,
	 * use the non-blocking version {@link #refreshInventoryAsync}.
	 * 
	 * @param querySkuDetails
	 *            if true, SKU details (price, description, etc) will be queried
	 *            as well as purchase information.
	 * @param moreItemSkus
	 *            additional PRODUCT skus to query information on, regardless of
	 *            ownership. Ignored if null or if querySkuDetails is false.
	 * @param moreSubsSkus
	 *            additional SUBSCRIPTIONS skus to query information on,
	 *            regardless of ownership. Ignored if null or if querySkuDetails
	 *            is false.
	 * @throws IabException
	 *             if a problem occurs while refreshing the inventory.
	 */
	Inventory queryInventory(boolean querySkuDetails, List<String> moreItemSkus, List<String> moreSubsSkus)
			throws IabException;
	
	/**
	 * Asynchronous wrapper for inventory query. This will perform an inventory
	 * query as described in {@link #queryInventory}, but will do so
	 * asynchronously and call back the specified listener upon completion. This
	 * method is safe to call from a UI thread.
	 * 
	 * @param querySkuDetails
	 *            as in {@link #queryInventory}
	 * @param moreSkus
	 *            as in {@link #queryInventory}
	 * @param listener
	 *            The listener to notify when the refresh operation completes.
	 */
	void queryInventoryAsync(final boolean querySkuDetails, final List<String> moreSkus,
			final QueryInventoryFinishedListener listener);
	
	void queryInventoryAsync(QueryInventoryFinishedListener listener);
	
	void queryInventoryAsync(boolean querySkuDetails, QueryInventoryFinishedListener listener);
	
	/**
	 * Consumes a given in-app product. Consuming can only be done on an item
	 * that's owned, and as a result of consumption, the user will no longer own
	 * it. This method may block or take long to return. Do not call from the UI
	 * thread. For that, see {@link #consumeAsync}.
	 * 
	 * @param itemInfo
	 *            The PurchaseInfo that represents the item to consume.
	 * @throws IabException
	 *             if there is a problem during consumption.
	 */
	void consume(Purchase itemInfo) throws IabException;
	
	/**
	 * Asynchronous wrapper to item consumption. Works like {@link #consume},
	 * but performs the consumption in the background and notifies completion
	 * through the provided listener. This method is safe to call from a UI
	 * thread.
	 * 
	 * @param purchase
	 *            The purchase to be consumed.
	 * @param listener
	 *            The listener to notify when the consumption operation
	 *            finishes.
	 */
	void consumeAsync(Purchase purchase, OnConsumeFinishedListener listener);
}
