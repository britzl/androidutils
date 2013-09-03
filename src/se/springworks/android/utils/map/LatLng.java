package se.springworks.android.utils.map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatLng {
	@JsonProperty("lat")
	private double latitude;
	
	@JsonProperty("lng")
	private double longitude;

	public LatLng() {
		
	}
	
	public LatLng(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}
