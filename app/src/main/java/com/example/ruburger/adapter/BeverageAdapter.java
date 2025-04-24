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

/**
 * @author Natalia Peguero, Olivia Kamau
 * This Beverage Adapter handles the recycler view for all of the 15 flavors in the beverage class
 */
public class BeverageAdapter extends RecyclerView.Adapter<BeverageAdapter.BeverageViewHolder> {
    private final Context context;
    private final List<Flavor> flavors;
    private final Map<Flavor, Size> selectedSizes = new HashMap<>();
    private final Map<Flavor, Integer> selectedQuantities = new HashMap<>();
    private final Runnable onChangeCallback;

    /**
     * Nested class Interface for the listener
     */
    public interface OnBeverageClickListener {

        /**
         * Sets upt he click listener for the beverage
         * @param flavor the flavor of the beverage
         * @param quantity the quantity of the beverage
         * @param size the size of the beverage
         */
        void onBeverageClick(Flavor flavor, int quantity, Size size);
    }

    private final OnBeverageClickListener clickListener;

    /**
     * Constructs the beverage adapter and iterates through the flavors to add a default selection of medium size and quantity of 1
     * @param context the context of the beverage
     * @param flavors the flavor
     * @param clickListener the click listner
     * @param onChangeCallback on call change back
     */
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

    /**
     * Creates a view holder for all of the beverages
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return returns a new instance of a beverageViewHolder
     */
    @NonNull
    @Override
    public BeverageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.beverages, parent, false);
        return new BeverageViewHolder(view);
    }

    /**
     * Sets up all of the images of the beverage based off the data in the enum class, finds the image in drawable based off the name, and adds the photo
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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

    /**
     * Gets the item count of the flavors
     * @return returns the count the flavors
     */
    @Override
    public int getItemCount() {
        return flavors.size();
    }

    /**
     * @author Natalia Peguero, Olivia Kamau
     * This class represents each recycler viewer for each beverage
     */
    static class BeverageViewHolder extends RecyclerView.ViewHolder {
        ImageView beverageImage;
        TextView beverageName;

        /**
         * Constructs each beverage view
         * @param itemView the item view to set
         */
        public BeverageViewHolder(@NonNull View itemView) {
            super(itemView);
            beverageImage = itemView.findViewById(R.id.beverageImage);
            beverageName = itemView.findViewById(R.id.beverageName);
        }
    }
}
