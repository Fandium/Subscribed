package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ringstechnology.suscribed.R;

public class SignIn extends AppCompatActivity {

    ActionBar actionBar;
    ImageButton back;
    Animation fadeout;
    EditText email,password;
    Button signin, signup, reset;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    String userID, ID, Password;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Signing in");
        progressBar.setMessage("Please wait");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);


        back =  findViewById(R.id.back);
        email = findViewById(R.id.id);
        password = findViewById(R.id.password);
        signin =  findViewById(R.id.signin);
        signup =  findViewById(R.id.signup);
        reset = findViewById(R.id.reset);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                progressBar.show();
                signIn();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                reset();
            }
        });
    }

    private void signIn() {
        ID = email.getText().toString().trim();
        Password = password.getText().toString().trim();
        if (ID.length() == 0 || Password.length() == 0) {
            progressBar.dismiss();
            Toast.makeText(SignIn.this, "Please fill in all the details", Toast.LENGTH_LONG).show();
        }
        else if (Patterns.EMAIL_ADDRESS.matcher(ID).matches()){
            mAuth.signInWithEmailAndPassword(ID, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        public static final String TAG = " ";

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signIn:success");
                                user = mAuth.getCurrentUser();
                                userID = mAuth.getCurrentUser().getUid();
                                if (user.isEmailVerified() == true){
                                    progressBar.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    //Toast.makeText(SignIn.this, getString(R.string.success), Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else{
                                    progressBar.dismiss();
                                    Toast.makeText(SignIn.this, "Please verify your email", Toast.LENGTH_LONG).show();
                                }

                            } else if (task.isComplete()) {
                                progressBar.dismiss();
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(SignIn.this, "Wrong E-mail or password", Toast.LENGTH_LONG).show();

                            }

                        }

                    });
        }
        else {
            progressBar.dismiss();
            Toast.makeText(SignIn.this, "Please enter a valid email format", Toast.LENGTH_LONG).show();
        }

    }

    private void reset() {
        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Send email");
        progressBar.setMessage("Please wait");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);
        progressBar.show();
        ID = email.getText().toString().trim();
        if (ID.length() == 0) {
            progressBar.dismiss();
            Toast.makeText(SignIn.this, "Please enter your email", Toast.LENGTH_LONG).show();
        }
        else{
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(ID)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.dismiss();
                                Toast.makeText(SignIn.this, "Password reset link sent check your email", Toast.LENGTH_LONG).show();
                            }
                            else{
                                progressBar.dismiss();
                                Toast.makeText(SignIn.this, "Error try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        // Simply Do noting!
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}