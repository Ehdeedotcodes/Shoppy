package com.ehdeeCodes.shoppyapp.adapters;

import android.content.Context;
import android.content.res.Configuration;
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

import model.DeletedItemModel;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.viewHolder> {

    private List<DeletedItemModel> modelDelList = new ArrayList<>();
    private Context context;

    // constructors
    public HistoryAdapter(Context context){
        this.context = context;
    }

    public HistoryAdapter(){}


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deleted_item_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.price.setText(modelDelList.get(holder.getAdapterPosition()).getPrice());
        holder.name.setText(modelDelList.get(holder.getAdapterPosition()).getName());

        //method to change item background on dark mode
        changeItemBGOnDarkMode(holder.deleteRLayout, context);
//        modelDelList.get(holder.getAdapterPosition()).setId(holder.getAdapterPosition());

    }

    @Override
    public int getItemCount() {
        return modelDelList.size();
    }

    public void setModelList(List<DeletedItemModel> itemDeleteList){
        this.modelDelList = itemDeleteList;
    }

    //method to clear all items from adapter list on clear all from db
    public void clearAllItems(){
        modelDelList.clear();
    }

    //method to change item background on dark mode enabled
    public void changeItemBGOnDarkMode(RelativeLayout itemBackground, Context context) {
        boolean isDarkModeEnable = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        if (isDarkModeEnable){
            itemBackground.setBackground(AppCompatResources.getDrawable(context, R.drawable.item_round_bg_dark)); //setting bg dark
        }
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView price, txtPrice, name;
        private RelativeLayout deleteRLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.delItemPrice);
            txtPrice = itemView.findViewById(R.id.txtdelPrice);
            name = itemView.findViewById(R.id.delItemName);
            deleteRLayout = itemView.findViewById(R.id.deleteRLayout);
        }
    }

}
