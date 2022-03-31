package scolaire.gestion.moduleaddvaccin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.ajouter).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivityAddVaccin.class)));
        findViewById(R.id.authentification).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ActivityScanner.class)));
    }
}