package gogitek.restaurentorder.repository;

import gogitek.restaurentorder.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Product getProductsByIdAndDelete(Long id, boolean delete);
    List<Product> getAllByDelete(boolean delete);
}
