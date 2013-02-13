package se.springworks.android.utils.sound;

import android.content.res.AssetFileDescriptor;

public interface ISoundPlayer {
	
	/**
	 * Sets a global volume for all sounds played. Note that this is a percentage of the
	 * current max volume as set by the system
	 * @param volume
	 */
	public void setVolume(float volume);
	
	/**
	 * Adds a sound and associates it with the specified key. If a sound is already associated with
	 * the key nothing happens (use {@link #replace(int, Object)} to overwrite any existing sound)
	 * @param resId
	 * @param key
	 */
	public void add(int resId, Object key);
	public void add(AssetFileDescriptor afd, Object key);
	public void add(String path, Object key);

	/**
	 * Replaces a sound. If no sound is associated with the specified key the
	 * new sound will be added to the key
	 * @param resId
	 * @param key
	 */
	public void replace(int resId, Object key);
	public void replace(AssetFileDescriptor afd, Object key);
	public void replace(String path, Object key);
	
	/**
	 * Removes a sound
	 * @param key
	 */
	public void remove(Object key);
	
	/**
	 * Plays a sound one time
	 * @param key
	 */
	public void play(Object key);
	
	/**
	 * Plays a sound one time
	 * @param key
	 * @param volume
	 */
	public void play(Object key, float volume);
	
	/**
	 * Loops a sound forever
	 * @param key
	 */
	public void loop(Object key);
	
	/**
	 * Loops a sound a specific number of times
	 * @param key
	 * @param loops Number of times to loop the sound (0 to play once, -1 to play forever)
	 */
	public void loop(Object key, int loops);
	
	/**
	 * Stops a specific sound
	 * @param key
	 */
	public void stop(Object key);
	
	/**
	 * Pauses all active sounds
	 */
	public void pause();
	
	/**
	 * Resumes all paused sounds
	 */
	public void resume();
}
