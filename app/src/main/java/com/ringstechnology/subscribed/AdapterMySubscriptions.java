package com.ringstechnology.subscribed;

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

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AdapterMySubscriptions extends FirestoreRecyclerAdapter<ItemMySubscription, AdapterMySubscriptions.HomeHolder> {
    Animation fadeout;
    String logo;


    public AdapterMySubscriptions(@NonNull FirestoreRecyclerOptions<ItemMySubscription> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final AdapterMySubscriptions.HomeHolder holder, final int position, @NonNull ItemMySubscription model) {
        holder.Name.setText(model.getName());
        holder.Fee.setText("Fee: "+model.getCurrency()+model.getFee());
        holder.Cycle.setText("Billed: "+model.getCycle());
        logo = model.getLogo()+".JPG";

        fadeout = AnimationUtils.loadAnimation(ListMySubscription.context, R.anim.fadeout);

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

                Intent intent = new Intent(ListMySubscription.context, ViewService.class);
                intent.putExtra("service", service);
                ListMySubscription.context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_subscription_items,
                parent, false);
        return new HomeHolder(v);
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        TextView Fee, Name, Cycle;
        android.widget.ImageView ImageView;
        ConstraintLayout Layout;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            Fee = itemView.findViewById(R.id.fee);
            Cycle = itemView.findViewById(R.id.cycle);
            ImageView = itemView.findViewById(R.id.image);
            Layout = itemView.findViewById(R.id.layout);
        }
    }
}
