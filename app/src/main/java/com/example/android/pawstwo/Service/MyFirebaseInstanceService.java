package com.example.android.pawstwo.Service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.pawstwo.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

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

    /*private void showNotification( Map<String,String> data ) {

        //String title =data.get ( "title" ).toString ();
       // String body = data.get ("body"  ).toString ();


        NotificationManager notificationManager = (NotificationManager)getSystemService ( Context.NOTIFICATION_SERVICE );

        String NOTIFICATION_CHANNEL_ID="com.example.android.pawstwo.test ";

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel ( NOTIFICATION_CHANNEL_ID, "Notification ", NotificationManager.IMPORTANCE_DEFAULT );

            notificationChannel.setDescription ( "New Message" );
            notificationChannel.enableLights ( true );
            notificationChannel.setLightColor ( Color.BLUE );
            notificationChannel.setVibrationPattern ( new long[] {0, 1000, 500, 1000} );
            notificationChannel.enableLights ( true );
            notificationManager.createNotificationChannel ( notificationChannel );
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder ( this, NOTIFICATION_CHANNEL_ID );
        notificationBuilder.setAutoCancel ( true ).setDefaults ( Notification.DEFAULT_ALL ).setWhen ( System.currentTimeMillis () ).setSmallIcon ( R.drawable.ic_notification);


        notificationManager.notify ( new Random (  ).nextInt (),notificationBuilder.build () );

    }

    private void showNotification( String title, String body ) {

        NotificationManager notificationManager = (NotificationManager)getSystemService ( Context.NOTIFICATION_SERVICE );

        String NOTIFICATION_CHANNEL_ID="com.example.android.pawstwo.test ";

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel ( NOTIFICATION_CHANNEL_ID, "Notification ", NotificationManager.IMPORTANCE_DEFAULT );

            notificationChannel.setDescription ( "New Message" );
            notificationChannel.enableLights ( true );
            notificationChannel.setLightColor ( Color.BLUE );
            notificationChannel.setVibrationPattern ( new long[] {0, 1000, 500, 1000} );
            notificationChannel.enableLights ( true );
            notificationManager.createNotificationChannel ( notificationChannel );
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder ( this, NOTIFICATION_CHANNEL_ID );
        notificationBuilder.setAutoCancel ( true ).setDefaults ( Notification.DEFAULT_ALL ).setWhen ( System.currentTimeMillis () ).setSmallIcon ( R.drawable.ic_notification).setContentTitle ( title ).setContentText ( body )
                .setContentInfo ( "Info" );

        notificationManager.notify ( new Random (  ).nextInt (),notificationBuilder.build () );



    }

    @Override
    public void onNewToken( String s ) {
        super.onNewToken ( s );


        Log.d("TOKENFIREBASE",s);
    }*/
}
