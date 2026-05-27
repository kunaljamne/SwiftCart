package in.scalive.swiftcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.scalive.swiftcart.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	public Optional<Product> findBySku(String sku);

	public boolean existsBySku(String sku);

	public List<Product> findByIsAvailableTrue();

	public List<Product> findByCategeoryIgnoreCase(String categeory);

	public List<Product> findByPriceBetween(double minPrice, double maxPrice);

	@Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%')) "
			+ "OR LOWER(p.description) LIKE LOWER(CONCAT('%',:keyword,'%'))) AND p.isAvailable=true")
	public Page<Product> searchProduct(@Param("keyword") String keyword, Pageable pageable);

	@Query("Update Product p Set p.stockQuantity=p.stockQuantity+:quantity where p.id=:productId ")
	@Modifying
	int increaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

	@Query("Update Product p Set p.stockQuantity=p.stockQuantity-:quantity where p.id=:productId AND p.stockQuantity>=:quantity")
	@Modifying
	int decreaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

	@Query("select p from Product p where p.stockQuantity <= :threshold And p.isAvailable=true")
	List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
}
