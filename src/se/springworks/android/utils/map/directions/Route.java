package se.springworks.android.utils.map.directions;

import java.util.ArrayList;

import se.springworks.android.utils.map.Bounds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Route {

	
	@JsonProperty("bounds")
	private Bounds bounds;
	
	@JsonProperty("copyrights")
	private String copyrights;
	
	@JsonProperty("legs")
	private ArrayList<Leg> legs;
	
	@JsonProperty("overview_polyline")
	private PolyLine overviewPolyline;
	
	@JsonProperty("summary")
	private String summary;
	
	@JsonProperty("warnings")
	private ArrayList<String> warnings;
	
	/**
	 * Get the viewport bounding box of this route
	 * @return
	 */
	public Bounds getBounds() {
		return bounds;
	}
	
	/**
	 * Get the copyrights text to be displayed for this route. You must handle and
	 * display this information yourself
	 * @return
	 */
	public String getCopyrights() {
		return copyrights;
	}
	
	/**
	 * Get an array which contains information about a leg of the route, between two
	 * locations within the given route. A separate leg will be present for each waypoint
	 * or destination specified. (A route with no waypoints will contain exactly one leg
	 * within the legs array.) Each leg consists of a series of steps
	 * @return
	 */
	public Iterable<Leg> getLegs() {
		return legs;
	}
	
	public Leg getFirstLeg() {
		return legs.get(0);
	}
	
	public Leg getLastLeg() {
		return legs.get(legs.size() - 1);
	}
	
	public boolean hasLegs() {
		return !legs.isEmpty();
	}
	
	/**
	 * Get an object holding an array of encoded points that represent an
	 * approximate (smoothed) path of the resulting directions
	 * @return
	 */
	public PolyLine getOverviewPolyline() {
		return overviewPolyline;
	}
	
	/**
	 * Get a short textual description for the route, suitable for naming and disambiguating
	 * the route from alternatives
	 * @return
	 */
	public String getSummary() {
		return summary;
	}
	
	/**
	 * Get an array of warnings to be displayed when showing these directions. You must
	 * handle and display these warnings yourself.
	 * @return
	 */
	public Iterable<String> getWarnings() {
		return warnings;
	}
	
	public long getTotalTime() {
		if(!hasLegs()) {
			return -1;
		}
		Leg first = getFirstLeg();
		Leg last = getLastLeg();
		return last.getArrivalTime().getDate().getTime() - first.getDepartureTime().getDate().getTime();
	}
	
	public Step getFirstStepWithTransitDetails() {
		for(Leg leg : legs) {
			for(Step step : leg.getSteps()) {
				if(step.hasTransitDetails()) {
					return step;
				}
			}
		}
		return null;
	}
	
	public Step getFirstNonWalkingStep() {
		for(Leg leg : legs) {
			for(Step step : leg.getSteps()) {
				if(step.getTravelMode() != TravelMode.WALKING) {
					return step;
				}
			}
		}
		return null;
	}
}
