package com.example.user_ongc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.user_ongc.pdf.PdfData;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchEmployee extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    MysearchAdapter mysearchAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    EditText search;

     String cpf,email,name,section,designation,phonenumber,dutypattern,bloodgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_employee);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Data!!! Please wait");
        progressDialog.show();

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search = findViewById(R.id.searchEmployee);

        db=FirebaseFirestore.getInstance();
        userArrayList= new ArrayList<User>();
        mysearchAdapter = new MysearchAdapter(SearchEmployee.this,userArrayList);

        recyclerView.setAdapter(mysearchAdapter);
        EventChangeListener();



    }

    private void EventChangeListener() {
        db.collection("users").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error !=null)
                {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error",error.getMessage());
                    return;
                }
                for(DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType()== DocumentChange.Type.ADDED){
                        userArrayList.add(dc.getDocument().toObject(User.class));

                    }

                    mysearchAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                }
            }
        });

       /* search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });*/


    }

    //private void filter(String text) {
    //}


}