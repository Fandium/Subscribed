package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.ringstechnology.suscribed.R;

public class FragmentSignIn extends Fragment {

    Context context = this.getContext();
    Animation fadeout;
    Button signin;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_sign_in,container,false);

        fadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        signin = rootView.findViewById(R.id.signin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(MainActivity.context, SignIn.class);
                startActivity(intent);
                MainActivity.activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return rootView;
    }
}

