package scolaire.gestion.moduleaddvaccin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ActivityScanner extends AppCompatActivity {

    LinearLayout information , demande_scanne;
    Intent intent ;
    String matricule , str_nom , str_prenom , str_genre ;
    TextView nom , prenom , genre ;
    ImageView qrcode ;
    private NotificationManager notificationManager ;
    LinearLayout infos_deux ;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        information = findViewById(R.id.information);
        demande_scanne = findViewById(R.id.demande_scanne);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        genre = findViewById(R.id.genre);
        qrcode = findViewById(R.id.qrcode);
        infos_deux = findViewById(R.id.infos_deux);
        progressBar = findViewById(R.id.progress_bar);

    }


    public void ScanButton(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
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
                    if (task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            if (documentSnapshot.exists()){
                                Patient profilUser = documentSnapshot.toObject(Patient.class);
                                profilUser.setId(documentSnapshot.getId());
                                progressBar.setVisibility(View.GONE);
                                infos_deux.setVisibility(View.VISIBLE);
                                nom.setText(profilUser.getNom());
                                prenom.setText(profilUser.getPrenom());
                                genre.setText(profilUser.getSexe());


                            }else{
                                Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
                            }



                        }
                    }

                });

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




}