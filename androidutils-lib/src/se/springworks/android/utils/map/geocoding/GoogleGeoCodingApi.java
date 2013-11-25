package se.springworks.android.utils.map.geocoding;

import java.util.HashMap;
import java.util.Map;

import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.json.IJsonParser;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.rest.IRestClient;
import se.springworks.android.utils.rest.IRestClient.OnHttpResponseHandler;

import com.google.inject.Inject;

/**
 * https://developers.google.com/maps/documentation/geocoding/
 * 
 * @author bjornritzl
 *
 */
public class GoogleGeoCodingApi implements IGeoCodingApi {

	@InjectLogger
	private Logger logger;
	
	@Inject
	private IRestClient restClient;
	
	@Inject
	private IJsonParser jsonParser;
	
	private Map<String, GeoCodeResults> resultsCache = new HashMap<String, GeoCodeResults>();
	
	@Override
	public void geocode(final String address, final IGeoCodeCallback callback) {
		if(resultsCache.containsKey(address)) {
			callback.onSuccess(resultsCache.get(address));
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("address", address);
		params.put("sensor", "true");
		
		restClient.get("http://maps.googleapis.com/maps/api/geocode/json", params, new OnHttpResponseHandler() {
			
			@Override
			public void onSuccess(String response) {
				GeoCodeResults results = jsonParser.fromJson(response, GeoCodeResults.class);
				resultsCache.put(address, results);
				callback.onSuccess(results);
			}
			
			@Override
			public void onFailure(Throwable t, String response, int code) {
				callback.onError(t, response);
			}
		});
	}

}
