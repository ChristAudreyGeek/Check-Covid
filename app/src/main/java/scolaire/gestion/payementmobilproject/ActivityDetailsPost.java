package scolaire.gestion.payementmobilproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ActivityDetailsPost extends AppCompatActivity {

    private RecyclerView recycler_commentaire ;
    private TextView title , descipriont , txt_source;
    private ImageView icon_send, img_post ;
    private Intent intent ;
    private String str_title , str_description , str_img , id_post , source , lien;

    FirebaseFirestore firebaseFirestore ;
    String key , url_img;
    LinearLayoutManager linearLayoutManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_post);
        init();

        findViewById(R.id.sortir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.verifier).setOnClickListener(view -> {

            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(lien));
            startActivity(browser);


        });

    }




    public void imageactualliter(Context context , String Url){
        img_post.setImageResource(R.color.grise_flou_flou);
        StorageReference ImageProfilref = FirebaseStorage.getInstance().getReference().child(Url);
        GlideApp.with(context.getApplicationContext())
                .load(ImageProfilref)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(img_post);

    }

    void init(){
        img_post = findViewById(R.id.img_post);
        title = findViewById(R.id.title_post);
        descipriont = findViewById(R.id.description_post);
        txt_source = findViewById(R.id.source);
        intent = getIntent();
        str_description = intent.getStringExtra("description");
        str_img = intent.getStringExtra("img");
        str_title = intent.getStringExtra("title");
        id_post = intent.getStringExtra("id_post");
        source = intent.getStringExtra("source");
        lien = intent.getStringExtra("lien");
        key = id_post ;
        title.setText(str_title);
        descipriont.setText(str_description);
        txt_source.setText("Source "+source);
        imageactualliter(this,str_img);

    }
}