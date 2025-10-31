package almora.almorafinal.Controller;

import almora.almorafinal.Entities.Cart;
import almora.almorafinal.Entities.User;
import almora.almorafinal.Services.CartService;
import almora.almorafinal.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {


    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // For now we use email to simulate logged-in user
    private User findUser(String email) {
        return userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<Cart> viewCart(@RequestParam String email) {
        return ResponseEntity.ok(cartService.getCart(findUser(email)));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestParam String email,
                                     @RequestParam Long productId,
                                     @RequestParam int quantity) {
        Cart updated = cartService.addItem(findUser(email), productId, quantity);
        return ResponseEntity.ok(Map.of("message", "Item added", "cart", updated));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateItem(@RequestParam String email,
                                        @RequestParam Long productId,
                                        @RequestParam int quantity) {
        Cart updated = cartService.updateItem(findUser(email), productId, quantity);
        return ResponseEntity.ok(Map.of("message", "Item updated", "cart", updated));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItem(@RequestParam String email,
                                        @RequestParam Long productId) {
        Cart updated = cartService.removeItem(findUser(email), productId);
        return ResponseEntity.ok(Map.of("message", "Item removed", "cart", updated));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam String email) {
        cartService.clearCart(findUser(email));
        return ResponseEntity.ok(Map.of("message", "Cart cleared"));
    }
}
