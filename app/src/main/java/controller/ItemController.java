package controller;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.ehdeeCodes.shoppyapp.adapters.HistoryAdapter;
import com.ehdeeCodes.shoppyapp.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.DeletedItemModel;
import model.ItemModel;
import model.ModelDB;


public class ItemController extends ViewModel {

    //model class here
    private final ModelDB modelDB = ModelDB.getInstance();
    //adapters here
    private final ItemAdapter itemAdapter = new ItemAdapter();
    private final HistoryAdapter historyAdapter = new HistoryAdapter();
    private List<Integer> IDs = new ArrayList<>();
    private int newID;

    public String itemUUID (){
        UUID itemID = UUID.randomUUID();
        String stringUUID = itemID.toString();
        return stringUUID;
    }

    /*get time item was deleted method
    * which collects time and binds it to item going to history DB*/
    public Long timeItemDeleted(){
        return System.currentTimeMillis();
    }

    //method to add new item
    public boolean addNewItem(String id, String itemName, String price, String desc, Context context){
        boolean successAdd = modelDB.addItem(id, itemName, price, desc, context);

        if (!successAdd){
            return false;
        }
        return true;
    }
    //method to add deleted item to history
    public void addToHistory(String id, String name, String price, long timeDeleted, String desc){
        boolean successAdd = modelDB.addToHistory(id, name, price, desc, timeDeleted);
        if (!successAdd){
            Log.d("ic history add stats", "addToHistory: not successful");
        }
        else {
            Log.d("ic history add stats", "addToHistory: successful");
        }
    }


    //method to check if tables are empty
    public boolean isItemListEmpty (){
        return modelDB.isItemListEmpty();
    }
    public boolean isHistoryListEmpty (){
        return modelDB.isHistoryListEmpty();
    }
    //method to return total number of items
    public int amountOfItems(){
        return modelDB.itemListSize();
    }

    //getItemDescription
    //*
    // for setting item name price on the edit screen to be edited*//
    public String getItemDesc(){
        int receivedPosition = itemAdapter.getClickedAdapterPosition();

        Log.d("clicked", "getItemDesc: " + itemAdapter.getClickedAdapterPosition());
        return modelDB.getItemDec(receivedPosition);
    }
    /*methods to return itemID
    * item name
    * item price to be passed to history*/
    public String getItemID(int position){
        return modelDB.getItemId(position);
    }
    public String getName(int position){
        return modelDB.getItemName(position);
    }
    public String getPrice(int position){
        return modelDB.getItemPrice(position);
    }
    public String getDesc(int position){
        return modelDB.getItemDec(position);
    }



    //method to return list of items to be bind to the adapters
    public List<ItemModel> itemList(){
        return modelDB.returnedItemList();
    }

    public List<DeletedItemModel> historyList(){
        return modelDB.returnedHistoryList();

    }

    public int priceTotal(){
        return modelDB.sumPrices();
    }

    public void allItemsAdded(Context context){
        modelDB.setItemList(context);
    }
    public void allHistoryItemAdded(Context context){
        modelDB.setHistoryList(context);
    }

    public boolean historyItemSize(){
        return modelDB.isHistoryListEmpty();
    }

    public int itemsSize(){return modelDB.itemListSize();}
    public int historySize(){
        return modelDB.historyListSize();
    }

    //this method removes item from the item table in the DB
    public boolean removeItemFromTable(ItemModel itemModel, Context context, String itemRemoveUUID){
        boolean itemRemoved = modelDB.removeItem(itemModel, context);
        
        if (itemRemoved){
            modelDB.removeItemFromList(itemRemoveUUID);
            updateAdapterOnDel();
            Toast.makeText(context.getApplicationContext(), "true", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(context.getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //method to remove item from history after 24hrs
    public boolean removeHistoryItem(DeletedItemModel deletedItemModel, Context context, String itemRemoveUUID){
        boolean itemRemoved = modelDB.removeHistoryItem(deletedItemModel, context);

        if (itemRemoved){
            modelDB.removeHistoryItemFromList(itemRemoveUUID);
            updateHistAdapterOnDel();
            Toast.makeText(context.getApplicationContext(), "true", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(context.getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //method to receive returned itemModel from ModelClass
    public ItemModel itemModelReturned(int position){
        return modelDB.returnedItem(position);
    }
    //method to receive returned deleteModel from ModelClass
    public DeletedItemModel historyModelReturned(int position){
        return modelDB.returnedHistoryItem(position);
    }

    //notify items adapter on item removed
    public void updateAdapterOnDel(){
        itemAdapter.notifyDataSetChanged();
    }

    //notify items adapter on item removed
    public void updateHistAdapterOnDel(){
        historyAdapter.notifyDataSetChanged();
    }

    //get adapter item size
    public int getHistoryItems(){
        return itemAdapter.getItemCount();
    }

    //removeItem ID on delete
    public void removeIDAt(int position){
        IDs.remove(position);
    }

    /*
    * methods to update item:
    * name
    * price
    * description*/

    //edit item name
    public void editItemName(Context context, ItemModel itemModel, String newName){
        boolean updateSuccess = modelDB.updateItemName(itemModel, newName, context);
        if (updateSuccess){
            Log.d("name edit status", "name updated successfully");
        }
        else Log.d("name edit error", "editItemName: " + "failed");
    }

    public void editItemPrice(Context context, ItemModel itemModel, int newPrice){
        boolean updateSuccess = modelDB.updateItemPrice(itemModel, newPrice, context);
        if (updateSuccess){
            Log.d("price edit status", "price updated successfully");
        }
        else Log.d("price edit error", "editItemName: " + "failed");
    }

    public void editItemDesc(Context context, ItemModel itemModel, String newDescription){
        boolean updateSuccess = modelDB.updateItemDesc(itemModel, newDescription, context);
        if (updateSuccess){
            Log.d("description edit status", "desc updated successfully");
        }
        else Log.d("description edit error", "editItemName: " + "failed");
    }

    //check if edt text fields changed
    //return true if name changed
    public boolean isNameChanged(EditText itemName, ItemModel itemModel){
        return itemName.getText().toString().equals(itemModel.getName());
    }

    //return true if price changed
    public boolean isPriceChanged(EditText itemPrice, ItemModel itemModel){
        return itemPrice.getText().toString().equals(itemModel.getPrice());
    }

    //return true if description changed
    public boolean isDescChanged(EditText itemDesc, ItemModel itemModel){
        return itemDesc.getText().toString().equals(itemModel.getDescription());
    }

    // method to clear history on click of trash fab
    public void clearHistory(Context context){
        modelDB.clearHistory(context);
        historyAdapter.clearAllItems();

    }

}
