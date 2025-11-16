package almora.almorafinal.Controller;

import almora.almorafinal.DTO.ProductDTO;
import almora.almorafinal.Entities.Product;
import almora.almorafinal.Services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService service;



    // ---------- Add New Product ----------
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product) {

        return ResponseEntity.ok(service.addProduct(product));
    }

    // ---------- Get All Products ----------
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    // ---------- Get Product by ID ----------
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductById(id)) ;

    }

    // ---------- Filter by Category ----------
    @GetMapping(params = "category")
    public ResponseEntity<List<ProductDTO>> getByCategory(@RequestParam Product.Category category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }

    // ---------- Filter by Category + SubCategory ----------
    @GetMapping("/category/{category}/subcategory/{subCategory}")
    public ResponseEntity<List<ProductDTO>> getByCategoryAndSubCategory(
            @PathVariable Product.Category category,
            @PathVariable String subCategory
    ) {
        return ResponseEntity.ok(service.getByCategoryAndSubCategory(category, subCategory));
    }

    // ---------- Search by Name ----------
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(service.search(keyword));
    }
    //-----------Update Product -------------
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return ResponseEntity.ok(service.upDateProduct(id, updatedProduct));
    }

    // ---------- Delete Product ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }
}
