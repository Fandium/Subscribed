package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ringstechnology.suscribed.R;

import java.util.HashMap;
import java.util.Map;

public class ContactUs extends AppCompatActivity {
    ActionBar actionBar;
    Animation fadeout;
    public static Context context;
    TextView instagram,twitter;
    EditText message;
    Button send;
    String Message;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ProgressDialog progressBar;
    FirebaseFirestore data = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        actionBar = getSupportActionBar();
        context = this;
        actionBar.hide();

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Sending...");
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);

        instagram  = findViewById(R.id.instagram);
        twitter  = findViewById(R.id.twitter);
        message  = findViewById(R.id.message);
        send  = findViewById(R.id.send);

        instagram.setText("https://www.instagram.com/subscribed_app/");
        Linkify.addLinks(instagram, Linkify.WEB_URLS);

        twitter.setText("https://twitter.com/Subscribed_app");
        Linkify.addLinks(twitter, Linkify.WEB_URLS);

        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                progressBar.show();
                Message = message.getText().toString().trim();
                if (Message.length() == 0){
                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Please write a message first", Toast.LENGTH_LONG).show();
                }
                else{
                    String userID = mAuth.getCurrentUser().getUid();
                    data.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                final long Id = System.currentTimeMillis();
                                sendMessage(Message,documentSnapshot.getString("first_name")+" "
                                        +documentSnapshot.getString("last_name"),documentSnapshot.getString("e-mail"),Id);
                            }
                            else{
                                progressBar.dismiss();
                                Toast.makeText(getApplicationContext(), "Error while sending", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }

            }
        });


    }

    private void sendMessage(String message, String name, String email, long Id){
        DocumentReference docRef  = FirebaseFirestore.getInstance().collection("Messages").document();
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("name", name);
        dataToSave.put("message", message);
        dataToSave.put("e-mail", email);
        dataToSave.put("id", Id);
        docRef.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    finish();
                    progressBar.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent .putExtra("GoToProfilePage",true);
                    startActivity(intent);
                    Toast.makeText(MainActivity.context, "Message sent", Toast.LENGTH_LONG).show();
                }else {
                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Simply Do noting!
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("GoToProfilePage",true);
        startActivity(intent);
    }
}