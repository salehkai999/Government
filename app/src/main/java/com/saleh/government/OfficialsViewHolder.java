package com.saleh.government;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialsViewHolder extends RecyclerView.ViewHolder {

    TextView posText;
    TextView nameText;
    public OfficialsViewHolder(@NonNull View itemView) {
        super(itemView);
        posText = itemView.findViewById(R.id.positionText);
        nameText = itemView.findViewById(R.id.nameText);

    }
}
