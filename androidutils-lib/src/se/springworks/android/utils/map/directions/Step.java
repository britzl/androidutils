package se.springworks.android.utils.map.directions;

import java.util.ArrayList;

import se.springworks.android.utils.map.LatLng;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Each element in the steps array defines a single step of the calculated
 * directions. A step is the most atomic unit of a direction's route, containing
 * a single step describing a specific, single instruction on the journey.
 * E.g. "Turn left at W. 4th St." The step not only describes the instruction but
 * also contains distance and duration information relating to how this step
 * relates to the following step. For example, a step denoted as "Merge onto
 * I-80 West" may contain a duration of "37 miles" and "40 minutes," indicating
 * that the next step is 37 miles/40 minutes from this step.
 * 
 * When using the Directions API to search for transit directions, the steps
 * array will include additional Transit Details in the form of a transit details
 * array. If the directions include multiple modes of transportation, detailed
 * directions will be provided for walking or driving steps in a sub_steps array.
 * For example, a walking step will include directions from the start and end
 * locations: "Walk to Innes Ave & Fitch St". That step will include detailed
 * walking directions for that route in the sub_steps array, such as: "Head
 * north-west", "Turn left onto Arelious Walker", and "Turn left onto Innes Ave".
 * 
 * @author bjornritzl
 *
 */
public class Step {

	@JsonProperty("distance")
	private Distance distance;
	
	@JsonProperty("duration")
	private Duration duration;
	
	@JsonProperty("end_location")
	private LatLng endLocation;
	
	@JsonProperty("html_instructions")
	private String htmlInstructions;
	
	@JsonProperty("polyline")
	private PolyLine polyLine;
	
	@JsonProperty("start_location")
	private LatLng startLocation;
	
	@JsonProperty("travel_mode")
	private String travelMode;
	
	@JsonProperty("maneuver")
	private String maneuver;
	
	@JsonProperty("transit_details")
	private TransitDetails transitDetails;
	
	@JsonProperty("steps")
	private ArrayList<Step> steps;
	
	
	/**
	 * Get the distance covered by this step until the next step
	 * @return
	 */
	public Distance getDistance() {
		return distance;
	}
	
	/**
	 * Get the typical time required to perform the step, until the next step
	 * @return
	 */
	public Duration getDuration() {
		return duration;
	}
	
	/**
	 * Get the location of the last point of this step
	 * @return
	 */
	public LatLng getEndLocation() {
		return endLocation;
	}
	
	/**
	 * Get the location of the starting point of this step
	 * @return
	 */
	public LatLng getStartLocation() {
		return startLocation;
	}
	
	public PolyLine getPolyLine() {
		return polyLine;
	}
	
	public String getManeuver() {
		return maneuver;
	}
	
	/**
	 * Get formatted instructions for this step, presented as an HTML text string
	 * @return
	 */
	public String getHtmlInstructions() {
		return htmlInstructions;
	}
	
	public Iterable<Step> getSteps() {
		return steps;
	}
	
	public TravelMode getTravelMode() {
		try {
			return TravelMode.valueOf(travelMode);
		}
		catch(IllegalArgumentException e) {
			return TravelMode.DRIVING;
		}
	}
	
	/**
	 * Get transit specific information. Transit details is only returned when {@link Step#getTravelMode()} equals
	 * {@link TravelMode#TRANSIT}
	 * @return Transit details or null
	 */
	public TransitDetails getTransitDetails() {
		return transitDetails;
	}
	
	public boolean hasTransitDetails() {
		return transitDetails != null;
	}
}
