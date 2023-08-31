package model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ModelDB implements ModelInterface{

    Context context;
    private List<ItemModel> allItemsList = new ArrayList<>();
    private List<DeletedItemModel> allHistoryItemsList = new ArrayList<>();
    private static ModelDB instance;

    //constructor
//    public  ModelDB(Context context){
//        this.context = context;
//    }
    public  ModelDB(){}

    //Singleton pattern
    public static ModelDB getInstance(){
        if (instance == null){
            instance = new ModelDB();
            return instance;
        }
        return instance;
    }

    public int itemListSize(){
        return allItemsList.size();
    }
    public int historyListSize(){return allHistoryItemsList.size();}

    //method to return a particular itemModel
    public ItemModel returnedItem(int position){
        return allItemsList.get(position);
    }

    //method to return a particular itemModel
    public DeletedItemModel returnedHistoryItem(int position){
        return allHistoryItemsList.get(position);
    }

    //method to check if allItemListIsEmpty
    public boolean isItemListEmpty(){
        return allItemsList.isEmpty();
    }
    public boolean isHistoryListEmpty(){
        return allHistoryItemsList.isEmpty();
    }

    //method to collect items from db and set them to our list created
    public void setItemList(Context context){
        Database database = new Database(context);
        allItemsList = database.getAllItems();
    }
    public void setHistoryList(Context context){
        Database database = new Database(context);
        allHistoryItemsList = database.getAllHistoryItems();
    }

    //method to sum prices
    public int sumPrices(){
        int totalPrice = 0;
        for (int i = 0; i < allItemsList.size(); i++) {
            totalPrice += Integer.parseInt(allItemsList.get(i).getPrice());
        }
        return totalPrice;
    }

    //method to return item list
    public List<ItemModel> returnedItemList(){
        return allItemsList;
    }
    //method to return history list
    public List<DeletedItemModel> returnedHistoryList(){
        return allHistoryItemsList;
    }

    //getter method for item description
    public String getItemDec(int position){
        return allItemsList.get(position).getDescription();
    }
    //getter method for item price
    public String getItemPrice(int position){
        return allItemsList.get(position).getPrice();
    }
    //getter method for item Name
    public String getItemName(int position){
        return allItemsList.get(position).getName();
    }
    //getter method for item ID
    public String getItemId(int position) {return allItemsList.get(position).getID();}


    //method to remove item from list on success del from db
    public void removeItemFromList(String itemUUID){
        for (int i = 0; i < allItemsList.size(); i++) {
            if (allItemsList.get(i).getID().equals(itemUUID)){
                allItemsList.remove(i);
            }
        }
    }

    //method to remove item from history on success del from db
    public void removeHistoryItemFromList(String itemUUID){
        for (int i = 0; i < allHistoryItemsList.size(); i++) {
            if (allHistoryItemsList.get(i).getId().equals(itemUUID)){
                allHistoryItemsList.remove(i);
            }
        }
    }

    // methods to edit items name in data base
    public boolean updateItemName(ItemModel itemModel, String newName, Context context){
        Database database = new Database(context);
        return database.updateName(itemModel, newName);
    }

    // methods to edit items price in data base
    public boolean updateItemPrice(ItemModel itemModel, int newPrice, Context context){
        Database database = new Database(context);
        return database.updatePrice(itemModel, newPrice);

    }

    // methods to edit items description in data base
    public boolean updateItemDesc(ItemModel itemModel, String newDesc, Context context){
        Database database = new Database(context);
        return database.updateDesc(itemModel, newDesc);
    }

    // clear history table
    public void clearHistory(Context context){
        Database database = new Database(context);
        database.clearHistory();
    }


    @Override
    public boolean addItem(String id, String name, String price, String desc, Context context) {
        Database database = new Database(context);
        ItemModel itemModel = new ItemModel(id, name, price, desc);
        this.context = context;

        boolean successAdd = database.addNewItem(itemModel);

        if (!successAdd){
            return false;
        }
        return true;
    }

    @Override
    public boolean addToHistory(String id, String name, String price, String desc, long timeDeleted) {
        Database database = new Database(context);
        DeletedItemModel deletedItemModel = new DeletedItemModel(id, name, price, timeDeleted, desc);

        boolean successAdd = database.addToHistory(deletedItemModel);

        if (!successAdd){
            return false;
        }
        return true;

//        database.addToHistory(deletedItemModel);
    }

    @Override
    public boolean removeHistoryItem(DeletedItemModel deletedItemModel, Context context) {
        this.context = context;
        Database database = new Database(context);

        boolean successDelete = database.deleteHistoryItem(deletedItemModel);

        if (successDelete){
            Log.d("del stats from model", "item removed");
            return true;
        }else {
            Log.d("del stats from model", "remove failed");
//            setHistoryList(context);
            return false;
        }
    }

    @Override
    public boolean removeItem(ItemModel itemModel, Context context) {

        this.context = context;

        Database database = new Database(context);

        boolean successDEL = database.deleteItem(itemModel);
        if (successDEL){
            Log.d("del stats from model", "item removed");
            return true;
        }else {
            Log.d("del stats from model", "remove failed");
            return false;
        }
    }
}