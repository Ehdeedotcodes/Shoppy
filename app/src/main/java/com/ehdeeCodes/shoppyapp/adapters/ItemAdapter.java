package com.ehdeeCodes.shoppyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.ehdeeCodes.shoppyapp.R;

import java.util.ArrayList;
import java.util.List;

import model.ItemModel;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {

    private String name;
    private String price;
    private String description;
    private int clickedAdapterPosition;
    private Context context;

    public ItemAdapter(startViewItemScreen startViewItemScreen, Context context){
        this.startViewItemScreen = startViewItemScreen;
        this.context = context;
    }

    //constructor
    public ItemAdapter(){}

    private static startViewItemScreen startViewItemScreen;
    private List<ItemModel> itemModelArrayList = new ArrayList<>();

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.name.setText(itemModelArrayList.get(holder.getAdapterPosition()).getName());
        holder.price.setText(itemModelArrayList.get(holder.getAdapterPosition()).getPrice());
//        itemModelArrayList.get(holder.getAdapterPosition()).setID(holder.getAdapterPosition());

        //method setting item background on dark mode enabled
        changeItemBGOnDarkMode(holder.itemRelativeLayout, holder.itemEditBtn, holder.editIcon, context);

        //click listener to move to item view screen
        holder.itemRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                setItemDetails(holder);

                clickedAdapterPosition = holder.getAdapterPosition(); // set adapter position

                com.ehdeeCodes.shoppyapp.adapters.ItemAdapter.startViewItemScreen.startItemScreen(intent, clickedAdapterPosition);
            }
        });

        //click listener to move to edit item view screen
        holder.itemEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                //method to getDetails for specific clicked item
                setItemDetails(holder);

                clickedAdapterPosition = holder.getAdapterPosition(); // set adapter position
                Log.d("adapt position", "onClick: " + clickedAdapterPosition);

                com.ehdeeCodes.shoppyapp.adapters.ItemAdapter.startViewItemScreen.startEditItemScreen(intent, clickedAdapterPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public void setItemDetails(viewHolder holder){
        name = itemModelArrayList.get(holder.getAdapterPosition()).getName();
        price = itemModelArrayList.get(holder.getAdapterPosition()).getPrice();
        description = itemModelArrayList.get(holder.getAdapterPosition()).getDescription();
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getClickedAdapterPosition() {
        return clickedAdapterPosition;
    }

    public void setItemModelArrayList(List<ItemModel> itemModelArrayList){
        this.itemModelArrayList = itemModelArrayList;
    }

    public void notifyNewItemAdded(int position){
        notifyItemInserted(position);
    }

    public void notifyItemCleared(int position){
        notifyItemRemoved(position);
    }


    public void changeItemBGOnDarkMode(RelativeLayout itemBackground, LinearLayout edtBackground, ImageView editIcon, Context context){
        boolean isDarkModeEnable = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        if (isDarkModeEnable){
            itemBackground.setBackground(AppCompatResources.getDrawable(context, R.drawable.item_round_bg_dark)); //setting bg dark
            edtBackground.setBackground(AppCompatResources.getDrawable(context, R.drawable.rounded_bg_dark)); //setting bg dark
            editIcon.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.edit_ic_filled_white));

        }
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        private TextView name, price, txtPrice;
        private RelativeLayout itemRelativeLayout;
        private LinearLayout itemEditBtn;
        private ImageView editIcon;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.itemPrice);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            name = itemView.findViewById(R.id.itemName);
            itemEditBtn = itemView.findViewById(R.id.linEdit);
            itemRelativeLayout = itemView.findViewById(R.id.itemRLayout);
            editIcon = itemView.findViewById(R.id.edit_ic);

        }
    }
    public interface startViewItemScreen{
        public void startItemScreen(Intent intent, int position);
        public void startEditItemScreen(Intent intent, int position);
    }
}
