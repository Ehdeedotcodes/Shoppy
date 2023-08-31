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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ehdeeCodes.shoppyapp.adapters.ItemAdapter;

import controller.ItemController;

public class NewItemScreen extends AppCompatActivity implements DarkMode{

    private ImageView backBTN;
    private EditText edtItemName, edtPrice, edtDescription;
    private Button addBTN;
    private LinearLayout descBackground;

    ViewModelProvider viewModelProvider;
    ItemController itemController;
    ItemAdapter itemAdapter = new ItemAdapter();

//    ArrayList<ItemModel> newItemArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_screen);

        viewModelProvider = new ViewModelProvider(NewItemScreen.this);
        itemController = viewModelProvider.get(ItemController.class);

        backBTN = findViewById(R.id.newItemBackArrow);
        edtItemName = findViewById(R.id.newNameOfItem);
        edtPrice = findViewById(R.id.newPriceOfItem);
        edtDescription = findViewById(R.id.descEditText);
        addBTN = findViewById(R.id.btnAdd);
        descBackground = findViewById(R.id.linearDesc);

        //set icons white on dark mode
        setIconsWhite();

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewItemScreen.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemName = edtItemName.getText().toString();
                String price = edtPrice.getText().toString();
                String txtDesc = edtDescription.getText().toString();

                if (!isItemNameEmpty(edtItemName) && !isItemPriceEmpty(edtPrice) && !isDescEmpty(edtDescription)){
//                    addItem(itemName, price, txtDesc);

                    String itemUUID = itemController.itemUUID();

                    boolean successAdd = itemController.addNewItem( itemUUID,itemName, price, txtDesc, NewItemScreen.this); // setting a new item to database here

                    if (successAdd){
                        Log.d("add stats", "add success");
                        itemController.allItemsAdded(NewItemScreen.this);
                        startActivity(new Intent(NewItemScreen.this, HomeScreen.class));
                        finish();
                    }
                    else {
                        Log.d("add error", "couldn't add");
                    }
                }
                else {
                    Toast.makeText(NewItemScreen.this, "Fill item details", Toast.LENGTH_SHORT).show();
                    Log.d("add error", "add failed");
                }
            }
        });
    }

    //method to check name not empty
    public boolean isItemNameEmpty(EditText name){
        String itemName = name.getText().toString();
        if (itemName.isEmpty()){
            return true;
        }
        return false;
    }

    //method to check price not empty
    public boolean isItemPriceEmpty(EditText price){
        String itemPrice = price.getText().toString();
        if (itemPrice.isEmpty()){
            return true;
        }
        return false;
    }

    //method to check description not empty
    private boolean isDescEmpty(EditText desc){
        String itemDesc = desc.getText().toString();
        if (itemDesc.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public void setIconsWhite() {
        boolean isDarkThemeOn = ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        if (isDarkThemeOn){
            backBTN.setImageDrawable(AppCompatResources.getDrawable(NewItemScreen.this, R.drawable.back_white));
            descBackground.setBackground(AppCompatResources.getDrawable(NewItemScreen.this, R.drawable.grey_bg_dark));
        }
    }
}