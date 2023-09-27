package com.ehdeeCodes.shoppyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import controller.ItemController;

public class EditItemScreen extends AppCompatActivity implements DarkMode{

    private LinearLayout edtScreenBackBTN;
    private EditText itemName, itemPrice, itemDesc;
    private Button saveEditBTN;
    private LinearLayout descBackground;
    private ImageView ivBackArrow;

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
        ivBackArrow = findViewById(R.id.esBackArrow);

        // set icons white on dark mode On
        setIconsOnDarkMode();

        // on click to save edits made to items
        saveEditBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean nameChanged = itemController.isNameChanged(itemName, itemController.itemModelReturned(itemPosition));
                boolean priceChanged = itemController.isPriceChanged(itemPrice, itemController.itemModelReturned(itemPosition));
                boolean descChanged = itemController.isDescChanged(itemDesc, itemController.itemModelReturned(itemPosition));

                if (!nameChanged || !priceChanged || !descChanged) {
                    //check if name has been changed
                    if (!nameChanged) {
                        String newName = itemName.getText().toString();
                        itemController.editItemName(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newName);
                    }

                    //check if price has been changed
                    if (!priceChanged) {
                        int newPrice = Integer.parseInt(itemPrice.getText().toString());
                        itemController.editItemPrice(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newPrice);
                    }

                    //check if description has been changed
                    if (!descChanged) {
                        String newDesc = itemDesc.getText().toString();
                        itemController.editItemDesc(EditItemScreen.this, itemController.itemModelReturned(itemPosition), newDesc);
                    }

                    //return back to home screen
                    startActivity(new Intent(EditItemScreen.this, HomeScreen.class));
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
    public void setIconsOnDarkMode() {
        boolean isDarkThemeOn = ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        if (isDarkThemeOn){
            ivBackArrow.setImageDrawable(AppCompatResources.getDrawable(EditItemScreen.this, R.drawable.back_white));
            descBackground.setBackground(AppCompatResources.getDrawable(EditItemScreen.this, R.drawable.grey_bg_dark));
        }
    }
}