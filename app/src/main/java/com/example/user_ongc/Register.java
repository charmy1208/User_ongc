package com.example.user_ongc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mEmail,mPassword,mCPFno,mname,msection,mdesignation,mphonenumber,mdutypattern,mbloodgroup;
    Button mRegisterBtn;
    TextView mLoginBtn;

    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.Password);
        mCPFno=findViewById(R.id.cpfno);
        msection=findViewById(R.id.Section);
        mname=findViewById(R.id.uname);
        mdesignation=findViewById(R.id.designation);
        mphonenumber=findViewById(R.id.phoneno);
        mdutypattern=findViewById(R.id.dutypattern);
        mbloodgroup=findViewById(R.id.bloodgroup);
        mRegisterBtn=findViewById(R.id.register);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        mLoginBtn=findViewById(R.id.textView3);

        if(firebaseAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String cpfno=mCPFno.getText().toString().trim();
                String name=mname.getText().toString().trim();
                String section=msection.getText().toString().trim();
                String designation=mdesignation.getText().toString().trim();
                String phoneno=mphonenumber.getText().toString().trim();
                String dutypattern=mdutypattern.getText().toString().trim();
                String bloodgroup=mbloodgroup.getText().toString().trim();



                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(cpfno))
                {
                    mCPFno.setError("CPF number is Required");
                    return;
                }
                if(TextUtils.isEmpty(name))
                {
                    mname.setError("Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(section))
                {
                    msection.setError("Section is Required");
                    return;
                }

                if(TextUtils.isEmpty(designation))
                {
                    mdesignation.setError("Designation is Required");
                    return;
                }
                if(TextUtils.isEmpty(phoneno))
                {
                    mphonenumber.setError("Phone Number is required");
                    return;
                }
                if(TextUtils.isEmpty(dutypattern))
                {
                    mdutypattern.setError("Duty Pattern is Required");
                    return;
                }

                if(TextUtils.isEmpty(bloodgroup))
                {
                    mbloodgroup.setError("Blood Group is Required");
                    return;
                }



                if(TextUtils.isEmpty(password))
                {
                    mPassword.setError("Password is Required");
                    return;
                }

                if(phoneno.length()>10){
                    mPassword.setError("Password must be = 4 characters");
                }
                if(cpfno.length()<5){
                    mPassword.setError("Password must be = 4 characters");
                }


                if(password.length()<6){
                    mPassword.setError("Password must be >= 6 characters");
                }
                progressBar.setVisibility(View.VISIBLE);
                //register the user

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID=firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference= fStore.collection("users").document(userID);
                            Map<String,Object> user= new HashMap<>();
                            user.put("email",email);
                            user.put("cpf",cpfno);
                            user.put("name",name);
                            user.put("section",section);
                            user.put("designation",designation);
                            user.put("phonenumber",phoneno);
                            user.put("dutypattern",dutypattern);
                            user.put("bloodgroup",bloodgroup);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"User profile is created for"+userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Register.this, "Error`" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });




            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,LoginActivity.class));
            }
        });

    }
}