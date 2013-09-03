package se.springworks.android.utils.map.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Duration {
	@JsonProperty("text")
	private String text;
	
	@JsonProperty("value")
	private int duration;
	
	public String getText() {
		return text;
	}
	
	public int getDurationInSeconds() {
		return duration;
	}		

}
