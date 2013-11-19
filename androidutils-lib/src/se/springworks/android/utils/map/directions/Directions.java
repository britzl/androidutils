package se.springworks.android.utils.map.directions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Directions {

	
	@JsonProperty("routes")
	private ArrayList<Route> routes;
	
	
	@JsonProperty("status")
	private String status;
	
	
	public List<Route> getRoutes() {
		return routes;
	}
	
	public Route firstRoute() {
		return routes.get(0);
	}
	
	public boolean hasRoutes() {
		return !routes.isEmpty();
	}
	
	public String getStatus() {
		return status;
	}
}
