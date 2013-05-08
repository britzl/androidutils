package se.springworks.android.utils.threading;

import java.util.ArrayList;
import java.util.List;


/**
 * Utility to execute a series of async tasks in sequence and trigger a callback
 * once all of the async tasks have completed  
 * @author bjornritzl
 *
 */
public class AsyncSequence {
	
	private enum State {
		WAITING,
		STARTED,
		COMPLETED
	}

	/**
	 * Wrapper for a single async method
	 * @author bjornritzl
	 *
	 */
	public static abstract class AsyncCall {
		
		private AsyncSequence sequence;
		
		private boolean ignoreErrors = false;

		public AsyncCall() {
			
		}
		
		public AsyncCall(boolean ignoreErrors) {
			this.ignoreErrors = ignoreErrors;
		}
		
		/**
		 * Perform the async task here
		 * @param executionCallback Call this once the async task has completed
		 */
		public abstract void execute(ICallback executionCallback);
	}
	
	private State state = State.WAITING;
	
	private List<AsyncCall> asyncCalls = new ArrayList<AsyncCall>();
	
	private ICallback callback;
	
	/**
	 * Add an async call. The async calls will be made in the order that they are added
	 * @param event
	 */
	public void add(AsyncCall event) {
		event.sequence = this;
		asyncCalls.add(event);
	}
	
	/**
	 * Stops the sequence and removes all calls
	 */
	public void stop() {
		asyncCalls.clear();
		state = State.COMPLETED;
	}
	
	/**
	 * Start the sequence of async calls
	 * @param callback Called once the sequence has completed and every time an error has occurred
	 */
	public void start(final ICallback callback) {
		this.callback = callback;
		if(state == State.STARTED) {
			return;
		}
		if(state == State.COMPLETED) {
			callback.onDone();
			return;
		}
		state = State.STARTED;
		executeNext();
	}

	/**
	 * Executes the next async call in the sequence
	 */
	private void executeNext() {
		if(asyncCalls.isEmpty()) {
			state = State.COMPLETED;
			callback.onDone();
			return;
		}
		
		final AsyncCall event = asyncCalls.remove(0);
		event.execute(new ICallback() {
			
			@Override
			public void onError(Throwable t) {
				if(event.ignoreErrors) {
					executeNext();
				}
				else {
					callback.onError(t);
					executeNext();
				}
			}
			
			@Override
			public void onDone() {
				executeNext();
			}
		});
	}
}
