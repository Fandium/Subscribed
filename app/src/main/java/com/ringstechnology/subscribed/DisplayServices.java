package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ringstechnology.suscribed.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class DisplayServices extends AppCompatActivity {
  ActionBar actionBar;
  public static Context context;
  TextView about,fee,link,name,est;
  TextView About,Fee,visit,subscribed;
  Button add;
  ImageView service;
  ImageButton back;
  Animation fadeout;
  ArrayList<String> UserServices = new ArrayList<>();
  public FirebaseAuth mAuth = FirebaseAuth.getInstance();
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference services;
  FirebaseFirestore data = FirebaseFirestore.getInstance();
  ProgressDialog progressBar;
  String userID,Service,serviceName,url,image,from;
  ImageView logo;
  private AdView mAdView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_service_display);

    actionBar = getSupportActionBar();
    context = this;
    actionBar.hide();

    from = getIntent().getExtras().get("from").toString();

    if(from.equals("home")){
      overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    about = findViewById(R.id.about2);
    About = findViewById(R.id.about);
    fee = findViewById(R.id.fee2);
    Fee = findViewById(R.id.fee);
    visit = findViewById(R.id.link);
    link = findViewById(R.id.link2);
    name = findViewById(R.id.name);
    est = findViewById(R.id.est);
    service = findViewById(R.id.service);
    back =  findViewById(R.id.back);
    logo = findViewById(R.id.imageView);
    subscribed = findViewById(R.id.subscribed);
    add = findViewById(R.id.addService);
    mAdView = findViewById(R.id.adView2);

    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      subscribed.setVisibility(View.GONE);
      add.setVisibility(View.GONE);
    }
    else{
      checkServices();
    }

    //MobileAds.initialize(this, "ca-app-pub-2655831736299916~3717915517");

    AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

    mAdView.loadAd(adRequest);

    mAdView.setAdListener(new AdListener() {
      @Override
      public void onAdLoaded() {
        // Code to be executed when an ad finishes loading.
        //logo.setVisibility(View.VISIBLE);
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


    Service = getIntent().getExtras().get("service").toString();

    fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

    data.collection("Services").document(Service).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot documentSnapshot = task.getResult();

          //setImage();

          about.setText(documentSnapshot.getString("about"));
          fee.setText(documentSnapshot.getString("fee"));
          visit.setText("Click the link to join " + documentSnapshot.getString("name"));
          name.setText(documentSnapshot.getString("name"));
          est.setText("Established " + documentSnapshot.getString("est"));
          url = documentSnapshot.getString("url");
          image = documentSnapshot.getString("logo")+".JPG";


          About.setVisibility(VISIBLE);
          Fee.setVisibility(VISIBLE);
          visit.setVisibility(VISIBLE);
          link.setVisibility(VISIBLE);

          link.setText(url);
          Linkify.addLinks(link, Linkify.WEB_URLS);

          FirebaseStorage storage = FirebaseStorage.getInstance();
          StorageReference storageRef = storage.getReference();

          storageRef.child("Services/"+image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
              Picasso.get()
                      .load(uri)
                      .fit().centerInside()
                      .into(service);

              //progressBar.dismiss();
            }
          }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
              Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
          });

        }else{
          Toast.makeText(MainActivity.context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        }
      }



    });

    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(fadeout);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
      }
    });

    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(fadeout);
        Intent intent = new Intent(getApplicationContext(), AddService.class);
        serviceName = name.getText().toString();
        intent.putExtra("service", Service);
        intent.putExtra("name", serviceName);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
      }
    });




  }

  public void checkServices(){
    userID = mAuth.getCurrentUser().getUid();
    //services = db.collection("Users").document(userID).collection("Subs");

    db.collection("Users").document(userID).collection("Subs")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  for (QueryDocumentSnapshot document : task.getResult()) {
                    UserServices.add(document.getId());
                  }
                  if (UserServices.contains(Service) == true){
                    subscribed.setVisibility(VISIBLE);
                  }
                  else {
                    add.setVisibility(VISIBLE);
                  }

                } else {
                  Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();

                }
              }
            });
  }

  @Override
  public void onBackPressed() {
    // Simply Do noting!
    if(from.equals("home")){
      Intent intent = new Intent(getApplicationContext(), MainActivity.class);
      startActivity(intent);
      overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    else{
      Intent intent = new Intent(getApplicationContext(), MainActivity.class);
      intent.putExtra("GoToAddPage",true);
      startActivity(intent);
    }

  }

  public void onStart() {

    super.onStart();
  }
}
