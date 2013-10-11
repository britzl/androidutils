package se.springworks.android.utils.map.directions;


public interface IDirectionsApi {

//	http://maps.googleapis.com/maps/api/directions/json?origin=Mickelsbergsvägen+354&destination=Sergels+torg&sensor=true&mode=transit&departure_time=1377897244

	public interface OnDirectionsCallback {
		public void onDirections(Directions directions);
		
		public void onError(Throwable t, String error);
	}
	
	void directions(String from, String to, TravelMode mode, long departureTimeSeconds, OnDirectionsCallback callback);
}
