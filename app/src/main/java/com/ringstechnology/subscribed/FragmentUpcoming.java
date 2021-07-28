package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ringstechnology.suscribed.R;

public class FragmentUpcoming extends Fragment {

    Animation fadeout;
    AdapterUpcoming adapter;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference services;
    String userID;
    RecyclerView recyclerView;
    Query query;
    public static Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_upcoming,container,false);

        fadeout = AnimationUtils.loadAnimation(MainActivity.context, R.anim.fadeout);

        userID = mAuth.getCurrentUser().getUid();
        services = db.collection("Users").document(userID).collection("Subs");

        recyclerView = rootView.findViewById(R.id.serviceView);

        query = services.orderBy("due", Query.Direction.ASCENDING);

        setUpAdapter(query);

        return rootView;
    }

    private void setUpAdapter(Query query) {
        FirestoreRecyclerOptions<ItemUpcoming> options = new FirestoreRecyclerOptions.Builder<ItemUpcoming>()
                .setQuery(query, ItemUpcoming.class)
                .build();

        adapter = new AdapterUpcoming(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context));
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

}