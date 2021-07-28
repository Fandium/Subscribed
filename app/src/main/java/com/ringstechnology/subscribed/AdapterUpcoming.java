package com.ringstechnology.subscribed;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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

public class AdapterUpcoming extends FirestoreRecyclerAdapter<ItemUpcoming, AdapterUpcoming.HomeHolder> {
    Animation fadeout;
    String logo;


    public AdapterUpcoming(@NonNull FirestoreRecyclerOptions<ItemUpcoming> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final HomeHolder holder, int position, @NonNull ItemUpcoming model) {
        long date = model.getDue();
        holder.Name.setText(model.getName());
        if (date == 1){
            holder.Due.setText("Due in: "+model.getDue()+" day");
        }
        else if (date == 0){
            holder.Due.setText("Due today");
        }
        else {
            holder.Due.setText("Due in: "+model.getDue()+" days");
        }
        holder.Date.setText("Next bill date: "+model.getDate());
        logo = model.getLogo()+".JPG";

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

    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_items,
                parent, false);
        return new HomeHolder(v);
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        TextView Due, Name, Date;
        android.widget.ImageView ImageView;
        ConstraintLayout Layout;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            Due = itemView.findViewById(R.id.due);
            Date = itemView.findViewById(R.id.date);
            ImageView = itemView.findViewById(R.id.image);
            Layout = itemView.findViewById(R.id.layout);
        }
    }
}
