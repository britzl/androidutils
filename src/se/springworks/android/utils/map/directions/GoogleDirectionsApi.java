package se.springworks.android.utils.map.directions;

import java.util.HashMap;
import java.util.Map;

import se.springworks.android.utils.cache.MemCache;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.json.IJsonParser;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.rest.IRestClient;
import se.springworks.android.utils.rest.IRestClient.OnHttpResponseHandler;

import com.google.inject.Inject;

/**
 * https://developers.google.com/maps/documentation/directions
 * 
 * http://maps.googleapis.com/maps/api/directions/json?origin=Mickelsbergsv%C3%A4gen+354&destination=Sergels+torg&sensor=true&mode=transit&departure_time=1377897244
 * 
 * http://stackoverflow.com/questions/9340800/detect-the-nearest-transit-stop-from-the-given-location
 * 
 * @author bjornritzl
 *
 */
public class GoogleDirectionsApi implements IDirectionsApi {
		
	@InjectLogger
	private Logger logger;
	
	@Inject
	private IRestClient rest;
	
	@Inject
	private IJsonParser json;

	// direction request need to be cached or else Google goes bananas 
	private MemCache<Directions> cache = new MemCache<Directions>();
	
	@Override
	public void directions(String from, String to, TravelMode mode, long departureTimeSeconds, final OnDirectionsCallback callback) {
		final String cacheKey = from + to;
		final Directions cachedData = cache.get(cacheKey);
		if(cachedData != null) {
			callback.onDirections(cachedData);
			return;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("origin", from);
		params.put("destination", to);
		params.put("sensor", "true");
		params.put("departure_time", Long.toString(departureTimeSeconds));
		switch(mode) {
		default:
			logger.warn("unkown mode %s", mode);
		case DRIVING:
			params.put("mode", "driving");
			break;
		case TRANSIT:
			params.put("mode", "transit");
			break;
		case WALKING:
			params.put("mode", "walking");
			break;
		
		}
		
		rest.get("http://maps.googleapis.com/maps/api/directions/json", params, new OnHttpResponseHandler() {
			
			@Override
			public void onSuccess(String response) {
				Directions d = GoogleDirectionsApi.this.json.fromJson(response, Directions.class);
				cache.cache(cacheKey, d, 30 * 1000);
				callback.onDirections(d);
			}
			
			@Override
			public void onFailure(Throwable t, String response) {
				callback.onError(t, response);
			}
		});
	}
}
