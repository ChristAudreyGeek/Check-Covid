package scolaire.gestion.moduleaddvaccin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ActivityPatientEnregistrer extends AppCompatActivity {

    Intent intent ;
    String matricule , str_nom , str_prenom , str_genre ;
    TextView nom , prenom , genre ;
    ImageView qrcode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_enregistrer);
        init();
    }

    void init(){
        intent = getIntent();
        matricule = intent.getStringExtra("matricule");
        str_nom = intent.getStringExtra("nom");
        str_prenom = intent.getStringExtra("prenom");
        str_genre = intent.getStringExtra("genre");
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        genre = findViewById(R.id.genre);
        qrcode = findViewById(R.id.qrcode);

        nom.setText(str_nom);
        prenom.setText(str_prenom);
        genre.setText(str_genre);

        generecode();
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


}

