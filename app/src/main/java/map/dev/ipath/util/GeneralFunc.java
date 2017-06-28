package map.dev.ipath.util;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;
import java.util.Locale;

import map.dev.ipath.SignUpActivity;

/**
 * Created by adrian on 05.04.2017.
 */

public class GeneralFunc {

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getLocationAddress(Context context, double latitude, double longitude){

        try {
            Geocoder geo = new Geocoder(context, Locale.getDefault());

            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                // Waiting for Location
            }
            else {
                if (addresses.size() > 0) {
                    String addressString = addresses.get(0).getFeatureName();
                    //  + ", getLocality: " + addresses.get(0).getLocality() + ", getAdminArea: "
                    // + addresses.get(0).getAdminArea() + ", getCountryName: " + addresses.get(0).getCountryName();


                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    String subAdminArea = addresses.get(0).getSubAdminArea();
                    String subLocality = addresses.get(0).getSubLocality();
                    String thoroughfare = addresses.get(0).getThoroughfare();
                    String url = addresses.get(0).getUrl();

                    String shareAddress = "";
                    if(address != null){
//                        shareAddress11 = shareAddress11 + address + "\n";
                        shareAddress = shareAddress + address + ", ";
                    }
                    if(subLocality != null){
                        shareAddress = shareAddress + subLocality + ", ";
                    }
                    if(city != null){
                        shareAddress = shareAddress + city + ", ";
                    }
                    if(state != null){
                        shareAddress = shareAddress + state + " ";
                    }
                    if(postalCode != null){
                        shareAddress = shareAddress + postalCode + ", ";
                    } else {
                        shareAddress = shareAddress + ", ";
                    }
//                    if(country != null){
////                        shareAddress11 = shareAddress11 + country + ".";
//                        shareAddress = shareAddress + country + ".";
//                    }
//						shareAddress11 = shareAddress11 + city + ", " + state + ", " + country;

                    return shareAddress;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return "";
    }
}
