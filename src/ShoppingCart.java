import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private ArrayList<Product> cartItems = new ArrayList<>();
    private List<Product> cart;
    private Map<Product, Integer> quantityMap;
    private double totalPrice = 0.0;

    public ShoppingCart() {
        cartItems = new ArrayList<>();
        quantityMap = new HashMap<>();
    }

    public void addtocart(Product product, int quantity) {
        if (!quantityMap.containsKey(product)) {
            cartItems.add(product);
        }
        quantityMap.put(product, quantityMap.getOrDefault(product, 0) + quantity);
    }


    public double removeItem(Product product) {
        int quantity = quantityMap.getOrDefault(product, 0);
        if (quantity > 1) {
            quantityMap.put(product, quantity - 1);
        } else {
            quantityMap.remove(product);
            cartItems.remove(product);
        }
        product.updateAvailableItems(quantity);
        return getTotalPrice();
    }


    public List<Product> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        totalPrice = 0.0;  // Reset total price to 0 before calculating

        for (Map.Entry<Product, Integer> entry : quantityMap.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            totalPrice += product.getPrice() * quantity;
        }
        return totalPrice;
    }

    public Map<Product, Integer> getQuantityMap() {
        return quantityMap;
    }



    public boolean hasSameCategoryProducts() {
        if (cartItems.size() < 2) {
            // If there are fewer than 2 items, can't have products from the same category
            return false;
        }

        String firstCategory = cartItems.get(0).getProductType();
        for (int i = 1; i < cartItems.size(); i++) {
            if (!firstCategory.equals(cartItems.get(i).getProductType())) {
                return false;
            }
        }

        return true;
    }


}