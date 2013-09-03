package se.springworks.android.utils.map.geocoding;

import se.springworks.android.utils.map.Bounds;
import se.springworks.android.utils.map.LatLng;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Geometry {

	@JsonProperty("location")
	private LatLng location;
	
	@JsonProperty("location_type")
	private String locationType;
	
	@JsonProperty("viewport")
	private Bounds viewport;
	
	public Geometry() {
		
	}
	
	public Geometry(LatLng location) {
		this.location = location;
	}
	
	public LatLng getLocation() {
		return location;
	}
	
	public Bounds getViewport() {
		return viewport;
	}
	
	public String getLocationType() {
		return locationType;
	}
}
