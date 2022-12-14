package gogitek.restaurentorder.repository;

import gogitek.restaurentorder.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    List<Category> findAllByDelete(boolean delete);
}
