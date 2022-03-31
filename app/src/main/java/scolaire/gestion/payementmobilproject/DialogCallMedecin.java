package scolaire.gestion.payementmobilproject;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import scolaire.gestion.payementmobilproject.Database.DatabaseHelper;
import scolaire.gestion.payementmobilproject.Server.APIService;
import scolaire.gestion.payementmobilproject.Server.Client;
import scolaire.gestion.payementmobilproject.Server.Data;
import scolaire.gestion.payementmobilproject.Server.MyResponse;
import scolaire.gestion.payementmobilproject.Server.Sender;
import scolaire.gestion.payementmobilproject.Server.Token;

import static android.content.Context.MODE_PRIVATE;


public class DialogCallMedecin extends DialogFragment {


    View view ;
    LinearLayout close ;
    static String myarrondissement ;
    TextView txt_verifie ;
    private DatabaseHelper databaseHelper ;
    private APIService apiService ;

    RoundKornerRelativeLayout urgence , covid , concer , gyneco , neuro ;


    public DialogCallMedecin() {
        // Required empty public constructor
    }

    public static DialogCallMedecin newInstance(String arrondissement){
        DialogCallMedecin dialogFragmentAfrique = new DialogCallMedecin();
        dialogFragmentAfrique.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        myarrondissement = arrondissement ;
        Bundle args = new Bundle();
        dialogFragmentAfrique.setArguments(args);
        return dialogFragmentAfrique;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_callmedecin, container, false);

        view.findViewById(R.id.covid).setOnClickListener(view -> {

        });

        view.findViewById(R.id.close).setOnClickListener(view -> getDialog().dismiss());

        view.findViewById(R.id.urgence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view.findViewById(R.id.cancero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view.findViewById(R.id.gyneco).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view.findViewById(R.id.neuro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER; //psotion
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setLayout(width, height);

        }
        //  Dialog dialog = getDialog();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable insetDrawable = new InsetDrawable(back,20);
        dialog.getWindow().setBackgroundDrawable(insetDrawable);
    }





}
