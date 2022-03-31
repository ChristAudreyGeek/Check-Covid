package scolaire.gestion.payementmobilproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class AdapterViewPager extends PagerAdapter {

    Context context;
    List<Screem> screems;

    public AdapterViewPager(Context context, List<Screem> screems) {
        this.context = context;
        this.screems = screems;
    }

    @Override
    public int getCount() {
        return screems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutscrem = inflater.inflate(R.layout.layout_screem,null);

        ImageView imgslide = layoutscrem.findViewById(R.id.img_screem);
        TextView txt_title = layoutscrem.findViewById(R.id.title);
        TextView txt_description = layoutscrem.findViewById(R.id.description);
        TextView entete = layoutscrem.findViewById(R.id.entete);
        TextView description = layoutscrem.findViewById(R.id.description_deux);
        LinearLayout relativeLayout = layoutscrem.findViewById(R.id.myrelative);


        if(position == 1){
            Drawable img = context.getResources().getDrawable(R.drawable.lockd);
            txt_description.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
            txt_description.setPadding(0,10,0,0);
            description.setVisibility(View.GONE);
        }

        if(position == 2){
            Drawable img = context.getResources().getDrawable(R.drawable.qrcoded);
            txt_description.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
            description.setVisibility(View.GONE);
            Drawable imgd = context.getResources().getDrawable(R.drawable.check);
            description.setCompoundDrawablesWithIntrinsicBounds(null, imgd, null, null);
            description.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            relativeLayout.setLayoutParams(params);
        }

        if(position == 3){
            Drawable img = context.getResources().getDrawable(R.drawable.bouclier);
            txt_description.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
            description.setVisibility(View.GONE);
        }


        imgslide.setImageResource(screems.get(position).getImg());
        txt_title.setText(screems.get(position).getTitle());
        txt_description.setText(screems.get(position).getDescription());
        entete.setText(screems.get(position).getEntete());
        description.setText(screems.get(position).getDescription_deux());
        container.addView(layoutscrem);
        return layoutscrem;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }
}
