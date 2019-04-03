package com.casasolarctpi.appsolar.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.casasolarctpi.appsolar.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    ImageView imageView;
    public static Context context;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        context = this;
        FirebaseApp.initializeApp(this);
        imageView = findViewById(R.id.imgSplash);
        inizialiteFirebase();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                animationSplash();
            }
        };
        new Timer().schedule(timerTask,200);


    }

    public void animationSplash(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(imageView,0,imageView.getWidth(),0,imageView.getHeight()*1.5f);
            final Animator animator1 = ViewAnimationUtils.createCircularReveal(imageView,imageView.getWidth()/2,imageView.getHeight()/2,imageView.getHeight()*1.5f,0);
            animator.setDuration(800);
            animator1.setDuration(800);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator1.start();
                }
            });

            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {

                    super.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageView.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                }
            });

            animator.start();

        }else {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                }
            };
            new Timer().schedule(timerTask,2000);
        }


    }

    private void inizialiteFirebase() {
        mAuth = FirebaseAuth.getInstance();

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Splash.this,MenuPrincipal.class);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(Splash.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
