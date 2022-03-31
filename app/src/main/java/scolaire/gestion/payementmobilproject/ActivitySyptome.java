package scolaire.gestion.payementmobilproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ActivitySyptome extends AppCompatActivity {

    private static AdapterActualliter adapterStudent ;
    private static List<Actualliter> list ;
    public static LinearLayoutManager manager_deux ;
    RecyclerView recyclerView ;
    FirebaseFirestore firebaseFirestore ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syptome);

        recyclerView = findViewById(R.id.recycler_actu);
        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();


        list.clear();
        adapterStudent = new AdapterActualliter(ActivitySyptome.this,list,true);
        manager_deux = new LinearLayoutManager(ActivitySyptome.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager_deux);
        recyclerView.setAdapter(adapterStudent);

        data();
    }

    void data(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("Actualliter").orderBy("time_publisher", Query.Direction.ASCENDING).
                addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            Actualliter actuallite = doc.getDocument().toObject(Actualliter.class);
                            actuallite.setId(doc.getDocument().getId());
                            list.add(actuallite);
                            adapterStudent.notifyDataSetChanged();
                        }
                    }
                });

    }

}