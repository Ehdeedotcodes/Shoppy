package com.ehdeeCodes.shoppyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ehdeeCodes.shoppyapp.adapters.HistoryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import controller.ItemController;
import model.DeletedItemModel;

public class HistoryScreen extends AppCompatActivity implements DarkMode{

    private ImageView  imgBag, ivBackArrow;
    private final HistoryAdapter historyAdapter = new HistoryAdapter(HistoryScreen.this);
    private ItemController itemController;
    private ViewModelProvider viewModelProvider;
    private RecyclerView historyRecyView;
    private FloatingActionButton deleteAllFAB;
    private LinearLayout cancel, clear,  backBTN;
    private RelativeLayout dialogRelativeLayout;

    private TextView txtEmpty, txtDelTime, txtDialogWarn, txtCancel, txtClear;

    //return previous activity if back is pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HistoryScreen.this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_screen);

        //setting viewModelProvider
        viewModelProvider = new ViewModelProvider(HistoryScreen.this);
        itemController = viewModelProvider.get(ItemController.class);

        backBTN = findViewById(R.id.historyBackArrow);
        imgBag = findViewById(R.id.imgHistoryEmptyState);
        txtEmpty = findViewById(R.id.txtEmptyStateHistory);
        historyRecyView = findViewById(R.id.deleteItemsRecView);
        txtDelTime = findViewById(R.id.txtDeleteTime);
        deleteAllFAB = findViewById(R.id.deletAllFAB);
        ivBackArrow =findViewById(R.id.hisBackArrow);
        Dialog dialog = createDialogBox();

        dialogRelativeLayout = dialog.findViewById(R.id.dialogRLayout);

        //set icons white on Dark mode On
        setIconsOnDarkMode();

        //remove items over 24hrs
        boolean itemRemovedOnOverStay = itemController.removeOverstayHistory(HistoryScreen.this);
        // check if that is last item, then show empty screen
        if (itemRemovedOnOverStay){
            setEmptyState();
        }

        //method to set history items
        itemController.allHistoryItemAdded(HistoryScreen.this);

        //set up dialog properties here

        deleteAllFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemController.historySize() != 0){
                    dialog.show();

                    //click listeners

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    clear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            itemController.clearHistory(HistoryScreen.this);
                            itemController.allHistoryItemAdded(HistoryScreen.this);

                            dialog.dismiss(); // dismiss the dialog after clearing history

                            setEmptyState();
                        }
                    });
                }

            }
        });

        Log.d("history screen stats", "is history array empty " + itemController.isHistoryListEmpty());

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryScreen.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });



        //*method to set empty state
        // check if history is empty set empty images to screen
        // else show recycler items*//
        boolean isHistoryEmpty = setEmptyState();

        if (!isHistoryEmpty){
            setAdapter();
            setHistoryRecyView();
        }

        //call swipe to undo method here
        undoItem(historyRecyView);
    }

    //set adapter items
    public void setAdapter(){
        historyAdapter.setModelList(itemController.historyList());
    }

    //set recycler view
    private void setHistoryRecyView(){
        historyRecyView.setAdapter(historyAdapter);
        historyRecyView.setLayoutManager(new LinearLayoutManager(HistoryScreen.this, LinearLayoutManager.VERTICAL, true));
        historyRecyView.setVisibility(View.VISIBLE);
    }

    private boolean setEmptyState(){
        if (!itemController.isHistoryListEmpty()){

            hideEmptyState(); // hide empty state screen
            return false;
        }
        else {

            showEmptyState(); //show empty state screen
            return true;

        }
    }

    //set empty screen on
    private void showEmptyState(){
        imgBag.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.VISIBLE);

        txtDelTime.setVisibility(View.GONE);
        deleteAllFAB.setVisibility(View.GONE);
        historyRecyView.setVisibility(View.GONE);
    }

    //set empty screen off
    private void hideEmptyState(){
        imgBag.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.GONE);

        deleteAllFAB.setVisibility(View.VISIBLE);
        txtDelTime.setVisibility(View.VISIBLE);
    }

    //create dialog box method
    public Dialog createDialogBox(){
        Dialog dialog = new Dialog(HistoryScreen.this);
        dialog.setContentView(R.layout.confirm_delete_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(HistoryScreen.this, R.drawable.dialog_inset));
        dialog.setCancelable(true);

        txtDialogWarn = dialog.findViewById(R.id.txtConfirmDelete);
        //initialize dialog buttons here
        cancel = dialog.findViewById(R.id.linearCancel);
        clear = dialog.findViewById(R.id.linearClear);
        txtCancel = dialog.findViewById(R.id.txtCancel);
        txtClear = dialog.findViewById(R.id.txtClear);
        return dialog;
    }

    private void undoItem(RecyclerView historyRecyView){
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

                //get object swiped to retrieve information and pass back to itemTable
                DeletedItemModel deletedItemModel = itemController.historyModelReturned(viewHolder.getAdapterPosition());
                String itemName = deletedItemModel.getName();
                String itemPrice = deletedItemModel.getPrice();
                String itemID = deletedItemModel.getId();
                String itemDesc = deletedItemModel.getDescription();
                long timePlaced = itemController.timeStamp();

                boolean validDelete = itemController.removeHistoryItem(deletedItemModel, HistoryScreen.this, itemID);

                if (validDelete){
                    itemController.addNewItem(itemID, itemName, itemPrice, itemDesc, timePlaced,HistoryScreen.this);
                }

                historyAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                if (itemController.isHistoryListEmpty()){
                    showEmptyState();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(historyRecyView);
    }


    @Override
    public void setIconsOnDarkMode() {
        boolean isDarkThemeOn = ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        if (isDarkThemeOn){
            ivBackArrow.setImageDrawable(AppCompatResources.getDrawable(HistoryScreen.this, R.drawable.back_white));
            dialogRelativeLayout.setBackgroundColor(getResources().getColor(R.color.dialog_grey));
            //set text colours white
            txtCancel.setTextColor(getResources().getColor(R.color.white));
            txtClear.setTextColor(getResources().getColor(R.color.white));
            txtDialogWarn.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //remove items over 24hrs
        itemController.removeOverstayHistory(HistoryScreen.this);
    }
}