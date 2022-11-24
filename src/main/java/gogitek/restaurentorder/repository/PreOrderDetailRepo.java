package gogitek.restaurentorder.repository;

import gogitek.restaurentorder.entity.PreOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface PreOrderDetailRepo extends JpaRepository<PreOrderDetail, Long> {
}
