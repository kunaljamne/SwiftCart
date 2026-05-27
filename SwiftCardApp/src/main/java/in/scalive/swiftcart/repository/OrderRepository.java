package in.scalive.swiftcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;   // ✅ correct import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.scalive.swiftcart.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    List<Order> findByStatus(String status);

    // ✅ Fetch order with items by orderId
    @Query("""
        SELECT DISTINCT o 
        FROM Order o
        LEFT JOIN FETCH o.orderItems oi
        LEFT JOIN FETCH oi.product   
        WHERE o.id = :orderId
    """)
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

    // ✅ Fetch order with items by orderNumber
    @Query("""
        SELECT DISTINCT o 
        FROM Order o
        LEFT JOIN FETCH o.orderItems oi
        LEFT JOIN FETCH oi.product  
        WHERE o.orderNumber = :orderNumber
    """)
    Optional<Order> findByOrderNumberWithItems(@Param("orderNumber") String orderNumber);

    // ✅ Search query
    @Query("""
        SELECT o 
        FROM Order o
        WHERE o.orderNumber LIKE CONCAT('%', :keyword, '%')
           OR o.user.fullName LIKE CONCAT('%', :keyword, '%')
           OR o.user.email LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Order> searchByOrder(@Param("keyword") String keyword, Pageable pageable);
}