package model;

import java.util.UUID;

public class ItemModel {
    private String ID;
    private String Name;
    private String price;
    private String description;
    private long timeAdded;

    String uuidString;

    //constructor
    public ItemModel(String ID, String name, String price, String description, long timeAdded) {
        this.ID = ID;
        this.Name = name;
        this.price = price;
        this.description = description;
        this.timeAdded = timeAdded;
    }

    //constructor
    public ItemModel(){}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeAdded() {
        return timeAdded;
    }
}
