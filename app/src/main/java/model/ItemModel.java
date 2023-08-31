package model;

import java.util.UUID;

public class ItemModel {
    String ID;
    String Name;
    String price;
    String description;

    String uuidString;

    //constructor
    public ItemModel(String ID, String name, String price, String description) {
        this.ID = ID;
        this.Name = name;
        this.price = price;
        this.description = description;

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
}
