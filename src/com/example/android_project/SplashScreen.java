package com.example.android_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {

	ImageView iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		iv = (ImageView) findViewById(R.id.imgLogo);
		
		Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		iv.startAnimation(animationFadeIn);
		
		new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, Home.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, 2000); //2000 miliSeconds (i.e 2 secs)
	}
}
