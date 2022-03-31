package scolaire.gestion.moduleaddvaccin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivitySplash extends AppCompatActivity {

    ViewPager viewPager;
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

        relativeLayout_introduction.setVisibility(View.GONE);
        relativeLayout_splash.setVisibility(View.VISIBLE);
        txt_une = findViewById(R.id.nomapplication);
        txt_deux = findViewById(R.id.version);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mytransition);
        txt_deux.startAnimation(animation);
        txt_une.startAnimation(animation);
        myasyntask = new Myasyntask();
        myasyntask.execute();


//        getSupportActionBar().hide();
        tableLayout = findViewById(R.id.tab_layout);
        linearLayout_commencer = findViewById(R.id.commencer);
        linear_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.linear_animation);

        linearLayout_commencer.setOnClickListener(view -> {
            Intent intent ;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
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
                intent = new Intent(getApplicationContext(),PrincipaleActivity.class);
            }else{

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    intent = new Intent(getApplicationContext(), PrincipaleActivity.class);
                }else{
                    intent = new Intent(getApplicationContext(),PrincipaleActivity.class);
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