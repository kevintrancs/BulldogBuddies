package com.example.ktran.wannabetinder.models;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by nicolehoward on 12/4/17.
 */

public class MyFirebaseInstanceStateIdService extends FirebaseInstanceIdService {

    final String TAG = "MyFirebaseInstance: ";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //If I want to send messages to this app instance or manage apps
        //subscriptions on the our server-side, send instance ID token to your app server
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}
