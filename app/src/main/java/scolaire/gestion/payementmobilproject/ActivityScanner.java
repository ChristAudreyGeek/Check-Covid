package scolaire.gestion.payementmobilproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import scolaire.gestion.payementmobilproject.Database.DatabaseHelper;

public class ActivityScanner extends AppCompatActivity {

    LinearLayout information , demande_scanne , myscanne_actuelle ;
    Intent intent ;
    String matricule , str_nom , str_prenom , str_genre ;
    TextView nom , prenom , genre ;
    ImageView qrcode , qrcodedeux;
    private NotificationManager notificationManager ;
    LinearLayout infos_deux ;
    ProgressBar progressBar ;
    private DatabaseHelper databaseHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        information = findViewById(R.id.information);
        demande_scanne = findViewById(R.id.demande_scanne);
        nom = findViewById(R.id.nom);
        qrcodedeux = findViewById(R.id.qrcodedeux);
        prenom = findViewById(R.id.prenom);
        genre = findViewById(R.id.genre);
        qrcode = findViewById(R.id.qrcode);
        infos_deux = findViewById(R.id.infos_deux);
        progressBar = findViewById(R.id.progress_bar);
        myscanne_actuelle = findViewById(R.id.scanne_actuelle);

        findViewById(R.id.verifier).setOnClickListener(view -> {

            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(matricule));
            startActivity(browser);


        });

        findViewById(R.id.enregistré).setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent(getApplicationContext(), ActivityListPasseSanitaire.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid","audrey");
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(ActivityScanner.this,1,intent,PendingIntent.FLAG_ONE_SHOT);

                Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                OreoSDKHuit oreoSDKHuit = new OreoSDKHuit(getApplicationContext());
                Notification.Builder builder = oreoSDKHuit.getOreoNotification("Passe sanitaire - Certificat covid","Votre code QR a été enregistré avec succés",pendingIntent,defaultsound);

                int i = 0;
                int j = 1 ;
                if (j > 0){
                    i = j ;
                }

                oreoSDKHuit.getNotificationManager().notify(1,builder.build());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String saveCurrentDate = currentDate.format(calendar.getTime());


                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                final String PostRendomTime = currentTime.format(calendarTime.getTime());
                String time = PostRendomTime+"-"+saveCurrentDate;
                byte[] data = getBitmapAsBitmapArray(returnbit(matricule));
                if (databaseHelper.insertdata(matricule,time,String.valueOf(data))){
                    Toast.makeText(ActivityScanner.this, "Identifiant enregistré avec succées", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(ActivityFacture.this, "pas de donné", Toast.LENGTH_SHORT).show();
                }

            }else{
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ActivityScanner.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Passe sanitaire - Certificat covid")
                        .setContentText("Votre code QR a été enregistré avec succés")
                        .setAutoCancel(true);
                Intent intent = new Intent(ActivityScanner.this,ActivityListPasseSanitaire.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(ActivityScanner.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0,builder.build());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String saveCurrentDate = currentDate.format(calendar.getTime());


                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                final String PostRendomTime = currentTime.format(calendarTime.getTime());
                String time = PostRendomTime+"-"+saveCurrentDate;
                byte[] data = getBitmapAsBitmapArray(returnbit(matricule));
                if (databaseHelper.insertdata(matricule,time,String.valueOf(data))){
                    Toast.makeText(ActivityScanner.this, "Votre code QR a été enregistré avec succés", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(ActivityFacture.this, "pas de donné", Toast.LENGTH_SHORT).show();
                }

            }




        });

        findViewById(R.id.enregistre).setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent(getApplicationContext(), ActivityListPasseSanitaire.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid","audrey");
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(ActivityScanner.this,1,intent,PendingIntent.FLAG_ONE_SHOT);

                Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                OreoSDKHuit oreoSDKHuit = new OreoSDKHuit(getApplicationContext());
                Notification.Builder builder = oreoSDKHuit.getOreoNotification("Passe sanitaire - Certificat covid","Votre code QR a été enregistré avec succés",pendingIntent,defaultsound);

                int i = 0;
                int j = 1 ;
                if (j > 0){
                    i = j ;
                }

                oreoSDKHuit.getNotificationManager().notify(1,builder.build());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String saveCurrentDate = currentDate.format(calendar.getTime());


                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                final String PostRendomTime = currentTime.format(calendarTime.getTime());
                String time = PostRendomTime+"-"+saveCurrentDate;
                byte[] data = getBitmapAsBitmapArray(returnbit(matricule));
                if (databaseHelper.insertdata(matricule,time,String.valueOf(data))){
                    Toast.makeText(ActivityScanner.this, "Identifiant enregistré avec succées", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(ActivityFacture.this, "pas de donné", Toast.LENGTH_SHORT).show();
                }

            }else{
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ActivityScanner.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Passe sanitaire - Certificat covid")
                        .setContentText("Votre code QR a été enregistré avec succés")
                        .setAutoCancel(true);
                Intent intent = new Intent(ActivityScanner.this,ActivityListPasseSanitaire.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(ActivityScanner.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0,builder.build());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String saveCurrentDate = currentDate.format(calendar.getTime());


                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                final String PostRendomTime = currentTime.format(calendarTime.getTime());
                String time = PostRendomTime+"-"+saveCurrentDate;
                byte[] data = getBitmapAsBitmapArray(returnbit(matricule));
                if (databaseHelper.insertdata(matricule,time,String.valueOf(data))){
                    Toast.makeText(ActivityScanner.this, "Votre code QR a été enregistré avec succés", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(ActivityFacture.this, "pas de donné", Toast.LENGTH_SHORT).show();
                }

            }




        });

    }


    public void ScanButton(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                //textView.setText("Cette carte est invalide");
            }else {
               // textView.setText(intentResult.getContents());
                matricule = intentResult.getContents();
                demande_scanne.setVisibility(View.GONE);
                information.setVisibility(View.VISIBLE);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("PATIENTS_VACCINE").whereEqualTo("matricule",String.valueOf(intentResult.getContents()))
                        .get().addOnCompleteListener(task -> {
                            if (task.getResult().size() == 0){
                                information.setVisibility(View.GONE);
                                demande_scanne.setVisibility(View.GONE);
                                myscanne_actuelle.setVisibility(View.VISIBLE);
                                generecodedeux(matricule);

                            }else{
                                if (task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        if (documentSnapshot.exists()){
                                            Patient profilUser = documentSnapshot.toObject(Patient.class);
                                            profilUser.setId(documentSnapshot.getId());
                                            progressBar.setVisibility(View.GONE);
                                            demande_scanne.setVisibility(View.GONE);
                                            information.setVisibility(View.VISIBLE);
                                            infos_deux.setVisibility(View.VISIBLE);
                                            generecode(matricule);
                                            nom.setText(profilUser.getNom());
                                            prenom.setText(profilUser.getPrenom());
                                            genre.setText(profilUser.getSexe());


                                        }else{
                                            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
                                        }



                                    }
                                }
                            }


                });

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void generecode(String mymatricule){
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = writer.encode(mymatricule, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcode.setImageBitmap(bitmap);

            //InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //manager.hideSoftInputFromWindow(matricule.getApplicationWindowToken())
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    void generecodedeux(String mymatricule){
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = writer.encode(mymatricule, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcodedeux.setImageBitmap(bitmap);

            //InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //manager.hideSoftInputFromWindow(matricule.getApplicationWindowToken())
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    Bitmap returnbit(String mymatricule){
        MultiFormatWriter writer = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix bitMatrix = writer.encode(mymatricule, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcodedeux.setImageBitmap(bitmap);

            //InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //manager.hideSoftInputFromWindow(matricule.getApplicationWindowToken())


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return bitmap;

    }


    public static byte[] getBitmapAsBitmapArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,outputStream);
        return outputStream.toByteArray();


    }




}