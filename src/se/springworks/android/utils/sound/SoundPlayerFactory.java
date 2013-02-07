package se.springworks.android.utils.sound;

public interface SoundPlayerFactory {

	public ISoundPlayer create(int maxStreams);
}
