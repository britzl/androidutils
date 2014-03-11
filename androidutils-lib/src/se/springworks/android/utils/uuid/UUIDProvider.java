package se.springworks.android.utils.uuid;

import java.util.UUID;

public interface UUIDProvider {

	/**
	 *  @return a UUID that may be used to uniquely identify your device for most purposes
	 */
	public UUID get();
}
