package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class AddService extends AppCompatActivity {

    ImageButton back;
    ActionBar actionBar;
    String service, serviceName, serviceDate, serviceCurrency, serviceCycle, serviceFee;
    TextView name,currency;
    EditText amount;
    Animation fadeout;
    public static Button date;
    Button add;
    Spinner billCycle;
    String userID,selectedBillCycle,selectedCurrency;
    FirebaseFirestore data = FirebaseFirestore.getInstance();
    private ProgressDialog progressBar;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public String [] cycles = {"Select bill cycle","Weekly","2-weeks", "Monthly", "5-weeks",
            "Quarterly","4-months", "6-months","Annually"};
    public static Context context;
    public String [] currencies = {"Select currency", "USD", "EUR", "GBP", "AUD", "JPY", "AED", "AFN", "ALL", "AMD", "ANG",
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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        actionBar = getSupportActionBar();
        context = this;
        actionBar.hide();

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Adding");
        progressBar.setMessage("Please wait");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);

        userID = mAuth.getCurrentUser().getUid();

        service = getIntent().getExtras().get("service").toString();
        serviceName = getIntent().getExtras().get("name").toString();
        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);


        //Toast.makeText(AddService.this, service, Toast.LENGTH_LONG).show();

        name =  findViewById(R.id.name);
        back =  findViewById(R.id.back);
        billCycle = findViewById(R.id.billCycle);
        //currency =  findViewById(R.id.currency);
        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);
        add = findViewById(R.id.add);
        currency = findViewById(R.id.currency);

        String userID = mAuth.getCurrentUser().getUid();
        data.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                   currency.setText(documentSnapshot.getString("currency"));

                }

            }
        });


        name.setText(serviceName);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Toast.makeText(AddService.this, "Please DO NOT select today's date or a past date", Toast.LENGTH_LONG).show();
                showDatePickerDialog(v);

            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,cycles
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        billCycle.setAdapter(spinnerArrayAdapter);
        billCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view1, int pos, long id) {
                selectedBillCycle = cycles[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
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
        });*/

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                progressBar.show();

                serviceCurrency = currency.getText().toString();;
                serviceCycle = selectedBillCycle;
                serviceDate = date.getText().toString();
                serviceFee = amount.getText().toString().trim();

                if(serviceDate.equals("Select date")){
                    progressBar.dismiss();
                    Toast.makeText(AddService.this, "Please select date", Toast.LENGTH_LONG).show();
                }
                else if(serviceCycle.equals("Select bill cycle")){
                    progressBar.dismiss();
                    Toast.makeText(AddService.this, "Please select bill cycle", Toast.LENGTH_LONG).show();
                }
                else if(serviceFee.length() == 0){
                    progressBar.dismiss();
                    Toast.makeText(AddService.this, "Please enter amount", Toast.LENGTH_LONG).show();
                }
                else{
                    add(serviceName,serviceDate,serviceCycle,Double.parseDouble(serviceFee),serviceCurrency);
                    progressBar.dismiss();
                    Toast.makeText(AddService.this, "Service added to your subscription list", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent .putExtra("GoToProfilePage",true);
                    startActivity(intent);
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getApplicationContext(), DisplayServices.class);
                intent.putExtra("service", service);
                intent.putExtra("from", "add service");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void add(String serviceName, String date, String cycle, Double fee, String currency){
        DocumentReference docRef  = FirebaseFirestore.getInstance().collection("Users").document(userID).collection("Subs").document(service);
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("name", serviceName);
        dataToSave.put("date", date);
        dataToSave.put("cycle", cycle);
        dataToSave.put("fee", fee);
        dataToSave.put("currency", currency);
        dataToSave.put("logo", service);
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
        bundle.putString("From Activity", "Add Service");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), DisplayServices.class);
        intent.putExtra("service", service);
        intent.putExtra("from", "add service");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}