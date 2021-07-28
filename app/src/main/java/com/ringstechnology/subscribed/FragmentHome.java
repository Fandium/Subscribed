package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ringstechnology.suscribed.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    Context context = this.getContext();
    ImageButton s1,s2,s3,s4,s5,s6;
    Animation fadeout,fadein;
    FirebaseFirestore data = FirebaseFirestore.getInstance();
    ArrayList<String> popular = new ArrayList<>();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_home,container,false);

        s1 = rootView.findViewById(R.id.service1);
        s2 = rootView.findViewById(R.id.service2);
        s3 = rootView.findViewById(R.id.service3);
        s4 = rootView.findViewById(R.id.service4);
        s5 = rootView.findViewById(R.id.service5);
        s6 = rootView.findViewById(R.id.service6);

        fadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
        fadein = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);

        s1.startAnimation(fadein);
        s2.startAnimation(fadein);
        s3.startAnimation(fadein);
        s4.startAnimation(fadein);
        s5.startAnimation(fadein);
        s6.startAnimation(fadein);


        data.collection("Popular")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                popular.add(document.getId());
                            }
                            setImage(s1,popular.get(0));
                            setImage(s2,popular.get(1));
                            setImage(s3,popular.get(2));
                            setImage(s4,popular.get(3));
                            setImage(s5,popular.get(4));
                            setImage(s6,popular.get(5));


                        } else {
                            Toast.makeText(MainActivity.context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //Toast.makeText(MainActivity.context, popular.get(0), Toast.LENGTH_LONG).show();



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getContext(), DisplayServices.class);
                if (popular.isEmpty() == true){
                    Toast.makeText(MainActivity.context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
                else{
                    intent.putExtra("service", popular.get(0));
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getContext(), DisplayServices.class);
                if (popular.isEmpty() == true){
                    Toast.makeText(MainActivity.context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
                else{
                    intent.putExtra("service", popular.get(1));
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
            }
        });

        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getContext(), DisplayServices.class);
                if (popular.isEmpty() == true){
                    Toast.makeText(MainActivity.context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
                else{
                    intent.putExtra("service", popular.get(2));
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
            }
        });

        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getContext(), DisplayServices.class);
                if (popular.isEmpty() == true){
                    Toast.makeText(MainActivity.context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
                else{
                    intent.putExtra("service", popular.get(3));
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
            }
        });

        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getContext(), DisplayServices.class);
                if (popular.isEmpty() == true){
                    Toast.makeText(MainActivity.context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
                else{
                    intent.putExtra("service", popular.get(4));
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
            }
        });

        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getContext(), DisplayServices.class);
                if (popular.isEmpty() == true){
                    Toast.makeText(MainActivity.context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
                else{
                    intent.putExtra("service", popular.get(5));
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
            }
        });


        return rootView;
    }

    public void setImage(final ImageButton image, String name){
        name = name+".JPG";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        storageRef.child("Services/"+name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .fit().centerInside()
                        .into(image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
}
