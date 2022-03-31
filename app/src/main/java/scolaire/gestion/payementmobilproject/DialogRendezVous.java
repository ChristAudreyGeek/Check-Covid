package scolaire.gestion.payementmobilproject;


import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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


public class DialogRendezVous extends DialogFragment {


    View view ;
    LinearLayout close ;
    static String myarrondissement ;
    TextView txt_verifie ;
    private DatabaseHelper databaseHelper ;
    private APIService apiService ;


    public DialogRendezVous() {
        // Required empty public constructor
    }

    public static DialogRendezVous newInstance(String arrondissement){
        DialogRendezVous dialogFragmentAfrique = new DialogRendezVous();
        dialogFragmentAfrique.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        myarrondissement = arrondissement ;
        Bundle args = new Bundle();
        dialogFragmentAfrique.setArguments(args);
        return dialogFragmentAfrique;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_discover, container, false);
        txt_verifie = view.findViewById(R.id.txt_confirmer);
        databaseHelper = new DatabaseHelper(getContext());
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
//        view.findViewById(R.id.close).setOnClickListener(view -> {
//            dismiss();
//            saveprefdata();
//        });

        txt_verifie.setText(myarrondissement+", êtes-vous sûr ? Vous allez recevoir une confirmation de rendez-vous dans un centre le moins saturé dans cet arrondissement");

        view.findViewById(R.id.close).setOnClickListener(view -> {
            dismiss();
//            saveprefdata();
        });

        view.findViewById(R.id.confirmer).setOnClickListener(view -> {
            sendrendezvous();
        });

        return view;
    }


    void sendrendezvous(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Action en cour");
        progressDialog.setMessage("Veuillez Patienté...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("UserProfil").whereEqualTo("uid",firebaseUser.getUid()).
                limit(1).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    ProfilUser profilUser = documentSnapshot.toObject(ProfilUser.class);
                    profilUser.setId(documentSnapshot.getId());

                    FirebaseFirestore firebaseFirestoree = FirebaseFirestore.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    final String saveCurrentDate = currentDate.format(calendar.getTime());


                    Calendar calendarTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    final String PostRendomTime = currentTime.format(calendarTime.getTime());
                    String time = PostRendomTime+"-"+saveCurrentDate;

                    Map<String,Object> map = new HashMap<>();
                    map.put("nom",profilUser.getName());
                    map.put("age",profilUser.getAge());
                    map.put("sexe",profilUser.getGenre());
                    map.put("photo",profilUser.getImg_url());
                    map.put("date",time);
                    firebaseFirestoree.collection("RENDEZVOUS").add(map).addOnCompleteListener(task1 -> {
                        sednotification();
                        databaseHelper.insertdatanotification("Votre demande a été reçu avec succès, vous serez notifié dans les 30 minutes qui suivent pour une prise de rendez-vous",time);
                        progressDialog.dismiss();
                        getDialog().dismiss();
                        startActivity(new Intent(getContext(),ActivityNotification.class));
                        // TODO ne pas oublier de mettre l'id de la centrale

                        //sendNotification("receiver",profilUser.getName(),"Vaccination "+time);
                    });

                }

            }else{

            }
        });
    }


    private void saveprefdata() {
        SharedPreferences pref = getActivity().getSharedPreferences("myPrefs_marque",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("ismarque",true);
        editor.commit();

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



    private void sendNotification(String receiver , final String username ,final String message){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,username+": "+message,"Koyekola",receiver);
                    Sender sender = new Sender(data,token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            // Toast.makeText(ActivityMessage.this, "Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    }



                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    //Toast.makeText(ActivityMessage.this, "Failed"+call, Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void sednotification(){
        String message = "Votre facture à été envoyer avec succés";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(getContext(), ActivityNotification.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid","audrey");
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),1,intent,PendingIntent.FLAG_ONE_SHOT);

            Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoSDKHuit oreoSDKHuit = new OreoSDKHuit(getContext());
            Notification.Builder builder = oreoSDKHuit.getOreoNotification("COVID-Vaccination",
                    "Vous avez été enregistré avec succès, l'heur de votre vaccination vous sera envoyé dans moins de 12 minutes",pendingIntent,defaultsound);

            int i = 0;
            int j = 1 ;
            if (j > 0){
                i = j ;
            }

            oreoSDKHuit.getNotificationManager().notify(1,builder.build());



        }else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("COVID-Vaccination")
                    .setContentText("Vous avez été enregistré avec succès, l'heur de votre vaccination vous sera envoyé dans moins de 12 minutes")
                    .setAutoCancel(true);
            Intent intent = new Intent(getContext(),ActivityNotification.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0,builder.build());



        }
    }

}
