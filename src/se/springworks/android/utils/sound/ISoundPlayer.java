package se.springworks.android.utils.sound;

public interface ISoundPlayer {
	public void setVolume(float volume);
	
	public void add(int resId, Object key);
	
	public void remove(Object key);
	
	public void play(Object key);
	
	public void loop(Object key);
	
	public void loop(Object key, int loops);
	
	public void stop(Object key);
}
