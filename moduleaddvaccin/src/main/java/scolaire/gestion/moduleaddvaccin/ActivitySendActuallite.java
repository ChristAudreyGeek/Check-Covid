package scolaire.gestion.moduleaddvaccin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySendActuallite extends AppCompatActivity {

    EditText source , title , name_source,description ;
    Uri uri_couverture , url_source ;
    private final int RESQUEST_CODE_IMAGE_COUVERTURE = 102 ;
    private final int RESQUEST_CODE_IMA = 101 ;
    ImageView img ;
    CircleImageView img_source ;
    ProgressDialog progressDialog ;
    FirebaseUser firebaseUser ;
    FirebaseFirestore firebaseFirestore ;
    FirebaseAuth firebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_actuallite);
        init();

        findViewById(R.id.img).setOnClickListener(view -> recordimage());

        findViewById(R.id.valider).setOnClickListener(view -> sendresultats());

        findViewById(R.id.img_source).setOnClickListener(view -> recordimageprofil());

    }

    void init(){
        name_source = findViewById(R.id.name_source);
        img_source = findViewById(R.id.img_source);
        source = findViewById(R.id.source);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        img = findViewById(R.id.img);
    }

    void sendresultats(){
        progressDialog = new ProgressDialog(ActivitySendActuallite.this);
        progressDialog.setTitle("Envoi en cour");
        progressDialog.setMessage("Veuillez Patienté...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final StorageReference reference ;
        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final StorageReference filepath_source = reference.child("Post_Image").child(url_source.getLastPathSegment() + ".jpg");
        filepath_source.putFile(url_source).addOnCompleteListener(task -> {

              if (task.isSuccessful()){

                  final StorageReference filepath_couverture = reference.child("Post_Image").child(uri_couverture.getLastPathSegment() + ".jpg");
                  filepath_couverture.putFile(uri_couverture).addOnCompleteListener(tas1k -> {
                      if (tas1k.isSuccessful()){
                          Calendar c = Calendar.getInstance();
                          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                          String strDate = sdf.format(c.getTime());
                          Map<String,Object> map = new HashMap<>();
                          map.put("lien_source",source.getText().toString());
                          map.put("title_actualliter",title.getText().toString());
                          map.put("description",description.getText().toString());
                          map.put("uid_publisher",firebaseUser.getUid());
                          map.put("time_publisher",strDate);
                          map.put("name_source",name_source.getText().toString());
                          map.put("img_source",filepath_source.getPath());
                          map.put("img_post",filepath_couverture.getPath());
                          firebaseFirestore.collection("Actualliter").add(map).addOnCompleteListener(task2 -> {
                              if(task2.isSuccessful()){
                                  startActivity(new Intent(ActivitySendActuallite.this, MainActivity.class));
                                  progressDialog.dismiss();
                                  finish();
                              }else{
                                  String message = task2.getException().toString();
                                  Toast.makeText(getApplicationContext(), "Erreur"+message, Toast.LENGTH_SHORT).show();
                                  progressDialog.dismiss();
                              }                        }).addOnFailureListener(e -> {
                              Toast.makeText(getApplicationContext(), "Erreur d'envoie ", Toast.LENGTH_SHORT).show();
                              progressDialog.dismiss();
                          });
                      }
                  });
              }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESQUEST_CODE_IMAGE_COUVERTURE && resultCode == RESULT_OK){
            uri_couverture = data.getData();
            img.setImageURI(uri_couverture);
            img.setVisibility(View.VISIBLE);

        }else if(requestCode == RESQUEST_CODE_IMA && resultCode == RESULT_OK){
            url_source = data.getData();
            img_source.setImageURI(url_source);
        }else{
            Toast.makeText(getApplicationContext(), "Pas d'image selectionner veuillez selectionner une image", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, ActivityAddMagBee.class));
        }
    }

    private void recordimage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,RESQUEST_CODE_IMAGE_COUVERTURE);
    }

    private void recordimageprofil(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,RESQUEST_CODE_IMA);
    }
}