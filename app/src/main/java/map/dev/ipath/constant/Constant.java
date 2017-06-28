package map.dev.ipath.constant;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by adrian on 10.03.2017.
 */

public class Constant {
    // web url: ipathgps.com
    // font name: Lucile calligraphy
    // mile = 1609.344 m

    //
    public static final float MToMile = 1609.344f;

    //
    public static final String MyShared = "MyShared";
    public static String firstRun = "firstRun";

    // tag
    public static final String FromTag = "FromTag";
    public static final String FromDetail = "FromDetail";
    public static final String FromSplash = "FromSplash";
    public static final String FromMain = "FromMain";
    public static final String FromAddPlace = "FromAddPlace";

    public static final String Default = "Default";

    // Fragment Tag Name
    public static final String FragmentMainMap = "FragmentMainMap";
    public static final String FragmentMainList = "FragmentMainList";
    public static final String FragmentMainFavorite = "FragmentMainFavorite";
//    public static final String AddPlaceActivity = "AddPlaceActivity";


    // request permission tag
    public static final int Permission_ACCESS_FINE_LOCATION = 2;
    public static final int Permission_CALL_PHONE = 1;

    // Selected Place Name
    public static String SelectedPlaceName = "";
    public static String SelectedPlaceRating = "";

    // URL info
    public static final String signUpURL = "http://54.191.131.180/ipath/mobile/users/signUp";                      // email, username, password
    public static final String signInURL = "http://54.191.131.180/ipath/mobile/users/signIn";                      // email(or username), password
    public static final String forgotPasswordURL = "http://54.191.131.180/ipath/mobile/users/forgotPass";          // email

    public static final String getUserURL = "https://54.191.131.180/ipath/mobile/users/getUser";                    // email:  personal data
    public static final String getAllUsersURL = "https://54.191.131.180/ipath/mobile/users/getAllUsers";            // get all users data
    public static final String getAllPlacesURL = "https://54.191.131.180/ipath/mobile/users/getAllPlaces";          // get all places data
    public static final String getAllFavoritesURL = "https://54.191.131.180/ipath/mobile/users/getAllFavorites";    // get all favorites data

    public static final String getAllFavoritesByUserIdURL = "https://54.191.131.180/ipath/mobile/users/getAllFavoritesByUserId";        // user_id: get all favorites data by user id
    public static final String getAllFavoritesByEmailURL = "https://54.191.131.180/ipath/mobile/users/getAllFavoritesByEmail";          // email: get all favorites data by email
    public static final String getAllFavoritesByUsernameURL = "https://54.191.131.180/ipath/mobile/users/getAllFavoritesByUsername";    // username: get all favorites data by username

    public static final String getAllRatesURL = "https://54.191.131.180/ipath/mobile/users/getAllRates";            // get all rates data

    public static final String getAllRatesByUserIdURL = "https://54.191.131.180/ipath/mobile/users/getAllRatesByUserId";                // user_id: get all rates data by user id
    public static final String getAllRatesByUserEmailURL = "https://54.191.131.180/ipath/mobile/users/getAllRatesByUserEmail";          // email: get all rates data by email
    public static final String getAllRatesByUsernameURL = "https://54.191.131.180/ipath/mobile/users/getAllRatesByUsername";            // username: get all rates data by username

    public static final String getAllRatesByPlaceIdURL = "https://54.191.131.180/ipath/mobile/users/getAllRatesByPlaceId";              // place_id: get all rates data by place id
    public static final String getAllRatesByPlaceNameURL = "https://54.191.131.180/ipath/mobile/users/getAllRatesByPlaceName";          // name: get all rates data by place name


    public static final String getAllInfoByUserIdURL = "https://54.191.131.180/ipath/mobile/users/getAllInfoByUserId";          // user_id: get all data by user id


    public static final String addPlaceURL = "https://54.191.131.180/ipath/mobile/places/addPlace";                         // name, category_id, latitude, longitude,
                                                                                                                    // phone, email, address

    public static final String addCategoryURL = "https://54.191.131.180/ipath/mobile/categories/addCategory";               // name

    public static final String addFavoriteURL = "https://54.191.131.180/ipath/mobile/favorites/addFavorite";                // user_id, place_id
    public static final String addFavoriteByNameURL = "https://54.191.131.180/ipath/mobile/favorites/addFavoriteByName";    // username, place_name

    public static final String addRateURL = "https://54.191.131.180/ipath/mobile/rates/addRate";                            // user_id, place_id, content, value
    public static final String addRateByNameURL = "https://54.191.131.180/ipath/mobile/rates/addRateByName";                // username, place_name, content, value


    // user info
    public static final String Email = "Email";
    public static final String Username = "Username";
    public static final String Password = "Password";

    public static LatLng CurrentPosition = new LatLng(29.2070317, -81.0471095);
}
