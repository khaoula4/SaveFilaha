package com.example.savefilaha;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    Button Tomap;

    public static int SPLASH_SCREEN=2500;
    private ImageView image;
    private ImageView logo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This Line will hide the status bar from the screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);



        image = findViewById(R.id.top);
        logo = findViewById(R.id.bottom);
        //Animations
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.up);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.down);

        //Set animation to elements
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                //Call next screen
                Intent intent=new Intent(MainActivity.this,Forum.class);

                startActivity(intent);



            }
        },SPLASH_SCREEN);





    }

}