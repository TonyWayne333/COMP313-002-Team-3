package com.example.onlineattendancesystem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentViewHolder extends RecyclerView.ViewHolder {

    TextView fullName;
    TextView sId;
    TextView sPresence;
    ImageView sImageUrl;

    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);

        fullName = itemView.findViewById(R.id.fullName);
        sId = itemView.findViewById(R.id.stdId);
        sPresence = itemView.findViewById(R.id.presence);
        sImageUrl = itemView.findViewById(R.id.faceImg);
    }
}