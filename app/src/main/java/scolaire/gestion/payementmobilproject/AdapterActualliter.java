package scolaire.gestion.payementmobilproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class AdapterActualliter extends RecyclerView.Adapter<AdapterActualliter.MyViewHolder> {

    private Context context ;
    private List<Actualliter> statusList ;
    private View view ;
    FirebaseFirestore firebaseFirestore  , firestore;
    StorageReference reference ;
    FirebaseUser firebaseUser ;
    String myid ;
    boolean myverifie ;
    String lastmessage ;



    public AdapterActualliter(Context context, List<Actualliter> statusList, boolean verifie) {
        this.context = context;
        this.statusList = statusList;
        this.myverifie = verifie ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_actualliter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = FirebaseStorage.getInstance().getReference();
        final Actualliter classe = statusList.get(position);
        String title = classe.getTitle_actualliter();
        String description = classe.getDescription();
        holder.title.setText(title);
        holder.description.setText(description);
        holder.imageactualliter(context,classe.getImg_post());
        holder.setImageProfil(context,classe.getImg_source());
        holder.name_school.setText("Source "+classe.getName_source());
        holder.mylinear.setOnClickListener(view -> {
            Intent intent = new Intent(context, ActivityDetailsPost.class);
            intent.putExtra("description",description);
            intent.putExtra("img",classe.getImg_post());
            intent.putExtra("title",title);
            intent.putExtra("id_post",classe.getId());
            intent.putExtra("source",classe.getName_source());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        });





        holder.icon_commentaire.setOnClickListener(view -> {
            Intent intent = new Intent(context, ActivityDetailsPost.class);
            intent.putExtra("description",description);
            intent.putExtra("img",classe.getImg_post());
            intent.putExtra("title",title);
            intent.putExtra("id_post",classe.getId());
            intent.putExtra("lien",classe.getLien_source());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        });

    }





    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title , name_school , description  ;
        private RoundKornerLinearLayout mylinear ;
        private ImageView img_post ;
        private CircleImageView img_ecole ;
        ImageView icon_commentaire ;
        private View view ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView ;
            title = view.findViewById(R.id.title);
            mylinear = view.findViewById(R.id.cardview);
            name_school = view.findViewById(R.id.name_ecole);
            icon_commentaire = view.findViewById(R.id.icon_commentaire);
            description = view.findViewById(R.id.description);
            img_ecole = view.findViewById(R.id.image_ecole);
            img_post = view.findViewById(R.id.image_post);
        }


        void setImageProfil(Context context,String urlimage){
            img_ecole.setImageResource(R.color.grise_flou_flou);
            StorageReference ImageProfilref = FirebaseStorage.getInstance().getReference().child(urlimage);
            GlideApp.with(context.getApplicationContext())
                    .load(ImageProfilref)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(img_ecole);

        }


        public void imageactualliter(Context context , String Url){
            img_post.setImageResource(R.color.grise_flou_flou);
            StorageReference ImageProfilref = FirebaseStorage.getInstance().getReference().child(Url);
            GlideApp.with(context.getApplicationContext())
                    .load(ImageProfilref)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(img_post);

        }

//
//        private void lastmessage(final String uid , final TextView thelastmessage){
//            lastmessage = "default";
//            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        Chat chat = snapshot.getValue(Chat.class);
//                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(uid) ||
//                                chat.getReceiver().equals(uid) && chat.getSender().equals(firebaseUser.getUid())){
//                            lastmessage = chat.getMessage();
//                        }
//                    }
//                    switch (lastmessage){
//                        case "default":
//                            thelastmessage.setText("");
//                            break;
//                        default:
//                            thelastmessage.setText(lastmessage);
//                            break;
//                    }
//                    lastmessage = "default";
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
    }
}
