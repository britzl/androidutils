package se.springworks.android.utils.map.geocoding;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.inject.Inject;

public class AndroidGeoCoder implements IGeoCodingApi {

	@Inject
	private Context context;
	
	@Override
	public void geocode(String address, IGeoCodeCallback callback) {
		
		GeoCodeResults results = new GeoCodeResults();
		
		Geocoder gc = new Geocoder(context);
		try {
			List<Address> addresses = gc.getFromLocationName(address, 5);
			for(Address a : addresses) {
				results.add(GeoCodeResult.fromAddress(a));
			}
		}
		catch (IOException e) {
			callback.onError(e, "");
			return;
		}
		callback.onSuccess(results);
	}

}
