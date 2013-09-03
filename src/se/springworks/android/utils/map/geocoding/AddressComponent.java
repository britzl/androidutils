package se.springworks.android.utils.map.geocoding;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressComponent {

	@JsonProperty("long_name")
	private String longName;
	
	@JsonProperty("short_name")
	private String shortName;
	
	@JsonProperty("types")
	private ArrayList<String> types;
	
	public String getLongName() {
		return longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public Iterable<String> getTypes() {
		return types;
	}
	
}
