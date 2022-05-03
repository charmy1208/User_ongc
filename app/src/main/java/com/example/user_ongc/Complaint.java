package com.example.user_ongc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Complaint extends AppCompatActivity {
    ImageButton dateButton, timeButton;
    EditText dateTextView,timeTextView, cpfInputText,inputName,inputDesignation,inputLocation, inputContact, inputComplain,inputNature;
    Spinner spinnerFields;
    Button submitButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        dateButton = findViewById(R.id.dateButton);
        dateTextView = findViewById(R.id.dateTextView);
        timeButton = findViewById(R.id.timeButton);
        timeTextView = findViewById(R.id.timeTextView);
        cpfInputText = findViewById(R.id.cpfInputText);
        inputName = findViewById(R.id.inputName);
        inputDesignation = findViewById(R.id.inputDesignation);
        inputLocation = findViewById(R.id.inputLocation);
        inputContact = findViewById(R.id.inputContact);
        inputComplain = findViewById(R.id.inputComplain);
        inputNature = findViewById(R.id.inputNature);
        submitButton = findViewById(R.id.submitButton);


        spinnerFields = findViewById(R.id.spinnerFields);


        //date and time button
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

        //For spinner
        List<String> Categories = new ArrayList<>();
        Categories.add(0, "-- Choose a field --");
        Categories.add("IT Complaint");
        Categories.add("Civil and Housekeeping Complaint");
        Categories.add("Electric Complaint");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFields.setAdapter(dataAdapter);
        spinnerFields.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("-- Choose a field --")){

                }else{
                    //choose.setText(adapterView.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ////
            }
        });

        //for database
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = dateTextView.getText().toString();
                String time = timeTextView.getText().toString();
                String cpfNo = cpfInputText.getText().toString();
                String name = inputName.getText().toString();
                String designation = inputDesignation.getText().toString();
                String location = inputLocation.getText().toString();
                String contact = inputContact.getText().toString();
                String complain = inputComplain.getText().toString();
                String nature = inputNature.getText().toString();
                String selected = spinnerFields.getSelectedItem().toString();

                Map<String,Object> data = new HashMap<>();
                data.put("Date",date);
                data.put("Time",time);
                data.put("CPF No.",cpfNo);
                data.put("Name",name);
                data.put("Designation",designation);
                data.put("Location",location);
                data.put("Contact",contact);
                data.put("Complain category",complain);
                data.put("Nature of complain",nature);
                data.put("Choose field",selected);

                db.collection("First collection").document("user data").set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

                            }
                        });

            }
        });


    }

    //for date and time button
    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();

        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //  String timeString = hour + " : " + minute;
                // timeTextView.setText(timeString);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR,hour);
                calendar1.set(Calendar.MINUTE,minute);
                CharSequence charSequence = DateFormat.format("hh:mm a", calendar1);
                timeTextView.setText(charSequence);
            }
        },HOUR, MINUTE, is24HourFormat);
        timePickerDialog.show();
    }

    private void handleDateButton(){

        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String dateString = date + " - " + (month+1) + " - " + year;
                dateTextView.setText(dateString);
            }
        },YEAR, MONTH, DATE);
        datePickerDialog.show();
    }


}

