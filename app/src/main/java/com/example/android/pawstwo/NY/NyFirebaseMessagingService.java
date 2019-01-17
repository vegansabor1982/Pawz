package com.example.android.pawstwo.NY;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.android.pawstwo.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived( RemoteMessage remoteMessage ) {
        super.onMessageReceived ( remoteMessage );

        String notification_title=remoteMessage.getNotification ().getTitle ();
        String notification_message=remoteMessage.getNotification ().getBody ();

        String click_action = remoteMessage.getNotification ().getClickAction ();

        String from_user_id=remoteMessage.getData ().get ( "from_user_id" );

        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder ( this ).setSmallIcon ( R.drawable.paw_logo_two).setContentTitle ( notification_title).setContentText ( notification_message );

        Intent resultIntent = new Intent ( click_action  );
        resultIntent.putExtra ( "userId", from_user_id );

        PendingIntent resultPendingIntent = PendingIntent.getActivity ( this, 0, resultIntent,PendingIntent.FLAG_UPDATE_CURRENT );

        mBuilder.setContentIntent ( resultPendingIntent );

        int mNotificationId= ( int ) System.currentTimeMillis ();

        NotificationManager mNotifyMgr= ( NotificationManager ) getSystemService ( NOTIFICATION_SERVICE );

        mNotifyMgr.notify ( mNotificationId, mBuilder.build () );

    }
}
