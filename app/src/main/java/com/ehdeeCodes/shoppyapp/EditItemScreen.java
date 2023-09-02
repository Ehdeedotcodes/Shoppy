package com.ehdeeCodes.shoppyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import controller.ItemController;

public class EditItemScreen extends AppCompatActivity implements DarkMode{

    private ImageView edtScreenBackBTN;
    private EditText itemName, itemPrice, itemDesc;
    private Button saveEditBTN;
    private LinearLayout descBackground;

    private ViewModelProvider viewModelProvider;
    private ItemController itemController;
    private int itemPosition;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditItemScreen.this, HomeScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_screen);

        //instantiating viemodel controller
        viewModelProvider = new ViewModelProvider(this);
        itemController = viewModelProvider.get(ItemController.class);

        //Binding elements to xml
        edtScreenBackBTN = findViewById(R.id.historyBackArrow);
        itemName = findViewById(R.id.edtNameOfItem);
        itemPrice = findViewById(R.id.edtPriceOfItem);
        itemDesc = findViewById(R.id.descEditText);
        saveEditBTN = findViewById(R.id.btnSaveEdit);
        descBackground = findViewById(R.id.linearDesc);

        // set icons white on dark mode On
        setIconsWhite();

        // on click to save edits made to items
        saveEditBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if any changes have been made to item name
                if (itemController.isNameChanged(itemName, itemController.itemModelReturned(itemPosition))){
                    String newName = itemName.getText().toString();
                    itemController.editItemName(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newName);
                    Toast.makeText(EditItemScreen.this, "item updated", Toast.LENGTH_SHORT).show();
                }
                else if (itemController.isPriceChanged(itemPrice, itemController.itemModelReturned(itemPosition))){
                    int newPrice = Integer.parseInt(itemPrice.getText().toString());
                    itemController.editItemPrice(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newPrice);
                    Toast.makeText(EditItemScreen.this, "item updated", Toast.LENGTH_SHORT).show();
                }
                else if (itemController.isDescChanged(itemDesc, itemController.itemModelReturned(itemPosition))){
                    String newDesc = itemDesc.getText().toString();
                    itemController.editItemDesc(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newDesc);
                    Toast.makeText(EditItemScreen.this, "item updated", Toast.LENGTH_SHORT).show();
                }
                else if (itemController.isNameChanged(itemName, itemController.itemModelReturned(itemPosition)) &&
                        itemController.isPriceChanged(itemPrice, itemController.itemModelReturned(itemPosition)) &&
                        itemController.isDescChanged(itemDesc, itemController.itemModelReturned(itemPosition))){

                    String newName = itemName.getText().toString();
                    int newPrice = Integer.parseInt(itemPrice.getText().toString());
                    String newDesc = itemDesc.getText().toString();

                    itemController.editItemName(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newName);
                    itemController.editItemPrice(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newPrice);
                    itemController.editItemDesc(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newDesc);

                    Log.d("all items edited", "onClick: " + "all items edited");

                }
                else {
                    Log.d("Edit item status", "nothing edited");
                }

            }
        });

        //set item description text
        setEdtDetails(itemDesc, itemName, itemPrice);

        edtScreenBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditItemScreen.this, HomeScreen.class));
                finish();
            }
        });
    }
    //method to set item current details to the edit text fields before edit
    public void setEdtDetails(EditText itemNewDesc, EditText itemNewName, EditText itemNewPrice){
        Intent intent = getIntent();
        String name = intent.getStringExtra("edtName");
        String price = intent.getStringExtra("edtPrice");
        String description = intent.getStringExtra("edtDesc");
        itemPosition = intent.getIntExtra("itemPosition", 0); // set item position from adapter

        itemNewName.setText(name);
        itemNewDesc.setText(description);
        itemNewPrice.setText(price);
    }

    @Override
    public void setIconsWhite() {
        boolean isDarkThemeOn = ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        if (isDarkThemeOn){
            edtScreenBackBTN.setImageDrawable(AppCompatResources.getDrawable(EditItemScreen.this, R.drawable.back_white));
            descBackground.setBackground(AppCompatResources.getDrawable(EditItemScreen.this, R.drawable.grey_bg_dark));
        }
    }
}