package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ringstechnology.suscribed.R;

import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    ActionBar actionBar;
    public static Context context;
    public static int day,month,year;
    EditText email,fname,lname,password,password2;
    public static Button datePicker;
    String Email = "",Fname= "",Lname= "",Password= "",Password2= "",dob;
    ImageButton back;
    Animation fadeout;
    Button signup;
    Spinner currency;
    String selectedCurrency;
    public static String userID;
    private FirebaseAuth mAuth;
    public String [] currencies = {"Select default currency", "USD", "EUR", "GBP", "AUD", "JPY", "AED", "AFN", "ALL", "AMD", "ANG",
            "AOA","ARS", "ATS (EURO)", "AWG", "AZN", "BAM", "BBD", "BDT", "BEF (EURO)", "BGN", "BHD", "BIF","BMD",
            "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYR", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC",
            "CUC", "CUP", "CVE", "CYP (EURO)", "CZK", "DJF", "DKK", "DMK (EURO)", "DOP", "DZD", "EEK (EURO)", "EGP",
            "ESP (EURO)", "ETB", "FIM (EURO)", "FJD", "FKP", "GEL", "GHS", "GIP", "GMD", "GNF", "GRD (EURO)", "GTQ",
            "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "IED (EURO)", "ILS", "INR", "IQD", "IRR", "ISK", "ITL (EURO)",
            "JMD", "JOD", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL",
            "LTL (EURO)", "LUF (EURO)", "LVL (EURO)", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO",
            "MTL (EURO)", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NLG (EURO)", "NOK", "NPR",
            "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PTE (EURO)", "PYG", "QAR", "RON", "RSD", "RUB",
            "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SIT (EURO)", "SKK (EURO)", "SLL", "SOS", "SRD",
            "STD", "SVC", "SYP", "SZL", "THB", "TMM", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "UYU",
            "VEB", "VND", "VUV", "WST", "XAF", "XCD", "XOF", "XPF", "YER", "ZAR", "ZMK", "ZWD"};
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        actionBar = getSupportActionBar();
        context = this;
        actionBar.hide();

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Signing up...");
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);

        mAuth = FirebaseAuth.getInstance();


        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        back =  findViewById(R.id.back);
        signup =  findViewById(R.id.signup);
        email = findViewById(R.id.id);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        datePicker =  findViewById(R.id.date);
        currency =  findViewById(R.id.currency);


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                showDatePickerDialog(v);
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,currencies
        );
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_item);
        currency.setAdapter(spinnerArrayAdapter2);
        currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view1, int pos, long id) {
                selectedCurrency = currencies[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* if (datePicker.getText() == "Date of Birth"){

        }*/

       signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               v.startAnimation(fadeout);
               progressBar.show();
               Email = email.getText().toString().trim();
               Fname = fname.getText().toString().trim();
               Lname = lname.getText().toString().trim();
               Password = password.getText().toString().trim();
               Password2 = password2.getText().toString().trim();
               dob = datePicker.getText().toString().trim();

               if (Email.length() == 0 ){
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please enter email", Toast.LENGTH_LONG).show();
               }
               else if(Fname.length() == 0){
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please enter first name", Toast.LENGTH_LONG).show();
               }
               else if(lname.length() == 0){
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please enter last name", Toast.LENGTH_LONG).show();
               }
               else if (dob.equals("Date of Birth"))
               {
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please enter date of birth", Toast.LENGTH_LONG).show();
               }
               else if (selectedCurrency.equals("Select default currency"))
               {
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please select currency", Toast.LENGTH_LONG).show();
               }
               else if(Password.length() == 0){
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please enter password", Toast.LENGTH_LONG).show();
               }
               else if(Password2.length() == 0) {
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please retype password", Toast.LENGTH_LONG).show();
               }
               else if (!(Password2.equals(Password))){
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please make sure the passwords match", Toast.LENGTH_LONG).show();
               }
               else if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                   signUp(Email,Password);
               }
               else{
                   progressBar.dismiss();
                   Toast.makeText(SignUp.this, "Please enter a valid email", Toast.LENGTH_LONG).show();
               }

           }
       });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void signUp(String ID, String Password){
        mAuth.createUserWithEmailAndPassword(ID, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        userID = mAuth.getCurrentUser().getUid();
                                        register(dob, Fname, Lname, Email,selectedCurrency);
                                        progressBar.dismiss();
                                        Toast.makeText(SignUp.this, "You have been signed up. Check your email for verification", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                        startActivity(intent);
                                        //register(dob, Fname, Lname, Email);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        finish();
                                    }
                                }
                            });
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        progressBar.dismiss();
                        Toast.makeText(SignUp.this, "You already have an account", Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar.dismiss();
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private void register(String dob, String fname, String lname, String email, String currency){
        DocumentReference docRef  = FirebaseFirestore.getInstance().collection("Users").document(userID);
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("first_name", fname);
        dataToSave.put("last_name", lname);
        dataToSave.put("e-mail", email);
        dataToSave.put("dob", dob);
        dataToSave.put("currency", currency);
        docRef.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    finish();
                }else {
                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void showDatePickerDialog(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("From Activity", "Sign Up");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    @Override
    public void onBackPressed() {
        // Simply Do noting!
        Intent intent = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}