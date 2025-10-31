package almora.almorafinal.Services;

import almora.almorafinal.Entities.Product;
import almora.almorafinal.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product save(Product product) {
        return repo.save(product);
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Optional<Product> getById(Long id) {
        return repo.findById(id);
    }

    public List<Product> getByCategory(Product.Category category) {
        return repo.findByCategory(category);
    }

    public List<Product> getByCategoryAndSubCategory(Product.Category category, String subCategory) {
        return repo.findByCategoryAndSubCategory(category, subCategory);
    }

    public List<Product> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
