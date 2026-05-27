package in.scalive.swiftcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.scalive.swiftcart.entity.OrderItem;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}

