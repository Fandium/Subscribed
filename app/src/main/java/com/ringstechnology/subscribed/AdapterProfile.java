package com.ringstechnology.subscribed;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ringstechnology.suscribed.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterProfile extends RecyclerView.Adapter<AdapterProfile.ProfileViewHolder>  {
    private ArrayList<ItemProfile> profileList;
    Animation  animation = AnimationUtils.loadAnimation(MainActivity.context, R.anim.fadeout);
    Activity activity = MainActivity.activity;
    String selectedView;
    FirebaseAuth mAuth;




    public static class ProfileViewHolder extends RecyclerView.ViewHolder {

         ImageView image;
         TextView text;
         ConstraintLayout layout;


        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.textView);
            layout = itemView.findViewById(R.id.layout);


        }

    }


    public AdapterProfile(ArrayList<ItemProfile> mprofileList, FragmentProfile fragmentProfile){
        profileList = mprofileList;
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_items, parent, false);
        ProfileViewHolder pvh = new ProfileViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, final int position) {
        final ItemProfile current = profileList.get(position);

        holder.image.setImageResource(current.getImage());
        holder.text.setText(current.getText());

        holder.layout.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                selectedView = current.getText();
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    if (position == 0){
                        view.startAnimation(animation);
                        Fragment fragment = new FragmentSignIn();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    else if(position == 1){
                        view.startAnimation(animation);
                        Intent intent = new Intent(MainActivity.context, SignIn.class);
                        activity.startActivity(intent);
                        MainActivity.activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
                else{
                    if (position == 0){
                        view.startAnimation(animation);
                        Intent intent = new Intent(MainActivity.context, ListMySubscription.class);
                        activity.startActivity(intent);
                        MainActivity.activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    else if(position == 1){
                        view.startAnimation(animation);
                        Intent intent = new Intent(MainActivity.context, ContactUs.class);
                        activity.startActivity(intent);
                        MainActivity.activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    else if(position == 2){
                        FragmentProfile.popup.setVisibility(view.VISIBLE);
                        FragmentProfile.recyclerView.setVisibility(view.GONE);
                        //view.startAnimation(animation);

                    }

                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public void goToPage(Fragment fragment){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

    }

    public FragmentActivity getActivity() {
        return (FragmentActivity) activity;
    }

}
