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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ehdeeCodes.shoppyapp.adapters.ItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import controller.ItemController;
import model.ItemModel;

public class HomeScreen extends AppCompatActivity implements DarkMode{

    private FloatingActionButton addFAB;
    private ImageView  imgEmptyState, ivBackArrow;
    private RecyclerView itemRecView;
    private TextView txtEmptyState, txtTotalPrice, txtTotalItems, txtPricePlus, txtItemsPlus;
    private View appBarBottomLine;
    private LinearLayout historyBTN;

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
        txtPricePlus = findViewById(R.id.txtPlusSign);
        txtItemsPlus = findViewById(R.id.txtPlusSignItems);
        ivBackArrow = findViewById(R.id.hsBackArrow);

        //set icons to white on Dark theme activated
        setIconsOnDarkMode();

        //show plus icon if textView length of price and total items exceeds
        showPlusSign(txtTotalPrice, txtPricePlus, 7);
        showPlusSign(txtTotalItems, txtItemsPlus, 6);

        viewModelProvider = new ViewModelProvider(this);
        itemController = viewModelProvider.get(ItemController.class);

        //delete item after 7 days and update Recycler view
        boolean itemDelSuccessful = itemController.removeOverstayItem(HomeScreen.this);

        if (itemDelSuccessful){
            // methods to update price and amount of items
            setItemPrice(txtTotalPrice);
            setItemCounts(txtTotalItems);

            //set plus icon to price total text and total items if it exceeds limit
            //show plus icon if price still exceed 7 digits and item exceed 6
            showPlusSign(txtTotalPrice, txtPricePlus, 7);
            showPlusSign(txtTotalItems, txtItemsPlus, 6);

            //if last item is deleted and list is empty then set empty screen
            if (itemController.isItemListEmpty()){
                //check if screen is empty to show empty screen
                showEmptyState(imgEmptyState, txtEmptyState);
            }

        }

        //set items to list
        itemController.allItemsAdded(HomeScreen.this);
        //method to remove item on swipe of recycler view item
        removeItem(itemRecView, txtTotalItems, txtTotalPrice, txtEmptyState, imgEmptyState);

//        Log.d("dbList Size", "value: " + itemController.amountOfItems());

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
                //animate from left
                overridePendingTransition(R.anim.anim_to_bottom, R.anim.anim_to_top);
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



    //check if dark screen is activated
    @Override
    public void setIconsOnDarkMode(){
        boolean isDarkThemeOn = ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        if (isDarkThemeOn){
            ivBackArrow.setImageDrawable(AppCompatResources.getDrawable(HomeScreen.this, R.drawable.history_white));
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

//            Log.d("itemTable size", "bool: " + itemController.isItemListEmpty());

            return false;
        }else {

            hideEmptyState(imgEmptyState, txtEmptyState);

            setItemCounts(itemsCount);
            setItemPrice(price);

//            Log.d("itemTable size", "bool: " + itemController.isItemListEmpty());

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
        int limit = 6; //limit for itemCount text length
        totalItems.setText(txtPriceTotal);

        //show plus icon if text max length reached
        showPlusSign(totalItems, txtPricePlus, limit);
    }

    //set items price to screen
    public void setItemPrice(TextView price){
        String txtPriceTotal = String.valueOf(itemController.priceTotal());
        int limit = 7; //limit for price text length
        price.setText(txtPriceTotal);

        //show plus icon if text max length reached
        showPlusSign(price, txtPricePlus, limit);
    }

    //check if price display limit reached then show plus sign
    public void showPlusSign(TextView textView, TextView plusIcon, int limit){
        int textLength = textView.getText().length() + 1;
        if (textLength >= limit){
            plusIcon.setVisibility(View.VISIBLE);
        }
        else {plusIcon.setVisibility(View.GONE);}
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
                long timeDeleted = itemController.timeStamp();

                boolean validDelete = itemController.removeItemFromTable(itemModel, HomeScreen.this, itemID);

                if (validDelete){
                    itemController.addToHistory(itemID, itemName, itemPrice, timeDeleted, itemDesc);
//                    Log.d("item description", "onSwiped: " + itemDesc);
                }

                itemAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // methods to update price and amount of items
                setItemPrice(priceTotal);
                setItemCounts(amountOfItems);

//                //show plus icon if price still exceed 7 digits and item exceed 6
//                showPlusSign(txtTotalPrice, txtPricePlus, 7);
//                showPlusSign(txtTotalItems, txtItemsPlus, 6);

                //check if screen is empty to show empty screen
                if (itemController.isItemListEmpty()){
                    showEmptyState(emptyIMG, emptyTXT);
                }

            }
        });
        itemTouchHelper.attachToRecyclerView(itemsRecyclerView);
    }
}