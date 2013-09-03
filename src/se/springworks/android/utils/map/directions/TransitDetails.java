package se.springworks.android.utils.map.directions;

import se.springworks.android.utils.map.LatLng;
import se.springworks.android.utils.map.directions.Leg.Time;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransitDetails {

	public static class Station {
		@JsonProperty("location")
		private LatLng location;
		
		@JsonProperty("name")
		private String name;
	}
	
	@JsonProperty("arrival_stop")
	private Station arrivalStop;
	
	@JsonProperty("arrival_time")
	private Time arrivalTime;
	
	@JsonProperty("departure_stop")
	private Station departureStop;
	
	@JsonProperty("departure_time")
	private Time departureTime;
	
	@JsonProperty("headsign")
	private String headsign;
	
	@JsonProperty("headway")
	private int headway;
	
	@JsonProperty("num_stops")
	private int numberOfStops;
	
	@JsonProperty("line")
	private Line line;
	
	public Station getArrivalStop() {
		return arrivalStop;
	}
	
	public Time getArrivalTime() {
		return arrivalTime;
	}
	
	public Station getDepartureStop() {
		return departureStop;
	}
	
	public Time getDepartureTime() {
		return departureTime;
	}
	
	/**
	 * Get the direction in which to travel on this line, as it is marked on
	 * the vehicle or at the departure stop. This will often be the terminus station.
	 * @return
	 */
	public String getHeadsign() {
		return headsign;
	}
	
	/**
	 * Get the expected number of seconds between departures from the same stop
	 * at this time. For example, with a headway value of 600, you would expect a ten
	 * minute wait if you should miss your bus.
	 * @return
	 */
	public int getHeadway() {
		return headway;
	}

	/**
	 * Get the number of stops in this step, counting the arrival stop, but not the
	 * departure stop. For example, if your directions involve leaving from Stop A,
	 * passing through stops B and C, and arriving at stop D, will return 3.
	 * @return
	 */
	public int getNumberOfStops() {
		return numberOfStops;
	}
	
	/**
	 * Get information about the transit line used in this step,
	 * @return
	 */
	public Line getLine() {
		return line;
	}
	
}
