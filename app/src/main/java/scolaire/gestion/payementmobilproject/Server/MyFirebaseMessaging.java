package scolaire.gestion.payementmobilproject.Server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import scolaire.gestion.payementmobilproject.ActivityNotification;


public class MyFirebaseMessaging extends FirebaseMessagingService {

    private String Tag;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        updatetoken(s);
        Log.e(Tag,"mon log se trouve etre ici s'ils vout plait "+s);
    }

    private void updatetoken(String refreshtoken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshtoken);
        if(firebaseUser != null){
            reference.child(firebaseUser.getUid()).setValue(token);
        }
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null && sented.equals(firebaseUser.getUid())){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendOreoNotification(remoteMessage);
            }else{
                sendNotification(remoteMessage);
            }

        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));

        if (title.equals("KOYEKOLA : DEMANDE D'ACCES")){


            Intent intent = new Intent(getApplicationContext(), ActivityNotification.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),j,intent,PendingIntent.FLAG_ONE_SHOT);

            Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoSDKHuit oreoSDKHuit = new OreoSDKHuit(this);
            Notification.Builder builder = oreoSDKHuit.getOreoNotification(title,body,pendingIntent,defaultsound,icon);

            int i = 0;
            if (j > 0){
                i = j ;
            }

            oreoSDKHuit.getNotificationManager().notify(i,builder.build());
        } else{

            Intent intent = new Intent(getApplicationContext(), ActivityNotification.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

            Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoSDKHuit oreoSDKHuit = new OreoSDKHuit(getApplicationContext());
            Notification.Builder builder = oreoSDKHuit.getOreoNotification(title,body,pendingIntent,defaultsound,icon);

            int i = 0;
            if (j > 0){
                i = j ;
            }

            oreoSDKHuit.getNotificationManager().notify(i,builder.build());

        }



    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        if (title.equals("KOYEKOLA : DEMANDE D'ACCES")){
            Intent intent = new Intent(getApplicationContext(), ActivityNotification.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),j,intent,PendingIntent.FLAG_ONE_SHOT);

            Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(Integer.parseInt(icon))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultsound)
                    .setContentIntent(pendingIntent);
            NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int i = 0;
            if (j > 0){
                i = j ;
            }

            noti.notify(i,builder.build());
        }else{
            Intent intent = new Intent(getApplicationContext(), ActivityNotification.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),j,intent,PendingIntent.FLAG_ONE_SHOT);

            Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(Integer.parseInt(icon))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultsound)
                    .setContentIntent(pendingIntent);
            NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int i = 0;
            if (j > 0){
                i = j ;
            }

            noti.notify(i,builder.build());
        }
    }
}
