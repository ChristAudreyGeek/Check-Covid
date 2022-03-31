package scolaire.gestion.payementmobilproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.hover.sdk.api.Hover;
//import com.hover.sdk.api.HoverParameters;

import scolaire.gestion.payementmobilproject.Database.DatabaseHelper;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db ;
    TextView nombre ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(MainActivity.this);
        nombre = findViewById(R.id.nombre);
        findViewById(R.id.scanner).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivityScanner.class)));
        findViewById(R.id.mycheck).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivityListPasseSanitaire.class)));
        findViewById(R.id.preenregistrement).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivityInscription.class)));
        findViewById(R.id.notification).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivityNotification.class)));
        findViewById(R.id.rendezvous).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivityInscription.class)));
        findViewById(R.id.prc).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivityTestPcr.class)));
        findViewById(R.id.sympto).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivitySyptome.class)));
        findViewById(R.id.callmedecin).setOnClickListener(view -> {
            FragmentManager manager = getSupportFragmentManager();
            DialogCallMedecin dialogFragmentAfrique = DialogCallMedecin.
                    newInstance("");
            dialogFragmentAfrique.setCancelable(false);
            dialogFragmentAfrique.show(manager,null);
        });

        long profile_counts = db.getProfilesCount();
        nombre.setText(String.valueOf(profile_counts));

        db.close();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAffinity();
    }

    //     Hover.initialize(this);
//    number = findViewById(R.id.number);
//    prix = findViewById(R.id.prix);
//    envoyer = findViewById(R.id.envoyer);
//    startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
//
//        envoyer.setOnClickListener(view -> {
////            Intent i = new HoverParameters.Builder(getApplicationContext())
////                    .request("5b637b9f")
////                    .extra("number",number.getText().toString())
////                    .extra("amount", prix.getText().toString())
////                    .buildIntent();
////            startActivityForResult(i,3);
//
//        String UssdCode = "*128*2*1*1*055251520*10000*6789#";
//
////check if edittext is empty
//        if (UssdCode.equalsIgnoreCase("")) {
//
//            Toast.makeText(MainActivity.this, "Please Input ussd code", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
////check if its a valid ussd code 2 1 1 numero montant pin
//        if (UssdCode.startsWith("*") && UssdCode.endsWith("#")) {
//
////we want to remove the last # from the ussd code as we need to encode it. so *555# becomes *555
//            UssdCode = UssdCode.substring(0, UssdCode.length() - 1);
//
//            String UssdCodeNew = UssdCode + Uri.encode("#");
//
////request for permission
//            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//
//            } else {
////dial Ussd code
//                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UssdCodeNew)));
//
//            }
//
//
//        } else {
//            Toast.makeText(MainActivity.this, "Please enter a valid ussd code", Toast.LENGTH_SHORT).show();
//        }
//
//    });


}