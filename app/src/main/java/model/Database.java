package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper{

    public final String itemTable = "item_table";
    public final String historyTable = "history_table";
    public static final int databaseVersion = 1;
    private final String itemID = "UUID";
    private final String historyID = "UUID";

    public Database(@Nullable Context context) {
        super(context, "shoppyDB", null, databaseVersion);
    }

    /**
     * Method to handle inputs to the SQL db**/
    public boolean addNewItem(ItemModel itemModel){
        SQLiteDatabase newItem = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("UUID", itemModel.getID());
        cv.put("NAME", itemModel.getName());
        cv.put("PRICE", itemModel.getPrice());
        cv.put("DESCRIPTION", itemModel.getDescription());

        long successAdd = newItem.insert(itemTable, null, cv);

        if (successAdd == -1){
            newItem.close();
            return false;
        }else {
        return true;
        }
    }

    /**
     * Method to handle inputs to the SQL db**/
    public boolean addToHistory (DeletedItemModel deletedItemModel){
        SQLiteDatabase newItem = Database.this.getWritableDatabase();
        String UUID = deletedItemModel.getId();
        String NAME = deletedItemModel.getName();
        int PRICE = Integer.parseInt(deletedItemModel.getPrice());
        long TIME = deletedItemModel.getTimeDeleted();

        //trigger to add here
        ContentValues cv = new ContentValues();

        cv.put("UUID", deletedItemModel.getId());
        cv.put("NAME", deletedItemModel.getName());
        cv.put("PRICE", deletedItemModel.getPrice());
        cv.put("DESCRIPTION", deletedItemModel.getDescription());
        cv.put("TIMEPLACED", deletedItemModel.getTimeDeleted());

        long successAdd = newItem.insert(historyTable, null, cv);

        if (successAdd == -1){
            newItem.close();
            return false;
        }
        newItem.close();
        return true;
    }

    // methods to handle deletes from database tables
    //todo: write delete method here
    public boolean deleteItem(ItemModel itemModel){
        SQLiteDatabase database = Database.this.getWritableDatabase();
        String uuid = itemModel.getID();

        Log.d("itemModId", "itemId " + uuid);

        String query = "" + itemID + " = " + itemModel.getID();

        return database.delete(itemTable, itemID + " = ? ", new String[]{uuid}) > 0;
    }

    // method to handle deletes from database history table
    public boolean deleteHistoryItem(DeletedItemModel deletedItemModel){
        SQLiteDatabase database = Database.this.getWritableDatabase();
        String uuid = deletedItemModel.getId();

        Log.d("itemModId", "itemId " + uuid);

        String query = "" + itemID + " = " + deletedItemModel.getId();

        return database.delete(historyTable, itemID + " = ? ", new String[]{uuid}) > 0;
    }

    /*Delete history item:
    * after item has reached 24hr
    * delete item using received id
    * TODO: this method will handle deletes after 24hrs*/
    public boolean deleteHistoryAfterHoursExceed(DeletedItemModel historyModel){

        SQLiteDatabase removeItem = this.getWritableDatabase();
        String itemID = historyModel.getId();
        long timeAdded = historyModel.getTimeDeleted();

        long currentTime = System.currentTimeMillis(); // current system time
        long timeDiff = 24 * 60 * 60 * 1000; // total milli seconds for 24hrs
        long timeSubtract = currentTime - timeDiff;

        String query = "" + timeAdded + " < " + timeSubtract;

        return removeItem.delete(historyTable, query + " = ? ", new String[]{String.valueOf(currentTime - timeDiff)}) > 0;
    }

    /*
    * clear all history items*/
    public void deleteAllHistoryItems(){
        //TODO: write method to delete all history items
        SQLiteDatabase historyTable = this.getWritableDatabase();
        String clearAllQuery = "DELETE * FROM" + historyTable;
    }

    /*
    * update items in table
    * item name*/
    public boolean updateName( ItemModel itemModel ,String newName){
        SQLiteDatabase table = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String uuid = itemModel.getID();

        cv.put("NAME", newName);
//        String updateQuery = "UPDATE " + table + " SET NAME = " + newName + " WHERE " + itemID + " = " + itemModel.getID();
//
//        table.execSQL(updateQuery, null);

        return table.update(itemTable, cv, itemID + " = ?", new String[]{uuid}) > 0;
    }

    //update item price in database
    public boolean updatePrice( ItemModel itemModel ,int newPrice){
        SQLiteDatabase table = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String uuid = itemModel.getID();

        cv.put("PRICE", newPrice);
//        String updateQuery = "UPDATE " + itemTable + " SET PRICE = " + newPrice + " WHERE " + itemID + " = " + itemModel.getID();
//
//        itemTable.execSQL(updateQuery, null);

        return table.update(itemTable, cv, itemID + " = ?", new String[]{uuid}) > 0;

    }

    //update item description in database
    public boolean updateDesc( ItemModel itemModel ,String newDesc){
        SQLiteDatabase table = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String uuid = itemModel.getID();

        cv.put("DESCRIPTION", newDesc);
//        String updateQuery = "UPDATE " + itemTable + " SET DESCRIPTION = " + newDesc + " WHERE " + itemID + " = " + itemModel.getID();
//        itemTable.execSQL(updateQuery, null);

        return table.update(itemTable, cv, itemID + " = ?", new String[]{uuid}) > 0;
    }

    //getAllItemsAdded
    public List<ItemModel> getAllItems(){
        List<ItemModel> allItemsAdded = new ArrayList<>();

        //getting data from db
        String queryItemsDB = "SELECT * FROM " + itemTable;

        SQLiteDatabase dbItems = this.getReadableDatabase();

        Cursor cursor = dbItems.rawQuery(queryItemsDB, null);

        //checking query items from cursor
        if (cursor.moveToFirst()){
            //then loop in the result set and add them to the created list of items to be returned
            do {

                String itemID = cursor.getString(0);
                String itemName = cursor.getString(1);
                int price = cursor.getInt(2);
                String description = cursor.getString(3);

                ItemModel itemModel = new ItemModel(itemID, itemName, String.valueOf(price), description);
                itemModel.setID(itemID);

                //passing received result to list
                allItemsAdded.add(itemModel);

            }while (cursor.moveToNext());
        }
        else {
            Log.d("databaseItems stats", "Empty list");
        }
        //close connections to database
        cursor.close();
        dbItems.close();
        return allItemsAdded;
    }

    public List<DeletedItemModel> getAllHistoryItems(){
        List<DeletedItemModel> allItemsAdded = new ArrayList<>();

        //getting data from db
        String queryItemsDB = "SELECT * FROM " + historyTable;

        SQLiteDatabase dbHistory = this.getReadableDatabase();

        Cursor cursor = dbHistory.rawQuery(queryItemsDB, null);

        //checking query items from cursor
        if (cursor.moveToFirst()){
            //then loop in the result set and add them to the created list of items to be returned
            do {

                String itemID = cursor.getString(0);
                String itemName = cursor.getString(1);
                int itemPrice = cursor.getInt(2);
                String itemDesc = cursor.getString(3);
                long timePlaced = cursor.getLong(4);

                DeletedItemModel historyList = new DeletedItemModel(itemID, itemName, String.valueOf(itemPrice), timePlaced, itemDesc);

                //passing result to list of history
                allItemsAdded.add(historyList);

            }while (cursor.moveToNext());
        }
        else {
            Log.d("databaseHistory stats", "Empty list");
        }
        //always close connection to database
        cursor.close();
        dbHistory.close();
        return allItemsAdded;
    }

    //method to clear history table items
    public void clearHistory(){
        SQLiteDatabase historyQuery = this.getWritableDatabase();
        String clearTable = "DELETE FROM " + historyTable;

        historyQuery.execSQL(clearTable);
    }


    //created on first lunch of application
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createItemTable = "CREATE TABLE " + itemTable + " (" + itemID + " VARCHAR (50) PRIMARY KEY, NAME VARCHAR (40), PRICE INTEGER, DESCRIPTION VARCHAR (100))";
        String createHistoryTable = "CREATE TABLE " + historyTable + " (" + historyID + " VARCHAR (50) PRIMARY KEY, NAME VARCHAR (40), PRICE INTEGER, DESCRIPTION VARCHAR (100), TIMEPLACED INTEGER)";

        sqLiteDatabase.execSQL(createItemTable);
        sqLiteDatabase.execSQL(createHistoryTable);

    }

    // this is returned if an existing database exists
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

}
