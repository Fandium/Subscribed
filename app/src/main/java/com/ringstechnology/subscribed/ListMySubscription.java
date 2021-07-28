package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ringstechnology.suscribed.R;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class ListMySubscription extends AppCompatActivity {

    Animation fadeout;
    AdapterMySubscriptions adapter;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference services;
    String userID,currency;
    TextView totalView,totalMonthView;
    RecyclerView recyclerView;
    Query query;
    public static Context context;
    long complete =0;
    double total = 0.0;
    ArrayList<Long> UserServices = new ArrayList<>();
    ImageButton back;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscription);

        actionBar = getSupportActionBar();
        context = this;
        actionBar.hide();

        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        userID = mAuth.getCurrentUser().getUid();
        services = db.collection("Users").document(userID).collection("Subs");

        recyclerView = findViewById(R.id.serviceView);
        back =  findViewById(R.id.back);
        totalView = findViewById(R.id.total);
        totalMonthView = findViewById(R.id.totalMonth);

        query = services.orderBy("logo");

        setUpAdapter(query);

        addBills();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent .putExtra("GoToProfilePage",true);
                startActivity(intent);
            }
        });
    }

    private void setUpAdapter(Query query) {
        FirestoreRecyclerOptions<ItemMySubscription> options = new FirestoreRecyclerOptions.Builder<ItemMySubscription>()
                .setQuery(query, ItemMySubscription.class)
                .build();

        adapter = new AdapterMySubscriptions(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }

    public void addBills(){
        userID = mAuth.getCurrentUser().getUid();
        db.collection("Users").document(userID).collection("Subs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double fee = (double) document.get("fee");
                                if (document.getString("cycle").equals("Annually")){
                                    total = total + (fee/12);
                                }else if(document.getString("cycle").equals("Monthly")) {
                                    total = total + fee;
                                }
                                else if(document.getString("cycle").equals("Weekly")) {
                                    total = total + (fee * 4);
                                }
                                else if(document.getString("cycle").equals("2-weeks")) {
                                    total = total + (fee * 2);
                                }
                                else if(document.getString("cycle").equals("Quarterly")) {
                                    total = total + (fee/3);
                                }
                                else if(document.getString("cycle").equals("5-weeks")) {
                                    total = total + (fee - 7);
                                }
                                else if(document.getString("cycle").equals("4-months")) {
                                    total = total + (fee/4);
                                }
                                else if(document.getString("cycle").equals("6-months")) {
                                    total = total + (fee/6);
                                }
                                currency = document.getString("currency");
                                //complete = complete + fee;

                            }
                            if (total != 0){
                                //totalView.setText("Total fee: "+currency+complete);
                                //totalView.setVisibility(VISIBLE);
                                totalMonthView.setText("Average monthly fee: "+currency+total);
                                totalMonthView.setVisibility(VISIBLE);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
        intent .putExtra("GoToProfilePage",true);
        startActivity(intent);
    }
}