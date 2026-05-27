package in.scalive.swiftcart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.scalive.swiftcart.entity.CartItem;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartIdAndProductId(Long cartId,Long productId);
	boolean existsByCartIdAndProductId(Long cartId,Long productId);
}
