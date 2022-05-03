package com.example.user_ongc.pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user_ongc.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PdfActivity extends AppCompatActivity {
    private RecyclerView pdfRecycler;
    private DatabaseReference reference;
    private List<PdfData> list;
    private PdfAdapter adapter;


    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout shimmerLayout;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfRecycler = findViewById(R.id.pdfRecycler);
        reference = FirebaseDatabase.getInstance().getReference().child("pdf");
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerLayout = findViewById(R.id.shimmer_layout);
        search = findViewById(R.id.searchText);
        getData();
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PdfData data = snapshot.getValue(PdfData.class);
                    list.add(data);
                }
                adapter = new PdfAdapter(PdfActivity.this,list);
                pdfRecycler.setLayoutManager(new LinearLayoutManager(PdfActivity.this));
                pdfRecycler.setAdapter(adapter);
                shimmerFrameLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PdfActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        search.addTextChangedListener(new TextWatcher() {
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
        });




    }
    private void filter(String text) {
        ArrayList<PdfData> filterlist = new ArrayList<>();
        for (PdfData item : list ){
            if (item.getPdfTitle().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(item);
            }
        }

        adapter.FilteredList(filterlist);
    }


    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        shimmerFrameLayout.startShimmer();
        super.onResume();
    }
}
