package gogitek.restaurentorder.repository;

import gogitek.restaurentorder.entity.PreOrder;
import gogitek.restaurentorder.entity.Product;
import gogitek.restaurentorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<PreOrder, Integer> {

    @Query(value = "SELECT count(*) FROM (SELECT distinct user_id FROM orfarm.cart where is_delete = 0) as template", nativeQuery = true)
    Integer countCart();
}
