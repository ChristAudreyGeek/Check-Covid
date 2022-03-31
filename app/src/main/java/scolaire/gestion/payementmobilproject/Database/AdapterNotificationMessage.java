package scolaire.gestion.payementmobilproject.Database;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import scolaire.gestion.payementmobilproject.ActivityScanneMyQR;
import scolaire.gestion.payementmobilproject.R;


public class AdapterNotificationMessage extends RecyclerView.Adapter<AdapterNotificationMessage.MyViewHolder> {

    private Context context ;
    private List<NotificationData> statusList ;
    private View view ;
    String myid ;
    boolean myverifie ;
    String lastmessage ;
    ArrayList abonner , time ;

    public AdapterNotificationMessage(Context context, ArrayList abonner, ArrayList time) {
        this.context = context;
        this.abonner = abonner;
        this.time = time;
    }

    public AdapterNotificationMessage(Context context, List<NotificationData> statusList) {
        this.context = context;
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_notification,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//        String name_abobonement = classe.getAbonnement();
//        holder.mynom.setText("Numéro d'abonnement "+name_abobonement);
//        holder.time.setText(classe.getTime());

        holder.mynom.setText(""+abonner.get(position));
        holder.time.setText(""+time.get(position));
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ActivityScanneMyQR.class);
//                intent.putExtra("matricule",String.valueOf(abonner.get(position)));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return abonner.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mynom , time ;
        private View view ;
        private LinearLayout click ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView ;
            mynom = view.findViewById(R.id.number_abonnement);
            time = view.findViewById(R.id.time);
            click = view.findViewById(R.id.click);
        }

    }
}
