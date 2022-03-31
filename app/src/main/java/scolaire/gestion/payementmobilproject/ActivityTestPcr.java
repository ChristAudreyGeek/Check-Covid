package scolaire.gestion.payementmobilproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityTestPcr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pcr);

        findViewById(R.id.sortie).setOnClickListener(view -> startActivity(new Intent(ActivityTestPcr.this,MainActivity.class)));

    }
}