package scolaire.gestion.payementmobilproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import scolaire.gestion.payementmobilproject.Server.APIService;
import scolaire.gestion.payementmobilproject.Server.Client;
import scolaire.gestion.payementmobilproject.Server.Data;
import scolaire.gestion.payementmobilproject.Server.Sender;
import scolaire.gestion.payementmobilproject.Server.Token;

public class ActivityCustomCompte extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner myspinner ;
    String myarrondissement ;
    EditText age, edit_nom;
    RelativeLayout valider ;
    ImageView img_avatar ;
    TextView name , txt_verification ;
    Intent intent ;
    CheckBox male , feminin ;
    //private Uri uriimage ;
    Uri uriimageune ;
    private StorageReference reference;
    ProgressDialog progressDialog ;
    String str_ame, verification  ;
    FirebaseUser firebaseUser ;
    private FirebaseFirestore firebaseFirestore ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_compte);
        init();
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        img_avatar.setOnClickListener(view -> recordimage());
        valider.setOnClickListener(view -> {
            if(uriimageune == null){
                if (edit_nom.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Vous devez entre votre nom d'utilisateur", Toast.LENGTH_SHORT).show();
                }else{
                    if (signInAccount == null){
                        senddatauriempty("");
                    }else{
                        senddatauriempty(firebaseUser.getPhotoUrl().toString());
                    }

                }
            }else{
                if (edit_nom.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Vous devez entre un nom d'utilisateur", Toast.LENGTH_SHORT).show();
                }else{
                    senddata();
                }

            }
        });

        if (signInAccount != null){
            Uri uri = Uri.parse(firebaseUser.getPhotoUrl().toString());
            GlideApp.with(this)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(img_avatar);
            edit_nom.setText(firebaseUser.getDisplayName());
        }

        male.setOnClickListener(view -> {
            male.setChecked(true);
            feminin.setChecked(false);
        });

        feminin.setOnClickListener(view -> {
            feminin.setChecked(true);
            male.setChecked(false);
        });

        myspinner = findViewById(R.id.myspinner);
        myspinner.setOnItemSelectedListener(this);
        String[] value = {"Masculin","Feminin"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.layout_style,arrayList);
        myspinner.setAdapter(arrayAdapter);



    }

    String  setsexe(){
        String genre ;
        if (feminin.isChecked()){
            genre = "feminin";
        }else {
            genre = "masculin";
        }

        return genre ;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selection = adapterView.getItemAtPosition(i).toString();
        myarrondissement = selection ;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    void senddata(){
        progressDialog = new ProgressDialog(ActivityCustomCompte.this);
        progressDialog.setTitle("Action en cour");
        progressDialog.setMessage("Veuillez Patienté...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        String PostRendomTime = FieldValue.serverTimestamp().toString();
        final StorageReference filePath = reference.child("Post_Image").child(uriimageune.getLastPathSegment() + PostRendomTime + ".jpg");
        filePath.putFile(uriimageune).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());

                Map<String,Object> map = new HashMap<>();
                map.put("uid",firebaseUser.getUid());
                map.put("name",edit_nom.getText().toString());
                map.put("img_url","");
                map.put("centre","");
                map.put("age", age.getText().toString());
                map.put("genre",myarrondissement);
                firebaseFirestore.collection("UserProfil").document(firebaseUser.getUid()).set(map).
                        addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){

                                senduserfirebasedatabse(map);

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
        }).addOnProgressListener(taskSnapshot -> {
        });

    }

    void senddatauriempty(String url){
        progressDialog = new ProgressDialog(ActivityCustomCompte.this);
        progressDialog.setTitle("Action en cour");
        progressDialog.setMessage("Veuillez Patienté...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(calendar.getTime());
        Map<String,Object> map = new HashMap<>();
        map.put("uid",firebaseUser.getUid());
        map.put("name",edit_nom.getText().toString());
        map.put("img_url",url);
        map.put("centre","");
        map.put("age", age.getText().toString());
        map.put("genre",myarrondissement);
        firebaseFirestore.collection("UserProfil").document(firebaseUser.getUid()).set(map).
                addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        senduserfirebasedatabse(map);

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

        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        age = findViewById(R.id.age);
        valider = findViewById(R.id.btn_inscription);
        img_avatar = findViewById(R.id.img_avatar);
        name = findViewById(R.id.name);
        male = findViewById(R.id.male);
        feminin = findViewById(R.id.feminin);
        edit_nom = findViewById(R.id.edit_nom);
        txt_verification = findViewById(R.id.ecriture);
        intent = getIntent();
        str_ame = intent.getStringExtra("name");
        verification = intent.getStringExtra("profil");
        name.setText(str_ame);

        FirebaseInstanceId.getInstance().getInstanceId().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String token = task.getResult().getToken();
                        updatetoken(token);
                    }
                });
//        if (verification.equals("noprofil")){
//            txt_verification.setVisibility(View.GONE);
//        }else{
//            txt_verification.setVisibility(View.VISIBLE);
//        }

    }


    private void recordimage(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setMinCropResultSize(512,512).
                setBorderCornerLength(30).
                setAspectRatio(1,1).start(ActivityCustomCompte.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            uriimageune = activityResult.getUri();
            img_avatar.setImageURI(uriimageune);
        }else{
            Toast.makeText(this, "Pas d'image selectionné veuillez recommencé ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), RegisterVaccination.class));
        }
    }

    void senduserfirebasedatabse(Map<String,Object> hashMap){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(firebaseUser.getUid()).
                setValue(hashMap).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Compte personnalisé avec succés", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RegisterVaccination.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void updatetoken(String refreshtoken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshtoken);
        reference.child(firebaseUser.getUid()).setValue(token);

    }







}