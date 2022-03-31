package scolaire.gestion.payementmobilproject;

import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

public class MyNotification extends NotificationListenerService {

    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // We can read notification while posted.
        for (StatusBarNotification sbm : MyNotification.this.getActiveNotifications()) {
            String title = sbm.getNotification().extras.getString("android.title");
            String text = sbm.getNotification().extras.getString("android.text");
            String package_name = sbm.getPackageName();

//            Log.v("Notification title is:", title);
//            Log.v("Notification text is:", text);
//            Log.v("package name:", package_name);
            Toast.makeText(context, "taille "+MyNotification.this.getActiveNotifications().length, Toast.LENGTH_SHORT).show();


        }
    }
}
