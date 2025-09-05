package src;

public class Item {
    String name;
    String price;
    int status;
    String category;
    int itemNum;
    String dibID;

    public Item(int itemNum, String name, String price, String category, String dibId, int status) {
        this.itemNum = itemNum;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dibID = dibId;
        this.status = status;
    }

    public Item(String name, String price, int status) {
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public Item(String name, String price, int status, String category) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", status=" + status +
                ", category='" + category + '\'' +
                ", itemNum=" + itemNum +
                ", dibID='" + dibID + '\'' +
                '}';
    }
}
