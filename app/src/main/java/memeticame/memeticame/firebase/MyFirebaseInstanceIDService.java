package memeticame.memeticame.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by efmino on 20-08-17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseInstanceService";

    @Override
    public void onTokenRefresh() {
        //get reg token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //display token
        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }

    private void sendRegistrationToServer(String token) {

    }
}
