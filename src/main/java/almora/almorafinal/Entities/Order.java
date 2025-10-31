package almora.almorafinal.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private double total;

    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PLACED;

    private LocalDateTime orderDate = LocalDateTime.now();
    private double totalPrice;
    public enum Status {
        PLACED,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
}
