/**
 * The Clothing class represents a clothing product and extends the Product class.
 * It includes additional attributes for size and color.
 */
public class Clothing extends Product {
    private String Size;
    private String Colour;

    //Constructor to initialize the Clothing object with specified attributes.
    public Clothing(String ProductID, String ProductName, int AvailableItems, double Price, String ProductType, String Size, String Colour) {
        super(ProductID, ProductName, AvailableItems, Price, ProductType);
        this.Size = Size;
        this.Colour = Colour;
    }
// getters and setters
    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String colour) {
        Colour = colour;
    }

}
