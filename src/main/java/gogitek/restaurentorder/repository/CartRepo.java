package gogitek.restaurentorder.repository;

import gogitek.restaurentorder.entity.PreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<PreOrder, Long> {
    PreOrder findByIdAndDelete(Long id, boolean delele);
    List<PreOrder> findAllByDelete(boolean delete);
}
