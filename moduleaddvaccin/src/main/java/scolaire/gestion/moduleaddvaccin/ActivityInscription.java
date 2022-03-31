package scolaire.gestion.moduleaddvaccin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityInscription extends AppCompatActivity {

    RelativeLayout mybtn_inscription;
    EditText adressmail, password, name;

    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    TextView txt_name;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "My tag";
    private FirebaseAuth mAuth;
    GoogleSignInClient googleSignIn;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(getApplicationContext(), ActivitySendActuallite.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        init();
        createRequest();
        findViewById(R.id.google).setOnClickListener(view -> signIn());


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mybtn_inscription = findViewById(R.id.btn_inscription);
        adressmail = findViewById(R.id.adressmail);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);

        mybtn_inscription.setOnClickListener(view -> {
            Register(adressmail.getText().toString(), password.getText().toString());
        });


    }

    void creatuserprofil() {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Votre compte a été parfaitement créé",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ActivitySendActuallite.class);
        intent.putExtra("profil", "noprofil");
        startActivity(intent);
        finish();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void init() {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mybtn_inscription = findViewById(R.id.btn_inscription);
        adressmail = findViewById(R.id.adressmail);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);

    }


    void createRequest() {
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

        googleSignIn = GoogleSignIn.getClient(this, gso);
        /** GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
         if (signInAccount != null){
         Toast.makeText(this, "User is Logger in Already", Toast.LENGTH_SHORT).show();
         }*/
    }

    void signIn() {
        Intent intent = googleSignIn.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
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
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(getApplicationContext(), ActivitySendActuallite.class);
                        intent.putExtra("profil", "noprofil");
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


    // TODO method create account user
    private void Register(String mail, String password) {
        progressDialog = new ProgressDialog(ActivityInscription.this);
        progressDialog.setMessage("Veuillez Patienté...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mAuth.createUserWithEmailAndPassword(mail.trim(), password).
                addOnCompleteListener(ActivityInscription.this, task -> {
                    if (task.isSuccessful()) {
                        creatuserprofil();
                    } else {
                        String erreur = task.getException().toString();
                        Toast.makeText(getApplicationContext(), "Erreur = " + erreur, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }

}
