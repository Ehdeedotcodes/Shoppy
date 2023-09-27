package model;

import android.content.Context;

import java.util.ArrayList;

public interface ModelInterface {
    public boolean addItem(String id, String name, String price, String desc, long timeAdded, Context context);
    public boolean addToHistory(String id, String name, String price, String desc, long timeDeleted);
    public boolean removeHistoryItem(DeletedItemModel deletedItemModel, Context context);
    public boolean removeItem(ItemModel itemModel, Context context);

}
