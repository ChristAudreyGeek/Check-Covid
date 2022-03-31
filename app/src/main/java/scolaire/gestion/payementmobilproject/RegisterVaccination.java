package scolaire.gestion.payementmobilproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class RegisterVaccination extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    Spinner myspinner ;
    String myarrondissement ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vaccination);

        myspinner = findViewById(R.id.myspinner);
        myspinner.setOnItemSelectedListener(this);
        String[] value = {"Makélékélé","Bacongo","Poto-Poto","Moungali","Ouenzé","TalangaÏ","M'filou","Madilou","Djiri"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.layout_style,arrayList);
        myspinner.setAdapter(arrayAdapter);

        findViewById(R.id.btn_inscription).setOnClickListener(view -> {
            FragmentManager manager = getSupportFragmentManager();
            DialogRendezVous dialogFragmentAfrique = DialogRendezVous.
                    newInstance(myarrondissement);
            dialogFragmentAfrique.setCancelable(false);
            dialogFragmentAfrique.show(manager,null);
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selection = adapterView.getItemAtPosition(i).toString();
        myarrondissement = selection ;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterVaccination.this,MainActivity.class));
        finish();

    }
}