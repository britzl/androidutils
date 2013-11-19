package se.springworks.android.utils.map.geocoding;

import java.util.ArrayList;

import se.springworks.android.utils.map.LatLng;

import android.location.Address;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoCodeResult {

	@JsonProperty("address_components")
	private ArrayList<AddressComponent> addressComponents;
	
	@JsonProperty("formatted_address")
	private String formattedAddress;
	
	@JsonProperty("geometry")
	private Geometry geometry;
	
	@JsonProperty("types")
	private ArrayList<String> types;
	
	public Iterable<AddressComponent> getAddressComponents() {
		return addressComponents;
	}
	
	/**
	 * Get a string containing the human-readable address of this location.
	 * Often this address is equivalent to the "postal address," which
	 * sometimes differs from country to country
	 * @return
	 */
	public String getFormattedAddress() {
		return formattedAddress;
	}
	
	public Geometry getGeometry() {
		return geometry;
	}
	
	public Iterable<String> getTypes() {
		return types;
	}
	
	
	public static GeoCodeResult fromAddress(Address a) {
		GeoCodeResult result = new GeoCodeResult();
		result.formattedAddress = "";
		for(int i = 0; i < a.getMaxAddressLineIndex(); i++) {
			result.formattedAddress += a.getAddressLine(i) + " ";
		}
		result.geometry = new Geometry(new LatLng(a.getLatitude(), a.getLongitude()));
		return result;
	}
	
}
