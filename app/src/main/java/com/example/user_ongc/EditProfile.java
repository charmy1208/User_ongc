package com.example.user_ongc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText profileFullname,profileemail,profilecpfno,profilesection,profiledesignation,profilephonenumber,profiledutypattern,profilebloodgroup;
    ImageView profileImageview;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    Button saveBtn;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String cpfno = data.getStringExtra("cpfno");
        String psection= data.getStringExtra("psection");
        String pdesignation= data.getStringExtra("pdesignation");
        String pphonenumber= data.getStringExtra("pphonenumber");
        String pdutypattern= data.getStringExtra("pdutypattern");
        String pbloodgroup= data.getStringExtra("pbloodgroup");


        fAuth=FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        user=fAuth.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference();

        profileFullname= findViewById(R.id.profilename);
        profileemail = findViewById(R.id.profileemail);
        profilecpfno=findViewById(R.id.profilecpf);
        profilesection=findViewById(R.id.profilesection);
        profiledesignation=findViewById(R.id.profiledesignation);
        profilephonenumber=findViewById(R.id.profilephonenumber);
        profiledutypattern=findViewById(R.id.profiledutypattern);
        profilebloodgroup=findViewById(R.id.profilebloodgroup);


        profileImageview= findViewById(R.id.editprofileimage);
        saveBtn = findViewById(R.id.saveprofile);

        StorageReference profileRef = storageReference.child("users/"+ fAuth.getCurrentUser().getUid()+ "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageview);
                //sets the photo in edit profile page
            }
        });


        profileImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
                //opens gallery in phone

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileFullname.getText().toString().isEmpty() || profileemail.getText().toString().isEmpty() || profilecpfno.getText().toString().isEmpty() ||profilesection.getText().toString().isEmpty() || profiledesignation.getText().toString().isEmpty() || profilephonenumber.getText().toString().isEmpty() || profiledutypattern.getText().toString().isEmpty() || profilebloodgroup.getText().toString().isEmpty() )
                {
                    Toast.makeText(EditProfile.this, "One or Many Fields are empty", Toast.LENGTH_SHORT).show();
                    return;

                }
                String email = profileemail.getText().toString();
                //updating the fields in database and changing the email in authentication too
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",email);
                        edited.put("name",profileFullname.getText().toString());
                        edited.put("cpf",profilecpfno.getText().toString());
                        edited.put("section",profilesection.getText().toString());
                        edited.put("designation",profiledesignation.getText().toString());
                        edited.put("phonenumber",profilephonenumber.getText().toString());
                        edited.put("dutypattern",profiledutypattern.getText().toString());
                        edited.put("bloodgroup",profilebloodgroup.getText().toString());

                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),userProfile.class));
                                finish();
                            }
                        });
                        Toast.makeText(EditProfile.this, "Email is changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        profileFullname.setText(fullName);
        profileemail.setText(email);
        profilecpfno.setText(cpfno);
        profilesection.setText(psection);
        profiledesignation.setText(pdesignation);
        profilephonenumber.setText(pphonenumber);
        profiledutypattern.setText(pdutypattern);
        profilebloodgroup.setText(pbloodgroup);

        Log.d(TAG,"onCreate" + fullName + " "+ email + " " + cpfno + " " + psection);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //uploading image to firebase storage
        StorageReference fileRef = storageReference.child("users/"+ fAuth.getCurrentUser().getUid()+ "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageview);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });

    }
}



