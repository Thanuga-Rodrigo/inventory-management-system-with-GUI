import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUI extends JFrame {
    private JTable productTable;
    private TableRowSorter<TableModel> rowSorter;
    private JPanel detailsPanel;
    private Product items;
    private JLabel totalPriceLabel;
    private JLabel totalLabel = new JLabel("Total: $0.0");;
    private JLabel firstPurchaseDiscountLabel;
    private JLabel categoryDiscountLabel;
    private JLabel finalTotalLabel;
    private ShoppingCart shoppingCart = new ShoppingCart();
    private User currentUser;
    private boolean isNewUser;


    public GUI(ArrayList<Product> productList, User newUser, boolean isNewUser) {
        totalPriceLabel= new JLabel();
        setTitle("Westminster Shopping Manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //loading the Jframe
        setSize(900, 600);
        this.currentUser = currentUser;
        this.isNewUser = isNewUser;

        //initialize labels
        totalPriceLabel = new JLabel();
        totalLabel = new JLabel();
        firstPurchaseDiscountLabel = new JLabel();
        categoryDiscountLabel = new JLabel();
        finalTotalLabel = new JLabel();

        // Create the table with column headers
        String[] columnHeaders = {"Product ID", "Name", "Category", "Price","Available Items", "Info"}; //headers of the column of the table

        DefaultTableModel tableModel = new DefaultTableModel(columnHeaders, 0);
        productTable = new JTable(tableModel);

        JTableHeader header = productTable.getTableHeader();    //changing fonts of the headers
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        add(detailsPanel, BorderLayout.SOUTH);



        // Populate the table with product information
        displayProducts(productList);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                displayGetters(selectedRow);

            }
        });



        //making the dropdown list
        String[] categories = {"All", "Electronic", "Clothing"};//setting the combobox
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        // Adding an ActionListener to the dropdown
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTableByCategory((String) categoryComboBox.getSelectedItem());
            }
        });


        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Product Category:"));
        panel.add(categoryComboBox);
        add(panel, BorderLayout.NORTH);


        JButton addToCartButton = new JButton("Add to Cart");       //making add to cart button

        addToCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createHorizontalStrut(280)); // Add some vertical space
        panel.add(addToCartButton);
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < productList.size()) {
                    items = productList.get(selectedRow);
                    // Get quantity from user
                    String quantityString = JOptionPane.showInputDialog(GUI.this, "Enter Quantity:"); //prompt to enter the quantity
                    try {
                        int quantity = Integer.parseInt(quantityString);
                        int availableItems = items.getAvailableItems();
                        if (quantity <= availableItems) {
                            items.setAvailableItems(availableItems - quantity);
                            shoppingCart.addtocart(items, quantity);
                            displayProducts(productList);
                            JOptionPane.showMessageDialog(GUI.this, "Product added to cart!");  //error indetifiers
                        } else {
                            JOptionPane.showMessageDialog(GUI.this, "Please enter a valid quantity.");
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(GUI.this, "Please enter a numeric value.");
                    }
                } else {
                    JOptionPane.showMessageDialog(GUI.this, "Please select a product before adding to the cart.");
                }
            }
        });

        JButton shoppingCartButton=new JButton("Shopping Cart");        //shopping cart button
        panel.add(Box.createVerticalStrut(70));//to add space between the dropdown and the button
        panel.add(shoppingCartButton);
        shoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayShoppingCart();
            }
        });

        // Initialize the TableRowSorter
        rowSorter = new TableRowSorter<>(productTable.getModel());
        productTable.setRowSorter(rowSorter);

        setVisible(true);
    }

    private void displayProducts(List<Product> productList) {       //displays the product
        DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
        tableModel.setRowCount(0); // Clear existing rows

        for (Product product : productList) {
            Object[] rowData = {
                    product.getProductID(),
                    product.getProductName(),
                    product.getProductType(),
                    product.getPrice(),
                    product.getAvailableItems(),
                    getProductInfo(product)
            };

            int availableItems = product.getAvailableItems();
            tableModel.addRow(rowData);

            int rowIndex = tableModel.getRowCount() - 1;

            if (availableItems <= 3) {
                for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++) {
                    productTable.getColumnModel().getColumn(columnIndex).setCellRenderer(new CustomTableCellRenderer(productList));
                }
            }
        }
    }
    private void displayShoppingCart() {        //displays the shopping cart
        JFrame cartFrame = new JFrame("Shopping Cart");
        cartFrame.setSize(500, 300);

        // Column headers for the shopping cart table
        String[] cartColumnHeaders = {"Product", "Quantity", "Price"};
        DefaultTableModel cartTableModel = new DefaultTableModel(cartColumnHeaders, 0);
        JTable cartTable = new JTable(cartTableModel);

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartFrame.add(cartScrollPane, BorderLayout.CENTER);

        JButton removeButton = new JButton("Remove Product");      //remove Button

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Product selectedProduct = shoppingCart.getCartItems().get(selectedRow);
                    double updatedTotal = shoppingCart.removeItem(selectedProduct);
                    shoppingCart.removeItem(selectedProduct);
                    updateCartTable(cartTableModel, totalLabel, firstPurchaseDiscountLabel, categoryDiscountLabel, finalTotalLabel);
                    updateTotalPriceLabel();
                } else {
                    JOptionPane.showMessageDialog(cartFrame, "Please select an item to remove.");
                }
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(removeButton);
        cartFrame.add(buttonsPanel, BorderLayout.NORTH);



        // Panel for total price and discounts
        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));

        JLabel totalLabel = new JLabel("Total: $0.0");
        JLabel firstPurchaseDiscountLabel = new JLabel("First Purchase Discount (10%): -$0.0");
        JLabel categoryDiscountLabel = new JLabel("Three Items in Same Category Discount (20%): -$0.0");
        JLabel finalTotalLabel = new JLabel("Final Total: $0.0");

        totalsPanel.add(totalLabel);
        totalsPanel.add(firstPurchaseDiscountLabel);
        totalsPanel.add(categoryDiscountLabel);
        totalsPanel.add(finalTotalLabel);

        cartFrame.add(totalsPanel, BorderLayout.SOUTH);

        // Populate the cart table with items and calculate totals and discounts
        updateCartTable(cartTableModel, totalLabel, firstPurchaseDiscountLabel, categoryDiscountLabel, finalTotalLabel);

        cartFrame.setVisible(true);
    }


    private String getProductInfo(Product product) {
        if (product instanceof Electronic) {
            Electronic electronics = (Electronic) product;
            return ( electronics.getBrand()+", "+electronics.getWarrantyPeriod() + " Week(s)");
        } else if (product instanceof Clothing) {
            Clothing clothing = (Clothing) product;
            return( clothing.getSize() +", "+clothing.getColour());
        }
        return ""; // Return empty string if not Electronics or Clothing
    }

    // Method to filter the table based on the selected category
    private void filterTableByCategory(String category) {
        if ("All".equals(category)) {
            rowSorter.setRowFilter(null); // Show all rows
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)^" + category + "$", 2)); // Filter based on category column
        }
    }

    // Method to display getters of the selected product in the detailsPanel
    private void displayGetters(int selectedRow) {
        detailsPanel.removeAll(); // Clear existing details
        detailsPanel.add(new JLabel("Details of the Products:"));
        if (selectedRow >= 0 && selectedRow < productTable.getRowCount()) {
            // Get the index of the selected row in the model
            int modelIndex = productTable.convertRowIndexToModel(selectedRow);

            // Get the product from the filtered list
            DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
            Object[] rowData = new Object[tableModel.getColumnCount()];
            for (int i = 0; i < rowData.length; i++) {
                rowData[i] = tableModel.getValueAt(modelIndex, i);
            }

            // Add details based on the type of product
            detailsPanel.add(new JLabel("Product ID: " + rowData[0]));
            detailsPanel.add(new JLabel("Product Name: " + rowData[1]));
            detailsPanel.add(new JLabel("Category: " + rowData[2]));
            detailsPanel.add(new JLabel("Price: " + rowData[3]));
            detailsPanel.add(new JLabel("Available Items: " + rowData[4]));

            if ("Electronic".equals(rowData[2])) {
                detailsPanel.add(new JLabel("Brand: " + rowData[5].toString().split(",")[0]));
                detailsPanel.add(new JLabel("Warranty Period: " + rowData[5].toString().split(",")[1]));
            } else if ("Clothing".equals(rowData[2])) {
                detailsPanel.add(new JLabel("Size: " + rowData[5].toString().split(",")[0]));
                detailsPanel.add(new JLabel("Colour: " + rowData[5].toString().split(",")[1]));
            }

            // Refresh the detailsPanel
            detailsPanel.revalidate();
            detailsPanel.repaint();
        }


    }

    private void updateCartTable(DefaultTableModel cartTableModel, JLabel totalLabel, JLabel firstPurchaseDiscountLabel, JLabel categoryDiscountLabel, JLabel finalTotalLabel) {
        cartTableModel.setRowCount(0); // Clear the table
        double total = 0;
        for (Map.Entry<Product, Integer> entry : shoppingCart.getQuantityMap().entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            double totalPrice = product.getPrice() * quantity;
            total += totalPrice;

            Object[] cartRowData = {
                    product.getProductName(),
                    quantity,
                    totalPrice
            };
            cartTableModel.addRow(cartRowData);
        }
        double firstPurchaseDiscount = 0;
        if (isNewUser) {
            firstPurchaseDiscount = total * 0.1; // Assuming a 10% discount for the first purchase
        }
        double categoryDiscount = 0;
        if (shoppingCart.hasSameCategoryProducts()) {
            categoryDiscount = total * 0.2; // Assuming a 20% discount for three items in the same category
        }
        double finalTotal = total - firstPurchaseDiscount - categoryDiscount;

        totalLabel.setText("Total: $" + String.format("%.2f", total));
        firstPurchaseDiscountLabel.setText("First Purchase Discount (10%): -$" + String.format("%.2f", firstPurchaseDiscount));
        categoryDiscountLabel.setText("Three Items in Same Category Discount (20%): -$" + String.format("%.2f", categoryDiscount));
        finalTotalLabel.setText("Final Total: $" + String.format("%.2f", finalTotal));



    }
    private void updateTotalPriceLabel() {
        double totalPrice = shoppingCart.getTotalPrice();
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }






}
class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private List<Product> productList;

    public CustomTableCellRenderer(java.util.List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (row < productList.size()){
            Product product = productList.get(row);
            if (product.getAvailableItems()<=3){
                component.setBackground(Color.red);
                component.setForeground(Color.white);
            }else{
                component.setBackground(Color.white);
                component.setForeground(Color.BLACK);
            }
        }
        return component;
    }
}