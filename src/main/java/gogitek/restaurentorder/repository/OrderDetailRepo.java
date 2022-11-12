package gogitek.restaurentorder.repository;

import gogitek.restaurentorder.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {
    @Query(value = "select *" +
            "from order_detail as od left join orders as o " +
            "on od.order_id = o.id left join product as p " +
            "on od.product_id = p.id order by create_at desc LIMIT 0, 10", nativeQuery = true)
    List<OrderDetail> getTopOrder();

    @Query(value = "select sum(quantity) from order_detail where order_id = ?", nativeQuery = true)
    Integer getTotalProduct(int id);

    @Query(value = "select * from order_detail left join orders on order_detail.order_id=orders.id where orders.status = 3", nativeQuery = true)
    List<OrderDetail> getListRevenueOrder();

    @Query(value = "select sum(quantity) " +
            "from order_detail " +
            "left join orders " +
            "on order_detail.order_id = orders.id where order_id = :id and create_at between :start and :end", nativeQuery = true)
    Integer getTotalProductByFilterAndOrderId(int id, Date start, Date end);

    @Query(value = "select sum(quantity) from order_detail left join orders on order_detail.order_id =orders.id order where order_id = ?1 and status.?2", nativeQuery = true)
    Integer getTotalProductByOrdersIdAndStatus(int id, int status);

    @Query(value = "select distinct order_detail.id, order_detail.product_id,order_detail.price, sum(order_detail.quantity)as quantity, order_detail.order_id " +
            "from order_detail left join orders on order_detail.order_id = orders.id left join product on order_detail.product_id = product.id " +
            "where not (orders.create_at >= :end or orders.create_at <= :start) and orders.status = 3 group by order_detail.product_id", nativeQuery = true)
    List<OrderDetail> findOrderDetailByDay(Date start, Date end);
    @Query(value = "select sum(order_detail.quantity) from order_detail left join orders on order_detail.order_id = orders.id left join product on order_detail.product_id = product.id where not (orders.create_at >= :end or orders.create_at <= :start) and orders.status = 3 and order_detail.product_id = :id", nativeQuery = true)
    Integer getTotalProductByDay(Date start, Date end, int id);
    @Query(value = "select count(orders.id) " +
            "from orders " +
            "where not (orders.create_at >= :end or orders.create_at <= :start)",nativeQuery = true)
    Integer getTotalOrderByDate(Date start, Date end);
}
