package com.example.user_ongc.ui.about;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user_ongc.R;

import java.util.ArrayList;
import java.util.List;


public class AboutFragment extends Fragment {
    private ViewPager viewPager;
    private BranchAdapter adapter;
    private List<BranchModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        list = new ArrayList<>();
        list.add(new BranchModel(R.drawable.ic_baseline_settings,"Business Development","Business Development & Joint Ventures Group of ONGC (BD&JV) is organization’s link to driving value integration in hydrocarbon molecule beyond the E&P domain and to identifying and developing futuristic energy portfolio for the company to ensure sustainability of the organization. The Group is headed by Director (HR) as the Director-Incharge of Business Development and Joint Ventures.\n" +
                "\n" +
                "BD&JV is involved in continuous scanning to identify synergistic business opportunities, evaluate viability and implement these opportunities for ONGC to expand its wings in the entire value chain of hydrocarbons and other domains of energy."));

        list.add(new BranchModel(R.drawable.ic_baseline_people_alt_24,"Human Resources","HR policies at ONGC revolve around the basic tenet of creating a highly motivated, vibrant & self-driven team. The Company cares for each & every employee and has in-built systems to recognise & reward them periodically. Motivation plays an important role in HR Development. In order to keep its employees motivated the company has incorporated schemes such as Reward and Recognition Scheme, Grievance Handling Scheme and Suggestion Scheme."));
        adapter = new BranchAdapter(getContext(),list);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        ImageView imageView=view.findViewById(R.id.ongc_image);
        Glide.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/ongcadmin.appspot.com/o/gallery%2F%5BB%4035d616fjpg?alt=media&token=a8f58cd7-0f53-4ab6-8a65-5acbc6309c05").into(imageView);



        return view;
    }
}