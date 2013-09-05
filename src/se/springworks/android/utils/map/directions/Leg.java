package se.springworks.android.utils.map.directions;

import java.util.ArrayList;
import java.util.Date;

import se.springworks.android.utils.map.LatLng;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Each element in the legs array specifies a single leg of the journey from the origin
 * to the destination in the calculated route. For routes that contain no waypoints,
 * the route will consist of a single "leg," but for routes that define one or more
 * waypoints, the route will consist of one or more legs, corresponding to the specific
 * legs of the journey.
 * @author bjornritzl
 *
 */
public class Leg {

	
	public static class Time {
		
		@JsonProperty("text")
		private String text;
		
		@JsonProperty("time_zone")
		private String timeZone;
		
		@JsonProperty("value")
		private long value;
		
		public Date getDate() {
			return new Date(value * 1000);
		}
		
		public String getTimeZone() {
			return timeZone;
		}
		
		public String getText() {
			return text;
		}
		
	}
	
	
	@JsonProperty("arrival_time")
	private Time arrivalTime;
	
	@JsonProperty("departure_time")
	private Time departureTime;
	
	@JsonProperty("distance")
	private Distance distance;
	
	@JsonProperty("duration")
	private Duration duration;
	
	@JsonProperty("duration_in_traffic")
	private Duration durationInTraffic;
	
	@JsonProperty("end_address")
	private String endAddress;
	
	@JsonProperty("end_location")
	private LatLng endLocation;
	
	@JsonProperty("start_address")
	private String startAddress;
	
	@JsonProperty("start_location")
	private LatLng startLocation;
	
	@JsonProperty("steps")
	private ArrayList<Step> steps;
	
	
	/**
	 * Get the estimated time of arrival for this leg. This property
	 * is only returned for transit directions
	 * @return
	 */
	public Time getArrivalTime() {
		return arrivalTime;
	}
	
	/**
	 * Get the estimated time of departure for this leg. This is only
	 * available for transit directions
	 * @return
	 */
	public Time getDepartureTime() {
		return departureTime;
	}
	
	/** 
	 * Get the total distance covered by this leg
	 * @return
	 */
	public Distance getDistance() {
		return distance;
	}
	
	/**
	 * Get the total duration of this leg
	 * @return
	 */
	public Duration getDuration() {
		return duration;
	}
	
	/**
	 * Get the total duration of this leg, taking into account current
	 * traffic conditions. The duration in traffic will only be returned
	 * if all of the following are true:
	 * 
	 * - The directions request includes a departure_time parameter set to
	 *   a value within a few minutes of the current time.
	 * - The request includes a valid Maps for Business client and signature
	 *   parameter.
	 * - Traffic conditions are available for the requested route.
	 * - The directions request does not include stopover waypoints.
	 * 
	 * @return
	 */
	public Duration getDurationInTraffic() {
		return durationInTraffic;
	}
	
	/**
	 * Get the human-readable address (typically a street address) reflecting
	 * the end location of this leg
	 * @return
	 */
	public String getEndAddress() {
		return endAddress;
	}
	
	/**
	 * Get the latitude/longitude coordinates of the given destination of this
	 * leg. Because the Directions API calculates directions between locations
	 * by using the nearest transportation option (usually a road) at the start
	 * and end points, end location may be different than the provided
	 * destination of this leg if, for example, a road is not near the
	 * destination
	 * @return
	 */
	public LatLng getEndLocation() {
		return endLocation;
	}
	
	/**
	 * Get the human-readable address (typically a street address) reflecting the
	 * start location of this leg
	 * @return
	 */
	public String getStartAddress() {
		return startAddress;
	}
	
	/**
	 * Get the latitude/longitude coordinates of the origin of this leg. Because
	 * the Directions API calculates directions between locations by using the
	 * nearest transportation option (usually a road) at the start and end points,
	 * start location may be different than the provided origin of this leg if,
	 * for example, a road is not near the origin.
	 * @return
	 */
	public LatLng getStartLocation() {
		return startLocation;
	}
	
	/**
	 * Get an array of steps denoting information about each separate step
	 * of the leg of the journey.
	 * @return
	 */
	public Iterable<Step> getSteps() {
		return steps;
	}
}
