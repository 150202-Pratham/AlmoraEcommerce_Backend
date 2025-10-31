package almora.almorafinal.Controller;

import almora.almorafinal.Entities.Order;
import almora.almorafinal.Entities.User;
import almora.almorafinal.Services.OrderService;
import almora.almorafinal.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    private User findUser(String email) {
        return userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 🧾 Place Order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestParam String email,
                                        @RequestParam String address) {
        Order order = orderService.placeOrder(findUser(email), address);
        return ResponseEntity.ok(Map.of("message", "Order placed successfully", "orderId", order.getId()));
    }

    // 📜 Get Order History
    @GetMapping("/history")
    public ResponseEntity<List<Order>> getHistory(@RequestParam String email) {
        return ResponseEntity.ok(orderService.getOrderHistory(findUser(email)));
    }

    // 🔍 Get Single Order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // 🚚 Update Order Status (admin use)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Order.Status status) {
        Order updated = orderService.updateStatus(id, status);
        return ResponseEntity.ok(Map.of("message", "Order status updated", "status", updated.getStatus()));
    }

}
