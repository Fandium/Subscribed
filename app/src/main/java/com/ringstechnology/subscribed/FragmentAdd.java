package com.ringstechnology.subscribed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.ringstechnology.suscribed.R;

public class FragmentAdd extends Fragment {

    Context context = this.getContext();
    ImageButton media, food, fashion, gaming, books, geek,shopping, grooming,childCare, homeCare, animalCare,
    oralCare, eyeCare, charity, fitness;
    Button all;
    Animation fadeout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_add,container,false);

        all = rootView.findViewById(R.id.all);
        media = rootView.findViewById(R.id.media);
        food = rootView.findViewById(R.id.food);
        fashion = rootView.findViewById(R.id.self);
        gaming = rootView.findViewById(R.id.gaming);
        books = rootView.findViewById(R.id.books);
        geek = rootView.findViewById(R.id.geek);
        shopping = rootView.findViewById(R.id.shopping);
        grooming = rootView.findViewById(R.id.grooming);
        childCare = rootView.findViewById(R.id.child);
        homeCare = rootView.findViewById(R.id.home);
        animalCare = rootView.findViewById(R.id.animal);
        oralCare = rootView.findViewById(R.id.oral);
        eyeCare = rootView.findViewById(R.id.eye);
        charity = rootView.findViewById(R.id.charity);
        fitness = rootView.findViewById(R.id.fitness);

        fadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                Intent intent = new Intent(getContext(), ListService.class);
                intent.putExtra("Category", "All");
                startActivity(intent);
            }
        });

        Buttons(media,"Media");
        Buttons(food,"Food");
        Buttons(fashion,"Fashion");
        Buttons(gaming,"Gaming");
        Buttons(books,"Books");
        Buttons(geek,"Geek");
        Buttons(shopping,"Shopping");
        Buttons(grooming,"Grooming");
        Buttons(childCare,"Child-care");
        Buttons(homeCare,"Home-care");
        Buttons(animalCare,"Animal-care");
        Buttons(oralCare,"Oral-care");
        Buttons(eyeCare,"Eye-care");
        Buttons(charity,"Self-care");
        Buttons(fitness,"Fitness");


        return rootView;
    }

    public void Buttons(ImageButton button, final String query){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(fadeout);
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    Fragment fragment = new FragmentSignIn();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else{
                    Intent intent = new Intent(getContext(), ListService.class);
                    intent.putExtra("Category", query);
                    startActivity(intent);
                }

            }
        });
    }
}