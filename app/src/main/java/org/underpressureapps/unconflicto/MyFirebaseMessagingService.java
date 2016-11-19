package org.underpressureapps.unconflicto;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by RandyC on 17/11/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(MainActivity.TAG, "From: " + remoteMessage.getFrom());
        Log.d(MainActivity.TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
