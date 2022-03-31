package scolaire.gestion.moduleaddvaccin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActivityAddVaccin extends AppCompatActivity {

    EditText nom , prenom , age ;
    CheckBox aztra , janssen , homme , femme ,spootnik ;
    ImageView img_profil , exit;
    RelativeLayout relative_ajouter ;
    String matricule ;
    Uri uriimageune ;
    private StorageReference reference;
    ProgressDialog progressDialog ;
    FirebaseUser firebaseUser ;
    private FirebaseFirestore firebaseFirestore ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccin);
        init();
        findViewById(R.id.exit).setOnClickListener(view -> onBackPressed());
        relative_ajouter.setOnClickListener(view -> {
            if (uriimageune == null){
                senddatauriempty();
            }else{

                progressDialog = new ProgressDialog(ActivityAddVaccin.this);
                progressDialog.setTitle("Enregistrement en cour");
                progressDialog.setMessage("Veuillez Patienté...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                String PostRendomTime = FieldValue.serverTimestamp().toString();
                final StorageReference filePath = reference.child("IMAGE_PATIENT").child(uriimageune.getLastPathSegment() + PostRendomTime + ".jpg");
                filePath.putFile(uriimageune).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        senddata(filePath.getPath());
                    }
                }).addOnProgressListener(taskSnapshot -> {
                });

            }

        });

        img_profil.setOnClickListener(view -> recordimage());

        aztra.setOnClickListener(view -> {
            aztra.setChecked(true);
            spootnik.setChecked(false);
            janssen.setChecked(false);
        });

        spootnik.setOnClickListener(view -> {
            aztra.setChecked(false);
            spootnik.setChecked(true);
            janssen.setChecked(false);
        });

        janssen.setOnClickListener(view -> {
            aztra.setChecked(false);
            spootnik.setChecked(false);
            janssen.setChecked(true);
        });

        femme.setOnClickListener(view -> {
            femme.setChecked(true);
            homme.setChecked(false);
        });

        homme.setOnClickListener(view -> {
            femme.setChecked(false);
            homme.setChecked(true);
        });


    }

    String genre(){
        String sexe = "";
        if(homme.isChecked()){
            sexe = "Masculin";
        }else{
            sexe = "Feminin";
        }
        return sexe ;
    }

    String vaccin(){
        String vaccin = "";
        if (aztra.isChecked()){
            vaccin = "AZTRAZENECCA";
        }

        if (janssen.isChecked()){
            vaccin = "JANSSEN";
        }

        if (spootnik.isChecked()){
            vaccin = "SPOOTNIK";
        }

        return vaccin ;

    }

    int matricule(){
        int rand ;
        do {
            // define the range
            int max = 5000000;
            int min = 1000000;
            int range = max - min + 1;

            rand = (int)(Math.random() * range) + min;

        }while (verifier(String.valueOf(rand)));

        return rand;

    }


    boolean myverifier = false ;
    boolean verifier(String matricule){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("PATIENTS_VACCINE").whereEqualTo("matricule",matricule).
                limit(1).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                myverifier = true ;
            }
        });
        return myverifier;
    }



    void senddata(String image){

        matricule = String.valueOf(matricule());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(calendar.getTime());


        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final String PostRendomTime = currentTime.format(calendarTime.getTime());
        String time = PostRendomTime+"-"+saveCurrentDate;

        Map<String,Object> map = new HashMap<>();
        map.put("nom",nom.getText().toString());
        map.put("prenom",prenom.getText().toString());
        map.put("age",age.getText().toString());
        map.put("vaccin",vaccin());
        map.put("sexe",genre());
        map.put("photo",image);
        map.put("date",time);
        map.put("matricule",matricule);

        firebaseFirestore.collection("PATIENTS_VACCINE").add(map).
                addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){

                        firebaseFirestore.collection("PATIENTS_VACCINE").document(task1.getResult().getId())
                                .get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                Patient patient = document.toObject(Patient.class);
                                progressDialog.dismiss();
                                newactivity(patient.getMatricule());
                            }
                        });



                    }else{
                        String message = task1.getException().toString();
                        Toast.makeText(getApplicationContext(), "Erreur"+message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Erreur d'enregistrement ", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

    void senddatauriempty(){
        progressDialog = new ProgressDialog(ActivityAddVaccin.this);
        progressDialog.setTitle("Action en cour");
        progressDialog.setMessage("Veuillez Patienté...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        matricule = String.valueOf(matricule());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(calendar.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final String PostRendomTime = currentTime.format(calendarTime.getTime());
        String time = PostRendomTime+"-"+saveCurrentDate;

        Map<String,Object> map = new HashMap<>();
        map.put("nom",nom.getText().toString());
        map.put("prenom",prenom.getText().toString());
        map.put("age",age.getText().toString());
        map.put("vaccin",vaccin());
        map.put("sexe",genre());
        map.put("photo","");
        map.put("date",time);
        map.put("matricule",String.valueOf(matricule()));
        firebaseFirestore.collection("PATIENTS_VACCINE").add(map).
                addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        firebaseFirestore.collection("PATIENTS_VACCINE").document(task1.getResult().getId())
                                .get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                Patient patient = document.toObject(Patient.class);
                                progressDialog.dismiss();
                                newactivity(patient.getMatricule());
                            }
                        });
                    }else{
                        String message = task1.getException().toString();
                        Toast.makeText(getApplicationContext(), "User"+message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "User d'envoie ", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });

    }

    void init(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        spootnik = findViewById(R.id.spootnik);
        aztra = findViewById(R.id.aztra);
        janssen = findViewById(R.id.jasnsesen);
        relative_ajouter = findViewById(R.id.valider);
        img_profil = findViewById(R.id.img);
        exit = findViewById(R.id.exit);
        age = findViewById(R.id.age);
        homme = findViewById(R.id.masculin);
        femme = findViewById(R.id.feminin);
//        str_ame = intent.getStringExtra("name");
//        name.setText(str_ame);

    }


    private void recordimage(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setMinCropResultSize(512,512).
                setBorderCornerLength(30).
                setAspectRatio(1,1).start(ActivityAddVaccin.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            uriimageune = activityResult.getUri();
            img_profil.setImageURI(uriimageune);
        }else{
            Toast.makeText(this, "Pas d'image selectionné veuillez recommencé ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }


    void newactivity(String matricule){
        Intent intent = new Intent(ActivityAddVaccin.this,ActivityPatientEnregistrer.class);
        intent.putExtra("matricule",matricule);
        intent.putExtra("nom",nom.getText().toString());
        intent.putExtra("prenom",prenom.getText().toString());
        intent.putExtra("genre",genre());
        startActivity(intent);
    }

}