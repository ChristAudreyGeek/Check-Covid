package scolaire.gestion.payementmobilproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import scolaire.gestion.payementmobilproject.Database.AdapterNotification;
import scolaire.gestion.payementmobilproject.Database.AdapterNotificationMessage;
import scolaire.gestion.payementmobilproject.Database.DatabaseHelper;
import scolaire.gestion.payementmobilproject.Database.NotificationData;

public class ActivityNotification extends AppCompatActivity {


    ArrayList<NotificationData> list ;
    DatabaseHelper db ;
    AdapterNotificationMessage adapter ;
    RecyclerView listView ;
    ArrayList<String> abonner , time ;
    LinearLayout nonotifie ;
    View root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        listView = findViewById(R.id.data_liste);
        nonotifie = findViewById(R.id.no_notifi);

        findViewById(R.id.add).setOnClickListener(view -> startActivity(new Intent(ActivityNotification.this,ActivityScanner.class)));
        db = new DatabaseHelper(ActivityNotification.this);
        list = new ArrayList<>();
        abonner = new ArrayList<>();
        time = new ArrayList<>();
        viewdata();

        findViewById(R.id.sortie).setOnClickListener(view -> startActivity(new Intent(ActivityNotification.this,MainActivity.class)));
    }





    void viewdata()
    {
        Cursor cursor = db.viewdatanotif();
        if (cursor.getCount() == 0){
            // Toast.makeText(getContext(), "Pas de données dans la list", Toast.LENGTH_SHORT).show();
            listView.setVisibility(View.GONE);
            nonotifie.setVisibility(View.VISIBLE);
        }else{

            while (cursor.moveToNext()){
//                list.add(cursor.getString(1));
                abonner.add(cursor.getString(1));
                time.add(cursor.getString(2));
            }

            adapter = new AdapterNotificationMessage(ActivityNotification.this,abonner,time);
            listView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityNotification.this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            listView.setLayoutManager(linearLayoutManager);

            listView.setVisibility(View.VISIBLE);
            nonotifie.setVisibility(View.GONE);

        }
    }
}





