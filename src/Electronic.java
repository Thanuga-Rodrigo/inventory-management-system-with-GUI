public class Electronic extends Product {
    private String Brand;
    private int WarrantyPeriod;

    public Electronic(String ProductID, String ProductName, int AvailableItems, double Price, String ProductType,String Brand, int WarrantyPeriod) {
        super(ProductID, ProductName, AvailableItems, Price, ProductType);
        this.Brand = Brand;
        this.WarrantyPeriod = WarrantyPeriod;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public int getWarrantyPeriod() {
        return WarrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        WarrantyPeriod = warrantyPeriod;
    }

}
