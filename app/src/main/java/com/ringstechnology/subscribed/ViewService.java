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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ringstechnology.suscribed.R;
import com.squareup.picasso.Picasso;

import static android.view.View.VISIBLE;

public class ViewService extends AppCompatActivity {

    ActionBar actionBar;
    public static Context context;
    ImageView service;
    TextView name,fee,billed,date,due;
    Button remove;
    Animation fadeout;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore data = FirebaseFirestore.getInstance();
    ProgressDialog progressBar;
    ImageView logo;
    String Service,image;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Removing...");
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);

        actionBar = getSupportActionBar();
        context = this;
        actionBar.hide();

        name = findViewById(R.id.name);
        fee = findViewById(R.id.fee);
        billed = findViewById(R.id.billed);
        date = findViewById(R.id.date);
        due = findViewById(R.id.due);
        remove = findViewById(R.id.removeService);
        service = findViewById(R.id.service);

        Service = getIntent().getExtras().get("service").toString();

        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        final String userID = mAuth.getCurrentUser().getUid();

        data.collection("Users").document(userID).collection("Subs").document(Service).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    long dueDate = (long) documentSnapshot.get("due");
                    if (dueDate == 1){
                        due.setText("Due in: "+documentSnapshot.get("due").toString()+" day");
                    }
                    else if (dueDate == 0){
                        due.setText("Due today");
                    }
                    else {
                        due.setText("Due in: "+documentSnapshot.get("due").toString()+" days");
                    }
                    //due.setText("Due in: "+documentSnapshot.get("due").toString()+" days");
                    fee.setText("Fee: "+documentSnapshot.get("currency")+documentSnapshot.get("fee"));
                    date.setText("Next bill date: " + documentSnapshot.getString("date"));
                    name.setText(documentSnapshot.getString("name"));
                    billed.setText("Billed: " + documentSnapshot.getString("cycle"));
                    image = documentSnapshot.getString("logo")+".JPG";


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

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                progressBar.show();
                data.collection("Users").document(userID).collection("Subs").document(Service).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ListMySubscription.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });

            }
        });





    }
    @Override
    public void onBackPressed() {
        // Simply Do noting!
        Intent intent = new Intent(getApplicationContext(), ListMySubscription.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}