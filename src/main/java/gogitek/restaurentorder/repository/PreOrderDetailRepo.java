package gogitek.restaurentorder.repository;

import gogitek.restaurentorder.constaint.Status;
import gogitek.restaurentorder.entity.PreOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PreOrderDetailRepo extends JpaRepository<PreOrderDetail, Long> {
    List<PreOrderDetail> findByStatusIn(List<Status> status);
}
