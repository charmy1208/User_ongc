package com.example.user_ongc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class userProfile extends AppCompatActivity {
    TextView cpf,emailp,username,sectionp,designationp,phonenumberp,dutypatternp,bloodgroupp;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID;
    ImageView profileImage;
    FirebaseUser user;
    Button resetpass1;
    Button changeProfile;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        cpf=findViewById(R.id.pcpf);
        emailp=findViewById(R.id.pemail);
        username=findViewById(R.id.pname);
        sectionp=findViewById(R.id.psection);
        designationp=findViewById(R.id.pdesignation);
        phonenumberp=findViewById(R.id.pphoneno);
        dutypatternp=findViewById(R.id.pdutypattern);
        bloodgroupp=findViewById(R.id.pbloodgroup);

        resetpass1= findViewById(R.id.resetpasslocal);

        profileImage = findViewById(R.id.imageView);
        changeProfile = findViewById(R.id.changeprofile);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+ firebaseAuth.getCurrentUser().getUid()+ "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        user =firebaseAuth.getCurrentUser();

        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                emailp.setText(value.getString("email"));
                cpf.setText(value.getString("cpf"));
                username.setText(value.getString("name"));
                sectionp.setText(value.getString("section"));
                designationp.setText(value.getString("designation"));
                phonenumberp.setText(value.getString("phonenumber"));
                bloodgroupp.setText(value.getString("bloodgroup"));
                dutypatternp.setText(value.getString("dutypattern"));

            }
        });
        resetpass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetPassword = new EditText(view.getContext());
                AlertDialog.Builder passreset = new AlertDialog.Builder(view.getContext());
                passreset.setTitle("Reset Password");
                passreset.setMessage("Enter new Password > 6 characters long ");
                passreset.setView(resetPassword);
                passreset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPass=resetPassword.getText().toString();
                        user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(userProfile.this,"password has been reset",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(userProfile.this, "Error link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                passreset.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passreset.create().show();
            }
        });


        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            //open gallery
            public void onClick(View v) {
                //  Intent openGallery =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(openGallery,1000);
                Intent i = new Intent(v.getContext(),EditProfile.class);
                i.putExtra("fullName",username.getText().toString());
                i.putExtra("email",emailp.getText().toString());
                i.putExtra("cpfno",cpf.getText().toString());
                i.putExtra("psection",sectionp.getText().toString());
                i.putExtra("pphonenumber",phonenumberp.getText().toString());
                i.putExtra("pdesignation",designationp.getText().toString());
                i.putExtra("pdutypattern",dutypatternp.getText().toString());
                i.putExtra("pbloodgroup",bloodgroupp.getText().toString());

                startActivity(i);


            }
        });


    }

    /*public void log1(View view2) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }*/
}
