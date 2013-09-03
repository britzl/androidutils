package se.springworks.android.utils.map.directions;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Line {

	public static class Agency {
		@JsonProperty("name")
		private String name;
		
		@JsonProperty("url")
		private String url;
		
		public String getName() {
			return name;
		}
		
		public String getUrl() {
			return url;
		}
	}
	
	public static class Vehicle {
		
		public enum Type {
			RAIL, //	Rail.
			METRO_RAIL, //	Light rail transit.
			SUBWAY, //	Underground light rail.
			TRAM, //	Above ground light rail.
			MONORAIL, //	Monorail.
			HEAVY_RAIL, //	Heavy rail.
			COMMUTER_TRAIN, //	Commuter rail.
			HIGH_SPEED_TRAIN, //	High speed train.
			BUS, //	Bus.
			INTERCITY_BUS, //	Intercity bus.
			TROLLEYBUS, //	Trolleybus.
			SHARE_TAXI, //	Share taxi is a kind of bus with the ability to drop off and pick up passengers anywhere on its route.
			FERRY, //	Ferry.
			CABLE_CAR, //	A vehicle that operates on a cable, usually on the ground. Aerial cable cars may be of the type GONDOLA_LIFT.
			GONDOLA_LIFT, //	An aerial cable car.
			FUNICULAR, //	A vehicle that is pulled up a steep incline by a cable. A Funicular typically consists of two cars, with each car acting as a counterweight for the other.
			OTHER //	All other vehicles will return this type.			
		}
		
		@JsonProperty("icon")
		private String icon;
		
		@JsonProperty("name")
		private String name;
		
		@JsonProperty("type")
		private String type;
		
		public String getIcon() {
			return icon;
		}
		
		public String getName() {
			return name;
		}
		
		public Type getType() {
			try {
				return Type.valueOf(type);
			}
			catch(IllegalArgumentException e) {
				return Type.OTHER;
			}
		}
	}
	
	@JsonProperty("agencies")
	private ArrayList<Agency> agencies;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("short_name")
	private String shortName;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("vehicle")
	private Vehicle vehicle;
	
	
	public Iterable<Agency> getAgencies() {
		return agencies;
	}
	
	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public String getUrl() {
		return url;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	
}
