package model;

public class DeletedItemModel {
    String name, price, id, description;
    long timeDeleted;

    public DeletedItemModel(String id, String name, String  price, long timeDeleted, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.timeDeleted = timeDeleted;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimeDeleted() {
        return timeDeleted;
    }
}
