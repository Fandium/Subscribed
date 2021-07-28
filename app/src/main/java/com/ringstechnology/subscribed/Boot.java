package com.ringstechnology.subscribed;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ringstechnology.suscribed.R;

public class Boot extends AppCompatActivity {
    ActionBar actionBar;
    Animation animation;
    ImageView logo;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);

        actionBar = getSupportActionBar();

        logo = findViewById(R.id.logo);

        actionBar.hide();

        h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, 1500);

    }

    @Override
    public void onBackPressed() {
        // Simply Do noting!
    }

}