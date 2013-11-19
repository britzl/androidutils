package se.springworks.android.utils.map;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Bounds {
	@JsonProperty("northeast")
	private LatLng northEastE6;

	@JsonProperty("southwest")
	private LatLng southWestE6;
}