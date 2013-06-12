package se.springworks.android.utils.threading;

import java.util.ArrayList;
import java.util.List;

import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;
import android.os.AsyncTask;


/**
 * Utility to execute a series of async tasks in sequence and trigger a callback
 * once all of the async tasks have completed  
 * @author bjornritzl
 *
 */
public class AsyncSequence {
	
	private static final Logger logger = LoggerFactory.getLogger(AsyncSequence.class);
	
	private enum State {
		IDLE,
		STARTED,
		COMPLETED
	}

	/**
	 * Wrapper for a single async method
	 * @author bjornritzl
	 *
	 */
	public static abstract class AsyncCall {
		
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
	
	private static class AsyncTaskWrapper extends AsyncCall {
		@SuppressWarnings("rawtypes")
		private AsyncTask task;
		private Object params[];
		
		@SuppressWarnings("rawtypes")
		public AsyncTaskWrapper(AsyncTask task, Object... params) {
			this.task = task;
			this.params = params;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void execute(final ICallback executionCallback) {
			task.execute(params);
			try {
				task.get();
				executionCallback.onDone();
			}
			catch (Exception e) {
				e.printStackTrace();
				executionCallback.onError(e);
			}
		}
	}
	
	
	public static abstract class AsyncTaskCall extends AsyncCall {

		private AsyncVoidTask task;
		
		@Override
		public void execute(final ICallback executionCallback) {
			task = new AsyncVoidTask() {
				
				@Override
				protected void performTask() {
					performAsyncTask();
				}
				
				@Override
				protected void onPostExecute() {
					executionCallback.onDone();
				}
			};
			task.execute();
		}
		
		public abstract void performAsyncTask();
	}
	
	private State state = State.IDLE;
	
	private List<AsyncCall> asyncCalls = new ArrayList<AsyncCall>();
	
	private List<ICallback> listeners = new ArrayList<ICallback>();
	
	private String name;
	
	public AsyncSequence() {
		this("");
	}
	
	public AsyncSequence(String name) {
		this.name = name;
	}
	
	/**
	 * Add an async call. The async calls will be made in the order that they are added
	 * @param event
	 */
	public void add(AsyncCall event) {
		if(isIdle()) {
			asyncCalls.add(event);
		}
	}

	@SuppressWarnings("rawtypes")
	public void add(AsyncTask task, Object... params) {
		if(isIdle()) {
			asyncCalls.add(new AsyncTaskWrapper(task, params));
		}
	}
	
	/**
	 * Stops the sequence and removes all calls.
	 * All listeners will be removed (without being called)
	 */
	public synchronized void reset() {
		asyncCalls.clear();
		listeners.clear();
		state = State.IDLE;
	}
	
	/**
	 * Adds a listener to be notified when the sequence is completed
	 * @param listener
	 */
	public synchronized void addListener(ICallback listener) {
		if(!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	/**
	 * Removes a listener
	 * @param listener
	 */
	public synchronized void removeListener(ICallback listener) {
		listeners.remove(listener);
	}
	
	/**
	 * 
	 * @param listener Called once the sequence has completed and every time an error has occurred
	 */
	public synchronized void start(ICallback listener) {
		addListener(listener);
		start();
	}
	
	/**
	 * Start the sequence of async calls.
	 * If the sequence is already started or completed nothing will happen.
	 */
	public synchronized void start() {
		logger.debug("start() %s", name);
		if(isStarted()) {
			logger.debug("start() already started. %s", name);
			return;
		}
		if(isCompleted()) {
			logger.debug("start() already completed. %s", name);
			notifySequenceCompleted();
			return;
		}
		logger.debug("start() starting. %s", name);
		state = State.STARTED;
		executeNext();
	}
	
	public boolean isIdle() {
		return state == State.IDLE;
	}
	
	public boolean isCompleted() {
		return state == State.COMPLETED;
	}
	
	public boolean isStarted() {
		return state == State.STARTED;
	}

	
	/**
	 * Executes the next async call in the sequence
	 */
	private synchronized void executeNext() {
//		logger.debug("executeNext() %s", name);
		if(asyncCalls.isEmpty()) {
//			logger.debug("executeNext() completed %s", name);
			state = State.COMPLETED;
			notifySequenceCompleted();
			return;
		}
		
		final AsyncCall event = asyncCalls.remove(0);
//		logger.debug("executeNext() executing %s for %s", event, name);
		event.execute(new ICallback() {
			
			@Override
			public void onError(Throwable t) {
//				logger.debug("executeNext() onError() %s", name);
				if(event.ignoreErrors) {
					executeNext();
				}
				else {
					notifySequenceError(t);
					executeNext();
				}
			}
			
			@Override
			public void onDone() {
//				logger.debug("executeNext() onDone() %s", name);
				executeNext();
			}
		});
	}
	
	private void notifySequenceCompleted() {
		logger.debug("notifySequenceCompleted() %s", name);
		for(ICallback callback : listeners) {
			callback.onDone();
		}
		listeners.clear();
	}
	
	private void notifySequenceError(Throwable t) {
		for(ICallback callback : listeners) {
			callback.onError(t);
		}
	}
}
