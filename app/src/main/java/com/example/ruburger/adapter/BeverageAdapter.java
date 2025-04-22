package com.example.ruburger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruburger.R;
import com.example.ruburger.model.Beverage;
import com.example.ruburger.model.Flavor;
import com.example.ruburger.model.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeverageAdapter extends RecyclerView.Adapter<BeverageAdapter.BeverageViewHolder> {

    private final Context context;
    private final List<Flavor> flavors;
    private final Map<Flavor, Size> selectedSizes = new HashMap<>();
    private final Map<Flavor, Integer> selectedQuantities = new HashMap<>();
    private final Runnable onChangeCallback;

    private int quantity;

    public BeverageAdapter(Context context, List<Flavor> flavors, Runnable onChangeCallback) {
        this.context = context;
        this.flavors = flavors;
        this.onChangeCallback = onChangeCallback;

        for (Flavor flavor : flavors) {
            selectedSizes.put(flavor, Size.MEDIUM);
            selectedQuantities.put(flavor, 1);
        }
    }

    public List<Beverage> getSelectedBeverages() {
        List<Beverage> list = new ArrayList<>();
        for (Flavor flavor : flavors) {
            Size size = selectedSizes.get(flavor);
            int quantity = selectedQuantities.get(flavor);
            if (quantity > 0) {
                list.add(new Beverage(quantity, size, flavor));
            }
        }
        return list;
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

        ArrayAdapter<Size> sizeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, Size.values());
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.beverageSize.setAdapter(sizeAdapter);

        Size currentSize = selectedSizes.get(flavor);
        if (currentSize != null) {
            holder.beverageSize.setSelection(currentSize.ordinal());
        }

        holder.beverageSize.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {
                selectedSizes.put(flavor, Size.values()[pos]);
                onChangeCallback.run();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

    }

    @Override
    public int getItemCount() {
        return flavors.size();
    }

    static class BeverageViewHolder extends RecyclerView.ViewHolder {
        ImageView beverageImage;
        TextView beverageName;
        Spinner beverageSize;
        TextView quantityText;
        ImageButton plusBtn, minusBtn;

        public BeverageViewHolder(@NonNull View itemView) {
            super(itemView);
            beverageImage = itemView.findViewById(R.id.beverageImage);
            beverageName = itemView.findViewById(R.id.beverageName);
            beverageSize = itemView.findViewById(R.id.beverageSize);
            quantityText = itemView.findViewById(R.id.quantityText);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
        }
    }
}
