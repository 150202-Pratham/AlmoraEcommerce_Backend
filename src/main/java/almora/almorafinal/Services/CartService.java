package almora.almorafinal.Services;

import almora.almorafinal.Entities.Cart;
import almora.almorafinal.Entities.CartItem;
import almora.almorafinal.Entities.Product;
import almora.almorafinal.Entities.User;
import almora.almorafinal.Repository.CartRepository;
import almora.almorafinal.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartService(CartRepository cartRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    public Cart getCart(User user) {
        return cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepo.save(newCart);
        });
    }

    public Cart addItem(User user, Long productId, int quantity) {
        Cart cart = getCart(user);
        Optional<Product> productOpt = productRepo.findById(productId);
        if (productOpt.isEmpty()) throw new RuntimeException("Product not found");

        Product product = productOpt.get();
        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst().orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setSubtotal(existingItem.getQuantity() * product.getPrice());
        } else {
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .subtotal(quantity * product.getPrice())
                    .build();
            cart.getItems().add(newItem);
        }

        cart.recalculateTotal();
        return cartRepo.save(cart);
    }

    public Cart updateItem(User user, Long productId, int quantity) {
        Cart cart = getCart(user);
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                item.setSubtotal(item.getQuantity() * item.getProduct().getPrice());
            }
        }
        cart.recalculateTotal();
        return cartRepo.save(cart);
    }

    public Cart removeItem(User user, Long productId) {
        Cart cart = getCart(user);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        cart.recalculateTotal();
        return cartRepo.save(cart);
    }

    public void clearCart(User user) {
        Cart cart = getCart(user);
        cart.getItems().clear();
        cart.setTotal(0);
        cartRepo.save(cart);
    }
}
