package com.example.user_ongc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MysearchAdapter extends RecyclerView.Adapter<MysearchAdapter.MyViewHolder> {


    Context context;
    ArrayList<User> userArrayList;

    public MysearchAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MysearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MysearchAdapter.MyViewHolder holder, int position) {

        User user = userArrayList.get(position);
        holder.name.setText(user.name);
        holder.email.setText(user.email);
        holder.cpf.setText(user.cpf);

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,email,cpf;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvfirstname);
            email=itemView.findViewById(R.id.tvemail);
            cpf=itemView.findViewById(R.id.tvcpf);

        }
    }


}
