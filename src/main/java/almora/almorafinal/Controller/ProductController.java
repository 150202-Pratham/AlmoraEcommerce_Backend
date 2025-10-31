package almora.almorafinal.Controller;

import almora.almorafinal.Entities.Product;
import almora.almorafinal.Services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // ---------- Add New Product ----------
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product) {
        Product saved = service.save(product);
        return ResponseEntity.ok(Map.of("message", "Product added successfully", "product", saved));
    }

    // ---------- Get All Products ----------
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ---------- Get Product by ID ----------
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return service.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Product not found")));
    }

    // ---------- Filter by Category ----------
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Product.Category category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }

    // ---------- Filter by Category + SubCategory ----------
    @GetMapping("/category/{category}/subcategory/{subCategory}")
    public ResponseEntity<List<Product>> getByCategoryAndSubCategory(
            @PathVariable Product.Category category,
            @PathVariable String subCategory
    ) {
        return ResponseEntity.ok(service.getByCategoryAndSubCategory(category, subCategory));
    }

    // ---------- Search by Name ----------
    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(service.search(keyword));
    }

    // ---------- Delete Product ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }
}
