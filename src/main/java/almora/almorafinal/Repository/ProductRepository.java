package almora.almorafinal.Repository;

import almora.almorafinal.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository <Product, Long> {
    List<Product> findByCategory(Product.Category category);

    List<Product> findByCategoryAndSubCategory(Product.Category category, String subCategory);

    List<Product> findByNameContainingIgnoreCase(String keyword);
}
