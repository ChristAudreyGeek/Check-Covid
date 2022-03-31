package scolaire.gestion.payementmobilproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityScanneMyQR extends AppCompatActivity {

    TextView nom , prenom , name ,genre ;
    ImageView qrcode , qrcodedeux;
    Intent intent ;
    CircleImageView img_profil ;
    GoogleSignInAccount signInAccount ;
    String matricule ;
    LinearLayout linearLayout , linearLayout_deux , line_connection , linearLayout_compte ;
    ProgressBar progressBar ;


    private ProgressDialog progressDialog;
    private FirebaseAuth auth ;
    private FirebaseFirestore firebaseFirestore ;

    TextView txt_name ;
    private static final int RC_SIGN_IN = 123 ;
    private static final String TAG = "My tag" ;
    private FirebaseAuth mAuth;
    GoogleSignInClient googleSignIn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_scanne_my_q_r);
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        init();

        createRequest();
        findViewById(R.id.google).setOnClickListener(view -> signIn());


        findViewById(R.id.phone_number).setOnClickListener(view -> startActivity(new Intent(ActivityScanneMyQR.this,ActivityLoginNumber.class)));

        findViewById(R.id.verifier).setOnClickListener(view -> {

            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(matricule));
            startActivity(browser);


        });



    }

    void init(){
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        intent = getIntent();
        matricule = intent.getStringExtra("matricule");
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        genre = findViewById(R.id.genre);
        qrcode = findViewById(R.id.qrcode);
        qrcodedeux = findViewById(R.id.qrcodedeux);
        line_connection = findViewById(R.id.creation_compte);
        linearLayout_compte = findViewById(R.id.is_profil);
        name = findViewById(R.id.name);
        img_profil = findViewById(R.id.img_utilisateur);
        linearLayout = findViewById(R.id.infos_deux);
        linearLayout_deux = findViewById(R.id.infos_trois);
        progressBar = findViewById(R.id.progress_bar);
        setprofil();
        generecode();
    }

    void createRequest(){
        /**GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
         .requestIdToken(getString(R.string.default_web_client_id))
         .requestEmail()
         .build();

         googleSignIn = GoogleSignIn.getClient(this,gso);*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("381661792229-g1bevm0uuins0bv1bsk1ljkn4pjtivqi.apps.googleusercontent.com")
                .requestEmail()
                .build();


//        610072751390-uf2fk2ip1eojoal1prvjptvmpsujhplv.apps.googleusercontent.com

        googleSignIn = GoogleSignIn.getClient(this,gso);
        /** GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
         if (signInAccount != null){
         Toast.makeText(this, "User is Logger in Already", Toast.LENGTH_SHORT).show();
         }*/
    }

    void signIn(){
        Intent intent = googleSignIn.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }


    void setprofil(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("PATIENTS_VACCINE").whereEqualTo("matricule",matricule)
                .get().addOnCompleteListener(task -> {
            if (task.getResult().size() == 0){
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                linearLayout_deux.setVisibility(View.VISIBLE);
                generecodedeux(matricule);
                profil_publiant(img_profil,name);
                ProgressBar Myprogrss = findViewById(R.id.progres);
                Myprogrss.setVisibility(View.VISIBLE);
                WebView webView = (WebView) findViewById(R.id.mywebview);
                webView.setVisibility(View.VISIBLE);
                ImageView imageView = findViewById(R.id.imageweb);
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webView.buildDrawingCache();
                //webView.setWebViewClient(new WebViewClient());
                webView.setWebViewClient(new WebViewClient() {


                    public void onPageFinished(WebView v, String url) {
                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

                        if (v.isShown()){
                            Picture picture = v.capturePicture();
                            if (picture.getHeight() <= 0 || picture.getWidth() <= 0){
                                Toast.makeText(ActivityScanneMyQR.this, "Erreur", Toast.LENGTH_SHORT).show();
                            }else {

                                Bitmap bmp = Bitmap.createBitmap(
                                        picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);

                                Canvas canvas = new Canvas(bmp);
                                picture.draw(canvas);
                                imageView.setImageBitmap(bmp);
                                Toast.makeText(ActivityScanneMyQR.this, ""+bmp, Toast.LENGTH_SHORT).show();
                                TextView textView = findViewById(R.id.txt_image);
                                textView.setText(""+bmp);




                            }


                        }else {

                        }
                    }


                    @Override
                    public void onPageCommitVisible(WebView view, String url) {
                        super.onPageCommitVisible(view, url);
                        Myprogrss.setVisibility(View.GONE);
                    }
                });




                // CookieManager.getInstance().setAcceptThirdPartyCookies(webView,true);
                webView.loadUrl(Uri.decode(matricule));



//                        Bitmap bmap = imageView.getDrawingCache();
//                        imageView.setImageBitmap(bmap);







            }
            if (task.isSuccessful()){
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    if (documentSnapshot.exists()){
                        Patient profilUser = documentSnapshot.toObject(Patient.class);
                        profilUser.setId(documentSnapshot.getId());
                        progressBar.setVisibility(View.GONE);
                        linearLayout_deux.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        nom.setText(profilUser.getNom());
                        prenom.setText(profilUser.getPrenom());
                        genre.setText(profilUser.getSexe());


                    }else{


                    }

                }
            }

        });
    }



    void generecode(){
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = writer.encode(matricule, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcode.setImageBitmap(bitmap);

            //InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //manager.hideSoftInputFromWindow(matricule.getApplicationWindowToken())
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    void profil_publiant(ImageView img_profil, TextView postant){
        FirebaseAuth auth ;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser == null){
            linearLayout_compte.setVisibility(View.GONE);
            line_connection.setVisibility(View.VISIBLE);
        }else{
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("UserProfil").document(firebaseUser.getUid())
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        linearLayout_compte.setVisibility(View.VISIBLE);
                        line_connection.setVisibility(View.GONE);
                        ProfilUser profilUser = documentSnapshot.toObject(ProfilUser.class);
                        if(profilUser.getImg_url().equals("") || profilUser.getImg_url() == null){
                            img_profil.setImageResource(R.drawable.avatar);
                            postant.setText(profilUser.getName());
                        }else{
                            postant.setText(profilUser.getName());
                            String urlimage = profilUser.getImg_url();
                            img_profil.setImageResource(R.color.grise_flou_flou);
                            if (signInAccount != null){
                                Uri uri = Uri.parse(firebaseUser.getPhotoUrl().toString());
                                GlideApp.with(this)
                                        .load(uri)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .skipMemoryCache(true)
                                        .into(img_profil);
                            }else{
                                StorageReference ImageProfilref = FirebaseStorage.getInstance().getReference().child(urlimage);
                                GlideApp.with(getApplicationContext())
                                        .load(ImageProfilref)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .skipMemoryCache(true)
                                        .into(img_profil);
                            }

                        }
                    }else{
                        // detec if account not profil



                    }


                }
            });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount idToken) {
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(getApplicationContext(), ActivityCustomCompte.class);
                        intent.putExtra("profil","noprofil");
                        startActivity(intent);
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "signInWithCredential:failure", task.getException());
                        //ASnackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        //updateUI(null);
                    }

                    // ...
                });
    }




}