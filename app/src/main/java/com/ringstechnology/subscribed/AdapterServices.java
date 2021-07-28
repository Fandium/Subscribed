package com.ringstechnology.subscribed;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ringstechnology.suscribed.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterServices extends FirestoreRecyclerAdapter<ItemServices, AdapterServices.HomeHolder> {
    Animation fadeout;
    String logo;
    Activity activity = ListService.activity;


    public AdapterServices(@NonNull FirestoreRecyclerOptions<ItemServices> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final HomeHolder holder, final int position, @NonNull ItemServices model) {
        holder.Name.setText(model.getName());
        holder.Category.setText(model.getCategory());
        logo = model.getLogo()+".JPG";

        fadeout = AnimationUtils.loadAnimation(ListService.context, R.anim.fadeout);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        storageRef.child("Services/"+logo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .fit().centerInside()
                        .into(holder.ImageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(fadeout);
                String service  = getItem(position).logo;
                Intent intent = new Intent(ListService.context, DisplayServices.class);

                intent.putExtra("service", service);
                intent.putExtra("from", "add");
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_items,
                parent, false);
        return new HomeHolder(v);
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        TextView Category, Name;
        android.widget.ImageView ImageView;
        ConstraintLayout Layout;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            Category = itemView.findViewById(R.id.category);
            ImageView = itemView.findViewById(R.id.image);
            Layout = itemView.findViewById(R.id.layout);
        }
    }
}






