package com.example.ruburger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ruburger.R;
import com.example.ruburger.model.Flavor;
import com.example.ruburger.model.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeverageAdapter extends RecyclerView.Adapter<BeverageAdapter.BeverageViewHolder> {
    private final Context context;
    private final List<Flavor> flavors;
    private final Map<Flavor, Size> selectedSizes = new HashMap<>();
    private final Map<Flavor, Integer> selectedQuantities = new HashMap<>();
    private final Runnable onChangeCallback;

    public interface OnBeverageClickListener {
        void onBeverageClick(Flavor flavor, int quantity, Size size);
    }

    private final OnBeverageClickListener clickListener;

    public BeverageAdapter(Context context, List<Flavor> flavors, OnBeverageClickListener clickListener, Runnable onChangeCallback) {
        this.context = context;
        this.flavors = flavors;
        this.clickListener = clickListener;
        this.onChangeCallback = onChangeCallback;

        for (Flavor flavor : flavors) {
            selectedSizes.put(flavor, Size.MEDIUM);
            selectedQuantities.put(flavor, 1);
        }
    }

    @NonNull
    @Override
    public BeverageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.beverages, parent, false);
        return new BeverageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeverageViewHolder holder, int position) {
        Flavor flavor = flavors.get(position);

        holder.beverageName.setText(flavor.name());
        int imageResId = context.getResources().getIdentifier(flavor.name().toLowerCase(), "drawable", context.getPackageName());
        holder.beverageImage.setImageResource(imageResId);

        holder.itemView.setOnClickListener(v -> {
            int qty = selectedQuantities.get(flavor);
            Size size = selectedSizes.get(flavor);
            clickListener.onBeverageClick(flavor, qty, size);
        });
    }

    @Override
    public int getItemCount() {
        return flavors.size();
    }

    static class BeverageViewHolder extends RecyclerView.ViewHolder {
        ImageView beverageImage;
        TextView beverageName;

        public BeverageViewHolder(@NonNull View itemView) {
            super(itemView);
            beverageImage = itemView.findViewById(R.id.beverageImage);
            beverageName = itemView.findViewById(R.id.beverageName);
        }
    }
}
