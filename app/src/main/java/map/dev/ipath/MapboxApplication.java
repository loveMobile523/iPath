package map.dev.ipath;

import android.app.Application;

//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;

/**
 * Created by adrian on 10.03.2017.
 */

public class MapboxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        FirebaseApp.initializeApp(this, new FirebaseOptions.Builder()
//                .setApiKey(getString(R.string.firebase_api_key))
//                .setApplicationId(getString(R.string.firebase_app_id))
//                .build());
    }
}
