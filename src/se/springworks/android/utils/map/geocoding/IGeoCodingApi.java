package se.springworks.android.utils.map.geocoding;

public interface IGeoCodingApi {

	public interface IGeoCodeCallback {
		
		public void onSuccess(GeoCodeResults results);
		
		public void onError(Throwable t, String error);

	}
	
	void geocode(String address, IGeoCodeCallback callback);
}
