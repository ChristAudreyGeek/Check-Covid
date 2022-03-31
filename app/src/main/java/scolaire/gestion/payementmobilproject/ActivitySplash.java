package scolaire.gestion.payementmobilproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivitySplash extends AppCompatActivity {

    ViewPager viewPager;
    AdapterViewPager adapterViewPager;
    TabLayout tableLayout;
    int position ;
    LinearLayout linearLayout_suivant , linearLayout_commencer;
    Animation linear_animation ;

    private TextView txt_deux , txt_une ;
    Myasyntask myasyntask ;
    RelativeLayout relativeLayout_introduction , relativeLayout_splash ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        relativeLayout_introduction = findViewById(R.id.relative_introduction);
        relativeLayout_splash = findViewById(R.id.relativei_splash);

        if (restorprefdata()){
            relativeLayout_introduction.setVisibility(View.GONE);
            relativeLayout_splash.setVisibility(View.VISIBLE);
            txt_une = findViewById(R.id.nomapplication);
            txt_deux = findViewById(R.id.version);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mytransition);
            txt_deux.startAnimation(animation);
            txt_une.startAnimation(animation);
            myasyntask = new Myasyntask();
            myasyntask.execute();
        }else{
            relativeLayout_splash.setVisibility(View.GONE);
            relativeLayout_introduction.setVisibility(View.VISIBLE);
        }

//        getSupportActionBar().hide();
        tableLayout = findViewById(R.id.tab_layout);
        linearLayout_commencer = findViewById(R.id.commencer);
        linear_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.linear_animation);


        List<Screem> screemList = new ArrayList<>();
        screemList.add(new Screem("COVID Certificats",getResources().getString(R.string.descript_une),"L'APPLICATION",R.drawable.image_une,""));
        screemList.add(new Screem("Conserver numeriquement les certificats COVID",getResources().getString(R.string.descript_deux),"AVANTAGES",R.drawable.image_deux,""));
        screemList.add(new Screem("Presenter facilement vos certificats",getResources().getString(R.string.descript_trois),"AVANTAGES",R.drawable.image_trois,getResources().getString(R.string.descript_trois_trois)));
        screemList.add(new Screem("Vos données restent dans l'application",getResources().getString(R.string.descript_quatre),"PROTECTION DES DONNEES",R.drawable.image_quatre,""));


        viewPager = findViewById(R.id.view_pager);
        adapterViewPager = new AdapterViewPager(ActivitySplash.this,screemList);
        viewPager.setAdapter(adapterViewPager);
        tableLayout.setupWithViewPager(viewPager);

        linearLayout_suivant = findViewById(R.id.linear_commencer);
        linearLayout_suivant.setOnClickListener(view -> {
            position = viewPager.getCurrentItem();
            if (position < screemList.size()){
                position ++;
                viewPager.setCurrentItem(position);

            }

            if (position == screemList.size()-1){
                loadLastScreem();
            }
        });

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == screemList.size()-1){
                    loadLastScreem();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        linearLayout_commencer.setOnClickListener(view -> {
            Intent intent ;
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                intent = new Intent(getApplicationContext(), MainActivity.class);
            }else{
                intent = new Intent(getApplicationContext(),MainActivity.class);
            }
            startActivity(intent);
            saveprefdata();
            finish();
        });

        tableLayout.setVisibility(View.GONE);

    }

    private boolean restorprefdata() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenend = pref.getBoolean("isIntroOpnend",false);
        return isIntroActivityOpenend;

    }

    private void saveprefdata() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();

    }


    private boolean restorprefdata_permission() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs_quatre",MODE_PRIVATE);
        Boolean isIntroActivityOpenend = pref.getBoolean("ispermission",false);
        return isIntroActivityOpenend;
    }

    private void loadLastScreem(){
        linearLayout_suivant.setVisibility(View.INVISIBLE);
        linearLayout_commencer.setVisibility(View.VISIBLE);
        //tableLayout.setVisibility(View.VISIBLE);
        linearLayout_commencer.setAnimation(linear_animation);

    }


    public class Myasyntask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
//            dialog = ProgressDialog.show(getContext(), "", "Loading ...", true);
//            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Intent intent ;

            if (restorprefdata_permission()){
                intent = new Intent(getApplicationContext(),MainActivity.class);
            }else{

                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }else{
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                }
            }


            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }finally {
                        startActivity(intent);
                        finish();
                    }
                }
            };

            thread.start();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //   dialog.dismiss();
//            postModelList = new ArrayList<>();
//            recyclerView = view.findViewById(R.id.recycler);
//            manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//            recyclerAdampterActuallite = new AdapterAfrique(getContext(),postModelList);
//            recyclerView.setLayoutManager(manager);
//            recyclerView.setAdapter(recyclerAdampterActuallite);

        }

    }


}