import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

interface ShoppingManager {     //The ShoppingManager interface defines the contract for managing a shopping system.
    void addProduct(Product product);
    void deleteProduct(String productId, int productType);
    void printProductList();
    void saveProductsToFile(String fileName);
    void loadProductsFromFile(String fileName);
}

class
WestminsterShoppingManager implements ShoppingManager {
    private List<Product> productList;  //arraylist to save data
    static ArrayList<String> IDlist=new ArrayList<>();

    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
    }

    public ArrayList<Product> getProductList() {
        return (ArrayList<Product>) productList;
    }

    @Override
    public void addProduct(Product product) {   //method to add products to the system

        if (productList.size() < 50) {
            productList.add(product);
            System.out.println("Product Added Successfully");
        }else {
            System.out.println("System is full cannot add more products.");
        }
    }

    @Override
    public void deleteProduct(String productId, int productType) {      //delete method to delete products from the system
        if (productType == 1) {
            productList.removeIf(product -> product.getProductID().equals(productId) && product.getProductType().equals("Electronic"));
            System.out.println("Product " + productId + " removed successfully from the system");
        } else if (productType == 2) {
            productList.removeIf(product -> product.getProductID().equals(productId) && product.getProductType().equals("Clothing"));
            System.out.println("Product " + productId + " removed successfully from the system");
        }else {
            System.out.println("Given product ID is not existed");
        }
    }

    @Override
    public void printProductList() {        //printing the product list sorted
        IDlist.clear();
        for (int x=0;x<productList.size();x++){
            String pID=productList.get(x).getProductID();
            IDlist.add(pID);
        }
        Collections.sort(IDlist);
        for (int i=0;i<IDlist.size();i++){
            for (Product product : productList){
                if (IDlist.get(i).equals(product.getProductID())){
                    System.out.println("--------Sorted product list--------");
                    if (product.getProductType().equals("Electronic")) {
                        Electronic electronic = (Electronic) product;
                        System.out.println("Product Type - " + product.getProductType() + "\n" + "Product ID - " + product.getProductID() + "\n" + "Product Name - " + product.getProductName() + "\n" + "Products Available - " + product.getAvailableItems() + "\n" + "Product Price - " + product.getPrice()
                                + "\n" + "Product Brand - " + electronic.getBrand() + "\n" + "Product warranty period - " + electronic.getWarrantyPeriod() + "\n");
                    } else if (product.getProductType().equals("Clothing")) {
                        Clothing clothing = (Clothing) product;
                        System.out.println("Product Type - " + product.getProductType() + "\n" + "Product ID - " + product.getProductID() + "\n"+"Product Name - " + product.getProductName() + "\n" + "Products Available - " + product.getAvailableItems() + "\n" + "Product Price - " + product.getPrice()
                                + "\n" + "Product Size - " + clothing.getSize() + "\n" + "Product colour - " + clothing.getColour() + "\n");
                    }

                }
            }
        }
    }

    @Override
    public void saveProductsToFile(String fileName) {
        try {
            FileWriter textFile = new FileWriter(fileName);    //opening file if available or creating new file
            // writing cashier 1 data into file
            for (Product product : productList) {

                if (product.getProductType().equals("Electronic")) {
                    Electronic electronic = (Electronic) product;
                    textFile.write("Product Type - " + product.getProductType() + "\n" + "Product ID - " + product.getProductID() + "\n" + "Product Name - " + product.getProductName() + "\n" + "Products Available - " + product.getAvailableItems() + "\n" + "Product Price - " + product.getPrice()
                            + "\n" + "Product Brand - " + electronic.getBrand() + "\n" + "Product warranty period - " + electronic.getWarrantyPeriod() + "\n");
                    textFile.write("\n");
                } else if (product.getProductType().equals("Clothing")) {
                    Clothing clothing = (Clothing) product;
                    textFile.write("Product Type - " + product.getProductType() + "\n" + "Product ID - " + product.getProductID() + "\n" + "Product Name - " + product.getProductName() + "\n" + "Products Available - " + product.getAvailableItems() + "\n" + "Product Price - " + product.getPrice()
                            + "\n" + "Product Size - " + clothing.getSize() + "\n" + "Product colour - " + clothing.getColour() + "\n");
                    textFile.write("\n");
                }

            }
            textFile.close();
            System.out.println("Successfully wrote to the file.");
            System.out.println();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

    }

    @Override
    public void loadProductsFromFile(String fileName) {
        try (Scanner readFile = new Scanner(new File(fileName))) {
            // Use a delimiter to read the entire product block
            readFile.useDelimiter("\\n\\n");

            while (readFile.hasNext()) {
                // Read the product block
                String productBlock = readFile.next();

                // Parse the values using regular expressions
                String productType = extractValue(productBlock, "Product Type");
                String productId = extractValue(productBlock, "Product ID");
                String productName = extractValue(productBlock, "Product Name");
                int availableItems = Integer.parseInt(extractValue(productBlock, "Products Available"));
                double price = Double.parseDouble(extractValue(productBlock, "Product Price"));

                Product product;
                if (productType.equals("Electronic")) {
                    String brand = extractValue(productBlock, "Product Brand");
                    int warrantyPeriod = Integer.parseInt(extractValue(productBlock, "Product warranty period"));
                    product = new Electronic(productId, productName, availableItems, price, productType, brand, warrantyPeriod);
                } else {
                    String size = extractValue(productBlock, "Product Size");
                    String color = extractValue(productBlock, "Product colour");
                    product = new Clothing(productId, productName, availableItems, price, productType, size, color);
                }

                addProduct(product);
            }

            System.out.println("Successfully loaded products from the file.");
        } catch (FileNotFoundException e) {
            // Handle the exception if the file is not found
            e.printStackTrace();
        }
    }

    private String extractValue(String block, String key) {
        // Use regular expression to extract the value based on the key
        String regex = key + " - (.+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(block);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";  // or handle the case when there's no value
    }


}