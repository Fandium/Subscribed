package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ringstechnology.suscribed.R;

import org.joda.time.Days;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView B;
    Fragment selectedFragment = null;
    public static Context context;
    public static Activity activity;
    ActionBar actionBar;
    private AdView mAdView;
    ConstraintLayout ad;
    String userID;
    ImageView logo;
    ArrayList<String> dates = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        context = this;
        activity = this;
        actionBar.hide();
        logo = findViewById(R.id.imageView);

        mAuth = FirebaseAuth.getInstance();

        ad = findViewById(R.id.ad);

        mAdView = findViewById(R.id.adView);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "getInstanceId failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String token = task.getResult().getToken();

                        String msg = getString(R.string.msg_token_fmt, token);
                    }
                });


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


        B = findViewById(R.id.navigation_view);
        B.setOnNavigationItemSelectedListener(navListen);

        Bundle extras = getIntent().getExtras();
        if(extras!=null && extras.containsKey("GoToAddPage")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentAdd()).commit();
            B.setSelectedItemId(R.id.add);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if(extras!=null && extras.containsKey("GoToProfilePage")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentProfile()).commit();
            B.setSelectedItemId(R.id.profile);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if(extras!=null && extras.containsKey("GoToNotifications")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FramentNotifications()).commit();
            B.setSelectedItemId(R.id.upcoming);

        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentHome()).commit();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null){

        }else{
            checkServices();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListen =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.home:
                            selectedFragment = new FragmentHome();
                            break;
                        case R.id.profile:
                            selectedFragment = new FragmentProfile();
                            break;
                        case R.id.upcoming:
                            selectedFragment = new FramentNotifications();
                            break;
                        case R.id.add:
                            selectedFragment = new FragmentAdd();
                            break;
                        case R.id.current:
                            if (FirebaseAuth.getInstance().getCurrentUser() == null){
                                selectedFragment = new FragmentSignIn();
                            }else{
                                selectedFragment = new FragmentUpcoming();
                            }
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public void onBackPressed() {
        // Simply Do noting!
    }

    public void checkServices(){
        userID = mAuth.getCurrentUser().getUid();
        final CollectionReference services = db.collection("Users").document(userID).collection("Subs");

        services.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Date nextBill = null;
                                long dueDate = 0;
                                String service = document.getId().toString();
                                String date = document.getString("date");
                                String cycle = document.getString("cycle");


                                Calendar c = Calendar.getInstance();
                                Date today = c.getTime();

                                SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    nextBill = format.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                long diff = nextBill.getTime() - today.getTime();
                                long seconds = diff / 1000;
                                long minutes = seconds / 60;
                                long hours = minutes / 60;
                                long days = (hours / 24);

                                if (hours == 0){
                                    Map<String, Object> dataToSave = new HashMap<String, Object>();
                                    Calendar d = Calendar.getInstance();
                                    if (cycle.equals("Weekly")){
                                        d.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                                    }
                                    else if (cycle.equals("2-weeks")){
                                        d.add(Calendar.DAY_OF_WEEK_IN_MONTH, 2);
                                    }
                                    else if (cycle.equals("Monthly")){
                                        d.add(Calendar.MONTH, 1);
                                    }
                                    else if (cycle.equals("5-weeks")){
                                        d.add(Calendar.DAY_OF_WEEK_IN_MONTH, 5);
                                    }
                                    else if (cycle.equals("Quarterly")){
                                        d.add(Calendar.MONTH, 3);
                                    }
                                    else if (cycle.equals("4-months")){
                                        d.add(Calendar.MONTH, 4);
                                    }
                                    else if (cycle.equals("6-months")){
                                        d.add(Calendar.MONTH, 6);
                                    }
                                    else if (cycle.equals("Annually")){
                                        d.add(Calendar.YEAR, 1);
                                    }



                                    String Today = format.format(d.getTime());
                                    dataToSave.put("date", Today);
                                    dataToSave.put("due", days);

                                    services.document(service).update(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }
                                else if (days != 0){
                                    Map<String, Object> dataToSave = new HashMap<String, Object>();
                                    dataToSave.put("due", days);

                                    services.document(service).update(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }

                                //dates.add(days+" days");

                                //TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                            }
                            //Toast.makeText(getApplicationContext(), dates.toString(), Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

}