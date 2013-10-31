package se.springworks.android.utils.view;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * A text watcher that will ignore changes less than a certain number of characters
 * and wait a certain number of milliseconds before indicating a text change by calling
 * {@link DelayedTextWatcher#onTextChanged()} 
 * @author bjornritzl
 *
 */
public abstract class DelayedTextWatcher implements TextWatcher, Runnable {

	private Handler handler = new Handler();
	
	private int minimumAcceptedLength;
	private int delayInMillis;
	
	public DelayedTextWatcher(int minimumAcceptedLength, int delayInMillis) {
		super();
		this.minimumAcceptedLength = minimumAcceptedLength;
		this.delayInMillis = delayInMillis;
	}

	@Override
	public final void afterTextChanged(Editable s) {
		// don't accept changes less than a specific lenth
		if(s.length() < minimumAcceptedLength) {
			return;
		}		
		// don't treat this as a text change straight away
		// wait and see if the user writes some more
		handler.removeCallbacks(this);
		handler.postDelayed(this, delayInMillis);
	}

	@Override
	public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public final void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void run() {
		onTextChanged();
	}
	
	protected abstract void onTextChanged();
}
