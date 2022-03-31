package scolaire.gestion.payementmobilproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class ActivityLoginNumber extends AppCompatActivity {
    EditText editText_number_phone ;
    PinView pinView ;
    String codesend ;
    CountryCodePicker ccp ;
    Button btn , btn_valide_code ;
    TextView mytxt_delai ;
    LinearLayout linear_entry_number ;
    RelativeLayout linear_send_code;
    Pinview mypin ;
    CountDownTimer countDownTimer ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_number);
        init();
        String number = ccp.getFullNumberWithPlus();
        btn.setOnClickListener(view -> {
            if (TextUtils.isEmpty(editText_number_phone.getText())){
                Toast.makeText(this, "Veuillez entrer votre numero de téléphone", Toast.LENGTH_SHORT).show();
            }else{
                sendVerifierCodeToUser(number+editText_number_phone.getText().toString().trim());
            }

        });
        userIsLigger();
        btn_valide_code.setOnClickListener(view -> verifiecode(mypin.getValue()));
        /**int nombre = 1234;
         mypin.setValue(String.valueOf(nombre));*/


    }


    void init(){
        btn_valide_code = findViewById(R.id.btn_valide_code);
        editText_number_phone = findViewById(R.id.phonetext);
        mypin = (Pinview) findViewById(R.id.pinview);
        ccp = (CountryCodePicker)  findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(editText_number_phone);
        btn = findViewById(R.id.btn_send_number);
        linear_entry_number = findViewById(R.id.linear_entry_number);
        linear_send_code = findViewById(R.id.linear_send_code);
        mytxt_delai = findViewById(R.id.txt_delai);

    }



    void sendVerifierCodeToUser(String number){
        Toast.makeText(this, ""+number, Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mcallback);
    }




    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesend = s;
            linear_entry_number.setVisibility(View.GONE);
            linear_send_code.setVisibility(View.VISIBLE);


        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            /** String code = phoneAuthCredential.getSmsCode();
             if (code != null){
             pinView.setText(code);
             verifiecode(code);
             }*/
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    void verifiecode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesend,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential){
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                userIsLigger();
            }
        });
    }

    private void userIsLigger() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            startActivity(new Intent(getApplicationContext(),ActivityCustomCompte.class));
            finish();
        }
    }


}