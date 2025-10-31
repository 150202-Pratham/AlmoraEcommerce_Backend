package almora.almorafinal.Services;

import almora.almorafinal.Entities.Cart;
import almora.almorafinal.Entities.Order;
import almora.almorafinal.Entities.OrderItem;
import almora.almorafinal.Entities.User;
import almora.almorafinal.Repository.CartRepository;
import almora.almorafinal.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final EmailService emailService;



    public OrderService(OrderRepository orderRepo, CartRepository cartRepo, EmailService emailService) {
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.emailService = emailService;
    }

    public Order placeOrder(User user, String shippingAddress) {
        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(item -> OrderItem.builder()
                        .product(item.getProduct())
                        .quantity(item.getQuantity())
                        .price(item.getProduct().getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        // Create Order
        Order order = Order.builder()
                .user(user)
                .items(orderItems)
                .total(cart.getTotal())
                .shippingAddress(shippingAddress)
                .status(Order.Status.PLACED)
                .build();


        double total = order.getItems()
                .stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        order.setTotalPrice(total);



        // Save Order and clear Cart
        Order savedOrder = orderRepo.save(order);
        if (order.getUser() != null && order.getUser().getEmail() != null) {
            emailService.sendOrderConfirmation(
                    order.getUser().getEmail(),
                    String.valueOf(savedOrder.getId()),
                    savedOrder.getTotalPrice()
            );
        }
        cart.getItems().clear();
        cart.setTotal(0);
        cartRepo.save(cart);

        return savedOrder;
    }

    public List<Order> getOrderHistory(User user) {
        return orderRepo.findByUser(user);
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order updateStatus(Long orderId, Order.Status status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepo.save(order);
    }
}
