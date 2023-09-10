package com.ehdeeCodes.shoppyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ehdeeCodes.shoppyapp.adapters.ItemAdapter;

import controller.ItemController;
import model.ItemModel;

public class ViewItemScreen extends AppCompatActivity implements DarkMode{

    private ImageView itemViewBackBTN;
    private TextView itemName, itemPrice, itemDesc;
    private Button removeItemBTN;
    private ViewModelProvider viewModelProvider;
    private ItemController itemController;
    private ItemModel itemModel = new ItemModel();
    private ItemAdapter itemAdapter = new ItemAdapter();
    private LinearLayout descBackground;

    private int itemPosition;

    //return back home activity on back button pressed
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewItemScreen.this, HomeScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_screen);

        //instantiating view model provider
        viewModelProvider = new ViewModelProvider(this);
        itemController = viewModelProvider.get(ItemController.class);

        //binding to ui components
        itemViewBackBTN = findViewById(R.id.itemBackArrow);
        itemName = findViewById(R.id.itemScrnItemName);
        itemPrice = findViewById(R.id.txtPrice2);
        itemDesc = findViewById(R.id.tvItemDesc);
        removeItemBTN = findViewById(R.id.btnRemove);
        descBackground = findViewById(R.id.linearDesc);

        //set icons white on dark mode On
        setIconsOnDarkMode();

        //method to set items in view
        setItemDetails(itemName, itemPrice, itemDesc);

        //onclick to remove item and add to history
        removeItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemID = itemController.getItemID(itemPosition);
                long timeDeleted = itemController.timeItemDeleted();
                String itemName = itemController.getName(itemPosition);
                String itemPrice = itemController.getPrice(itemPosition);
                String itemDesc = itemController.getDesc(itemPosition);
                itemModel = itemController.itemModelReturned(itemPosition);
                boolean validDelete = itemController.removeItemFromTable(itemModel, ViewItemScreen.this, itemID);

                if (validDelete){
                    Intent intent = new Intent(ViewItemScreen.this, HomeScreen.class); //start intent to return to previous screen

                    itemController.addToHistory(itemID, itemName, itemPrice, timeDeleted, itemDesc);

                    startActivity(intent); // start home screen
                    finish();
                    Log.d("item successful", "onClick: " + "item removed");
                }

                itemAdapter.notifyItemRemoved(itemPosition);
            }
        });

        itemViewBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewItemScreen.this, HomeScreen.class));
                finish();
            }
        });
    }

    //setting values received from intent
    public void setItemDetails(TextView itemName, TextView itemPrice, TextView itemDesc){
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("desc");
        itemPosition = intent.getIntExtra("itemViewPosition", 0);

        itemName.setText(name);
        itemPrice.setText(price);
        itemDesc.setText(description);
    }

    @Override
    public void setIconsOnDarkMode() {
        boolean isDarkThemeOn = ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        if (isDarkThemeOn){
            itemViewBackBTN.setImageDrawable(AppCompatResources.getDrawable(ViewItemScreen.this, R.drawable.back_white));
            descBackground.setBackground(AppCompatResources.getDrawable(ViewItemScreen.this, R.drawable.grey_bg_dark));
        }
    }
}