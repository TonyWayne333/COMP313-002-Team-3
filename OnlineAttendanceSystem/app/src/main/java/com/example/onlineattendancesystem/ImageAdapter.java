package com.example.onlineattendancesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    Context context;
    ArrayList<UploadImage> uploadImage;

    public ImageAdapter(Context c, ArrayList<UploadImage> u) {
        context = c;
        uploadImage = u;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.profile_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.firstName.setText(uploadImage.get(position).getStudFirstName());
        holder.lastName.setText(uploadImage.get(position).getStudLastName());
        holder.id.setText(uploadImage.get(position).getStudId());

        Picasso.get().load(uploadImage.get(position).getsImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return uploadImage.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView firstName, lastName, id, presence;
        ImageView image;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = (TextView) itemView.findViewById(R.id.firstName);
            lastName = (TextView) itemView.findViewById(R.id.lastName);
            id = (TextView) itemView.findViewById(R.id.stdId);
            image = (ImageView) itemView.findViewById(R.id.faceImg);
        }
    }

}