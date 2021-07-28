package com.ringstechnology.subscribed;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    String date,from;
    Context context = getContext();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        from = getArguments().getString("From Activity");

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        int actualMonth = month + 1;


        String selectedDate = ""+day+"/"+actualMonth+"/"+year;


        date = selectedDate;

        if (from.equals("Sign Up")){
            SignUp.datePicker.setText(date);
        }
        else if (from.equals("Add Service")){
            AddService.date.setText(date);
        }


        /*SignUp.day = day;
        SignUp.month = month;
        SignUp.year = year;

        int Month = month + 1;

        String date = ""+day+"/"+Month+"/"+year+"";

        SignUp.datePicker.setText(date);*/
    }
}
