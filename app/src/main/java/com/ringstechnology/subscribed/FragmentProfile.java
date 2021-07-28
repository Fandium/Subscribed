package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ringstechnology.suscribed.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FragmentProfile extends Fragment {

  Context context = this.getContext();
  Button yes, no;
  public static View logout;
  public static RecyclerView recyclerView;
  public static ConstraintLayout popup;
  RecyclerView.Adapter adapter;
  public static RecyclerView.LayoutManager layoutManager;
  Animation fadeout;
  FirebaseFirestore data = FirebaseFirestore.getInstance();
  TextView welcome;
  ArrayList<ItemProfile> profileList;
  public static Context profileContext;
  public static Activity profileActivity;
  public FirebaseAuth mAuth = FirebaseAuth.getInstance();


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.activity_fragment_profile, container, false);

    fadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
    profileList = new ArrayList<>();


    recyclerView = rootView.findViewById(R.id.recyclerView);
    welcome = rootView.findViewById(R.id.welcome);
    recyclerView.setHasFixedSize(true);
    adapter = new AdapterProfile(profileList, this);

    popup  = rootView.findViewById(R.id.popup);
    yes = rootView.findViewById(R.id.yes);
    no = rootView.findViewById(R.id.no);


    layoutManager = new LinearLayoutManager(this.getContext());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      profileList.add(new ItemProfile(R.drawable.ic_my_services_24, "My Subscribtions"));
      profileList.add(new ItemProfile(R.drawable.ic_sign_in_24, "Sign In"));
    } else {
      welcome.setVisibility(VISIBLE);
      profileList.add(new ItemProfile(R.drawable.ic_my_services_24, "My Subscribtions"));
      profileList.add(new ItemProfile(R.drawable.ic_contact_us_24, "Contact Us"));
      //profileList.add(new ItemProfile(R.drawable.ic_currency_24, "Change Currency"));
      profileList.add(new ItemProfile(R.drawable.ic_sign_out_24, "Sign Out"));

      String userID = mAuth.getCurrentUser().getUid();

      data.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
          if (task.isSuccessful()) {
            DocumentSnapshot documentSnapshot = task.getResult();

            welcome.setText("Hello "+documentSnapshot.getString("first_name"));

          }

        }
      });
    }

    yes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(fadeout);
        mAuth.signOut();
        Intent intent = new Intent(getContext(), SignIn.class);
        //intent .putExtra("GoToProfilePage",true);
        startActivity(intent);
        MainActivity.activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
      }
    });

    no.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //v.startAnimation(fadeout);
        popup.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
      }
    });


    return rootView;
  }
}