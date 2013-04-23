package se.springworks.android.utils.threading;

import java.util.ArrayList;
import java.util.List;

public class AsyncSequence {
	
	private enum State {
		WAITING,
		STARTED,
		COMPLETED
	}

	public static abstract class AsyncEvent implements ICallback {
		
		private AsyncSequence sequence;

		public abstract void execute();
		
		@Override
		public final void onDone() {
			sequence.executeNextTask();
		}
		
		@Override
		public final void onError(Throwable t) {
			sequence.onTaskError(t);
		}
	}
	
	private State state = State.WAITING;
	
	private List<AsyncEvent> asyncEvents = new ArrayList<AsyncEvent>();
	
	private ICallback callback;
	
	public void addAsyncEvent(AsyncEvent event) {
		event.sequence = this;
		asyncEvents.add(event);
	}
	
	public void stop() {
		asyncEvents.clear();
		state = State.COMPLETED;
	}
	
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
		executeNextTask();
	}
	
	private void onTaskError(Throwable t) {
		callback.onError(t);
		executeNextTask();
	}
	
	private void executeNextTask() {
		if(asyncEvents.isEmpty()) {
			state = State.COMPLETED;
			callback.onDone();
			return;
		}
		
		AsyncEvent event = asyncEvents.remove(0);
		event.execute();
	}
}
