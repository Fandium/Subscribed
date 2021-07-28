package com.ringstechnology.subscribed;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ringstechnology.suscribed.R;

public class ListService extends AppCompatActivity {

    Animation fadeout;
    AdapterServices adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference services = db.collection("Services");
    RecyclerView recyclerView;
    Query query;
    public static Context context;
    ImageButton back;
    ActionBar actionBar;
    public static Activity activity;
    private AdView mAdView;
    ConstraintLayout ad;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        actionBar = getSupportActionBar();
        context = this;
        activity = this;
        actionBar.hide();

        ad = findViewById(R.id.ad);

        mAdView = findViewById(R.id.adView);

        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        recyclerView = findViewById(R.id.serviceView);
        back =  findViewById(R.id.back);

        MobileAds.initialize(this, "ca-app-pub-2655831736299916~3717915517");


        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                ad.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        category = getIntent().getExtras().get("Category").toString();

        if (category.equals("All")){
            query = services.orderBy("logo");
        }
        else {
            query = services.whereEqualTo("category", category).orderBy("logo");
        }

        setUpAdapter(query);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent .putExtra("GoToAddPage",true);
                startActivity(intent);
            }
        });
    }

    private void setUpAdapter(Query query) {
        FirestoreRecyclerOptions<ItemServices> options = new FirestoreRecyclerOptions.Builder<ItemServices>()
                .setQuery(query, ItemServices.class)
                .build();

        adapter = new AdapterServices(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        // Simply Do noting!
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("GoToAddPage",true);
        startActivity(intent);
    }
}
