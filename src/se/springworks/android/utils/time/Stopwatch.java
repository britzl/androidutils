package se.springworks.android.utils.time;


public class Stopwatch {

	enum State {
		RUNNING,
		STOPPED
	}
	
	private State state = State.STOPPED;
	
	private long startTime;
	
	private long runningTime = 0;
	
	public Stopwatch() {
		
	}
	
	public Stopwatch(long runningTime) {
		this.runningTime = runningTime;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
		state = State.RUNNING;
	}
	
	public void stop() {
		if(state != State.RUNNING) {
			return;
		}
		runningTime += System.currentTimeMillis() - startTime;
		state = State.STOPPED;
	}
	
	public void reset() {
		state = State.STOPPED;
		runningTime = 0;	
	}
	
	public boolean isRunning() {
		return state == State.RUNNING;
	}

	public long getRunningTime() {
		if(state == State.RUNNING) {
			return runningTime + (System.currentTimeMillis() - startTime);
		}
		return runningTime;
	}
}
