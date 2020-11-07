package com.saleh.government;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
