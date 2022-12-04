package gogitek.restaurentorder.repository;

import gogitek.restaurentorder.constaint.Role;
import gogitek.restaurentorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    @Query("SELECT u from User u where u.email = :email")
    User findUserByEmail(@Param("email") String email);

    @Query(value = "SELECT email from User ", nativeQuery = true)
    List<String> getEmail();

    @Query(value = "select count(*) from User where role = 1", nativeQuery = true)
    Integer countCustomer();
    List<User> getUserByRoleIn(List<Role> role);
    @Query(value = "select count(distinct user_id) " +
            "from user left join orders on user.id = orders.user_id where not (orders.create_at >= :end or orders.create_at <= :start)",nativeQuery = true)
    Integer getTotalUserId(Date start, Date end);
}
