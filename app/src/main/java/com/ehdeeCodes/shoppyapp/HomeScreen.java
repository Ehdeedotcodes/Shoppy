package com.ehdeeCodes.shoppyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ehdeeCodes.shoppyapp.adapters.ItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import controller.ItemController;
import model.ItemModel;

public class HomeScreen extends AppCompatActivity implements DarkMode{

    private FloatingActionButton addFAB;
    private ImageView historyBTN, imgEmptyState;
    private RecyclerView itemRecView;
    private TextView txtEmptyState, txtTotalPrice, txtTotalItems;
    private View appBarBottomLine;

    private ItemAdapter itemAdapter = new ItemAdapter(new ItemAdapter.startViewItemScreen() {

        @Override
        public void startItemScreen(Intent intent, int position) {
            intent = new Intent(HomeScreen.this, ViewItemScreen.class);

            intent.putExtra("name", itemAdapter.getName());
            intent.putExtra("price", itemAdapter.getPrice());
            intent.putExtra("desc", itemAdapter.getDescription());

            intent.putExtra("itemViewPosition", position);

            startActivity(intent);
            finish();
        }

        @Override
        public void startEditItemScreen(Intent intent, int position) {

            intent = new Intent(HomeScreen.this, EditItemScreen.class);

            intent.putExtra("edtName", itemAdapter.getName());
            intent.putExtra("edtPrice", itemAdapter.getPrice());
            intent.putExtra("edtDesc", itemAdapter.getDescription());

            intent.putExtra("itemPosition", position);

            startActivity(intent);
            finish();
        }

    }, HomeScreen.this);
    private boolean isItemsEmpty;

    private ViewModelProvider viewModelProvider;
    private ItemController itemController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        addFAB = findViewById(R.id.addFAB);
        historyBTN = findViewById(R.id.btnHistory);
        itemRecView = findViewById(R.id.itemsRecView);
        imgEmptyState = findViewById(R.id.homeScreenEmptyState);
        txtEmptyState = findViewById(R.id.txtEmptyState);
        txtTotalPrice = findViewById(R.id.totalPrice);
        txtTotalItems = findViewById(R.id.totalItems);
        appBarBottomLine = findViewById(R.id.appBarBottomLine);

        //set icons to white on Dark theme activated
        setIconsWhite();


        viewModelProvider = new ViewModelProvider(this);
        itemController = viewModelProvider.get(ItemController.class);

        //set items to list
        itemController.allItemsAdded(HomeScreen.this);
        //method to remove item on swipe of recycler view item
        removeItem(itemRecView, txtTotalItems, txtTotalPrice, txtEmptyState, imgEmptyState);

        Log.d("dbList Size", "value: " + itemController.amountOfItems());

        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, HistoryScreen.class);
                startActivity(intent);
                finish();
            }
        });

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, NewItemScreen.class);
                startActivity(intent);
                finish();
            }
        });

        //setting empty state
        isItemsEmpty = checkIfListEmpty(imgEmptyState,txtTotalPrice, txtTotalItems, txtEmptyState);

        if (isItemsEmpty){
            //setting recycler view to screen
            setAdapterItems();
            setRecyclerView();
        }else Log.d("list state", "list is empty");

    }

    //method to set item background on dark mode
    public void setItemBackgroundDarkMode(){

    }



    //check if dark screen is activated
    @Override
    public void setIconsWhite(){
        boolean isDarkThemeOn = ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        if (isDarkThemeOn){
            historyBTN.setImageDrawable(AppCompatResources.getDrawable(HomeScreen.this, R.drawable.history_white));
            appBarBottomLine.setBackgroundColor(getResources().getColor(R.color.dark_grey));
        }
    }

    //method to setting adapter items
    public void setAdapterItems(){
        itemAdapter.setItemModelArrayList(itemController.itemList());
    }

    //method to set recycler view of items
    public void setRecyclerView(){
        itemRecView.setAdapter(itemAdapter);
        itemRecView.setLayoutManager(new LinearLayoutManager(HomeScreen.this, LinearLayoutManager.VERTICAL, true));
        itemRecView.setVisibility(View.VISIBLE);
    }

    //method to setEmptyState visibility
    public boolean checkIfListEmpty(ImageView imgEmptyState, TextView price, TextView itemsCount, TextView txtEmptyState){
        if (itemController.isItemListEmpty()){
            showEmptyState(imgEmptyState, txtEmptyState);
            itemsCount.setText(R.string.item_count_zero);
            price.setText(R.string.price_zero);

            Log.d("itemTable size", "bool: " + itemController.isItemListEmpty());

            return false;
        }else {

            hideEmptyState(imgEmptyState, txtEmptyState);

            setItemCounts(itemsCount);
            setItemPrice(price);

            Log.d("itemTable size", "bool: " + itemController.isItemListEmpty());

            return true;
        }
    }

    //show empty state screens
    public void showEmptyState(ImageView emptyIMG, TextView emptyTXT){
        emptyIMG.setVisibility(View.VISIBLE);
        emptyTXT.setVisibility(View.VISIBLE);
    }

    // hide empty screen items
    public void hideEmptyState(ImageView emptyIMG, TextView emptyTXT){
        emptyIMG.setVisibility(View.GONE);
        emptyTXT.setVisibility(View.GONE);
    }

    //set items count to screen
    public void setItemCounts(TextView totalItems){
        String txtPriceTotal = String.valueOf(itemController.amountOfItems());
        totalItems.setText(txtPriceTotal);
    }

    //set items count to screen
    public void setItemPrice(TextView price){
        String txtPriceTotal = String.valueOf(itemController.priceTotal());
        price.setText(txtPriceTotal);
    }

    // method to handle delete items of recycler view
    public void removeItem(RecyclerView itemsRecyclerView, TextView amountOfItems, TextView priceTotal, TextView emptyTXT, ImageView emptyIMG){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.START);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Log.d("position", "position: " + itemController.itemModelReturned(viewHolder.getAdapterPosition()));
                ItemModel itemModel = itemController.itemModelReturned(viewHolder.getAdapterPosition());
                String itemID = itemController.getItemID(viewHolder.getAdapterPosition());
                String itemName = itemController.getName(viewHolder.getAdapterPosition());
                String itemPrice = itemController.getPrice(viewHolder.getAdapterPosition());
                String itemDesc = itemController.getDesc(viewHolder.getAdapterPosition());
                long timeDeleted = itemController.timeItemDeleted();

                boolean validDelete = itemController.removeItemFromTable(itemModel, HomeScreen.this, itemID);

                if (validDelete){
                    itemController.addToHistory(itemID, itemName, itemPrice, timeDeleted, itemDesc);
                    Log.d("item description", "onSwiped: " + itemDesc);
                }

                itemAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // methods to update price and amount of items
                setItemPrice(priceTotal);
                setItemCounts(amountOfItems);

                //check if screen is empty to show empty screen
                if (itemController.isItemListEmpty()){
                    showEmptyState(emptyIMG, emptyTXT);
                }

            }
        });
        itemTouchHelper.attachToRecyclerView(itemsRecyclerView);
    }
}