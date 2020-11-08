package com.saleh.government;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficialsAdapter  extends RecyclerView.Adapter<OfficialsViewHolder> {

    private List<Officals> officalsList;
    private MainActivity mainActivity;

    public OfficialsAdapter(List<Officals> officalsList, MainActivity mainActivity) {
        this.officalsList = officalsList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public OfficialsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
        //item.setOnLongClickListener(mainActivity);
        item.setOnClickListener(mainActivity);
        return new OfficialsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialsViewHolder holder, int position) {
        Officals officals = officalsList.get(position);
        holder.nameText.setText(officals.getName()+" ("+officals.getParty()+")");
        holder.posText.setText(officals.getPosition());


    }

    @Override
    public int getItemCount() {
        return officalsList.size();
    }
}
