import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import
        java.util.Scanner;

// The Main class contains the main method for executing the Westminster Shopping Manager program.
    public class Main {
        public static void main(String[] args) {
            WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
            MainMenu();

            while(true){

                Scanner input = new Scanner(System.in);
                System.out.print("\nEnter your option - ");
                int option = input.nextInt();
                switch(option){

                    case 1 : // Code for adding products to the system
                        int productTypeChoice = 0;
                        while (productTypeChoice != 1 && productTypeChoice != 2) { // if the user not selects 1 or 2,
                            System.out.print("Enter 1 for Electronic and 2 for Clothing: ");
                            try {
                                productTypeChoice = input.nextInt();
                                if (productTypeChoice != 1 && productTypeChoice != 2) { // if the user not selects 1 and 2,
                                    System.out.println("Invalid input. Please enter 1 or 2.");
                                    continue;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a number.");
                                input.next();
                                continue;
                            }
                        }


                        System.out.print("Enter product ID: ");
                        String productId = input.next();
                        input.nextLine();


                        System.out.print("Enter product name: ");
                        String productName = input.next();

                        int availableItems = -1;
                        while (availableItems < 0) {
                            System.out.print("Enter available items: ");
                            try {
                                availableItems = input.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a number.");
                                input.next();
                            }
                        }

                        double price = -1.0;

                        // Loop until a valid price is entered.
                        while (price < 0.0) {
                            System.out.print("Enter price: ");
                            try {
                                price = input.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a number.");
                                input.next();
                            }
                        }

                        if (productTypeChoice == 1) {
                            String ProductType = "Electronic";

                            System.out.print("Enter brand: ");
                            String brand = input.next();
                            input.nextLine();

                            System.out.print("Enter warranty period (Weeks): ");
                            int warrantyPeriod = input.nextInt();

                            Electronic electronic = new Electronic(productId, productName, availableItems, price, ProductType, brand, warrantyPeriod);
                            shoppingManager.addProduct(electronic);
                        } else if (productTypeChoice == 2) {
                            String ProductType = "Clothing";
                            input.nextLine();

                            System.out.print("Enter size (XS,S,M,L,XL,XXL): ");
                            String size = input.next();

                            System.out.print("Enter color: ");
                            String color = input.next();

                            Clothing clothing = new Clothing(productId, productName, availableItems, price, ProductType, size, color);
                            shoppingManager.addProduct(clothing);
                        }else{
                            System.out.println("Invalid Product Type Choice.");
                        }

                        break;

                    case 2 :    // Code for deleting a product from the system
                        int deleteProductType = 0;
                        while (deleteProductType != 1 && deleteProductType != 2) { // if the user not selects 1 or 2,
                            System.out.print("Enter 1 for Electronic and 2 for Clothing: ");
                            try {
                                deleteProductType = input.nextInt();
                                if (deleteProductType != 1 && deleteProductType != 2) { // if the user not selects 1 or 2,
                                    System.out.println("Invalid input. Please enter 1 or 2.");
                                    continue;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a number.");
                                input.next();
                                continue;
                            }
                        }
                        System.out.print("Enter product ID to delete: ");
                        String deleteProductId = input.next();
                        shoppingManager.deleteProduct(deleteProductId, deleteProductType);
                        break;

                    case 3 :    // Code for printing the list of products
                        shoppingManager.printProductList();
                        break;



                    case 4 :    // Code for saving data into a file
                        shoppingManager.saveProductsToFile("Saved Data.txt");
                        break;

                    case 5 :     // Code for loading data from a text file
                        shoppingManager.loadProductsFromFile("Saved Data.txt");
                        break;
                    case 6 :
                        // Code for exiting the system
                        System.out.print("Enter User Name: ");
                        String username = input.next();
                        System.out.print("Enter Password: ");
                        String password = input.next();
                        User newUser = new User(username, password);
                        boolean isNewUser = false;

                        if (!UserDetails.isUserExists(newUser)) {
                            System.out.println("New user. First purchase discount available.");
                            try {
                                UserDetails.saveUser(newUser);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            isNewUser = true;
                        }else {
                            System.out.println("User Already Exists!");
                        }

                        ArrayList<Product> productList = shoppingManager.getProductList();
                        GUI myGUI = new GUI(productList, newUser, isNewUser);
                        break;

                    case 7:
                        System.out.println("Thank You for Using Westminster Shopping Manager....");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid Option Entered.");
                        break;
                }
            }
        }

    //Displays the main menu of the Westminster Shopping Manager program.
        public static void MainMenu(){
            System.out.println("\n Westminster Shopping Manager");
            System.out.println("---------------------------------");
            System.out.println("1. Add Products to the System.");
            System.out.println("2. Delete a product from the system.");
            System.out.println("3. Print the list of products.");
            System.out.println("4. Save data into a file.");
            System.out.println("5. load from text file.");
            System.out.println("6. Graphical User Interface (User Only).");
            System.out.println("7. Exit the System.");
        }
    }
