// The Product class is an abstract class representing a generic product.
abstract class Product {
    private String ProductID;
    private String ProductName;
    private int AvailableItems;
    private double Price;
    private String ProductType;
//Constructor to initialize a Product object with specified attributes.
    public Product(String ProductID, String ProductName, int AvailableItems, double Price, String ProductType) {
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.AvailableItems = AvailableItems;
        this.Price = Price;
        this.ProductType = ProductType;
    }
    //getters and setters
    public String getProductID() {
        return ProductID;
    }
    public void setProductID(String productID) {

        ProductID = productID;
    }
    public String getProductName() {

        return ProductName;
    }
    public void setProductName(String productName) {

        ProductName = productName;
    }
    public int getAvailableItems() {
        return AvailableItems;
    }
    public void setAvailableItems(int availableItems) {
        AvailableItems = availableItems;
    }
    public double getPrice() {
        return Price;
    }
    public void setPrice(double price) {
        Price = price;
    }
    public String getProductType(){
        return ProductType;
    }
    public void updateAvailableItems(int quantityRemoved) {
        AvailableItems += quantityRemoved;
    }
}


