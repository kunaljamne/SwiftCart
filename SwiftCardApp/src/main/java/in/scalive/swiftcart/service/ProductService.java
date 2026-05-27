package in.scalive.swiftcart.service;

import java.util.List;

import in.scalive.swiftcart.dto.request.ProductRequestDTO;
import in.scalive.swiftcart.dto.request.UpdateProductRequestDTO;
import in.scalive.swiftcart.dto.response.PageResponseDTO;
import in.scalive.swiftcart.dto.response.ProductResponseDTO;

public interface ProductService {
	public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
	public ProductResponseDTO getProductById(Long id);
	public ProductResponseDTO getProductBySku(String sku);
	public List<ProductResponseDTO> getAllProducts();
	public PageResponseDTO<ProductResponseDTO> getAllProductsPaginated(int page,int size,String sortBy,String sortDir);
	public List<ProductResponseDTO> getAvailableProducts();
	public List<ProductResponseDTO> getProductsByCategory(String category);
	public List<ProductResponseDTO> getProductsByPriceRange(double minPrice,double maxPrice);
	public PageResponseDTO<ProductResponseDTO> searchProducts(String keyword,int page,int size);
	public ProductResponseDTO updateProduct(Long id,UpdateProductRequestDTO updateProductRequestDTO);
	public void updateStock(Long productId,Integer quantity);
	public List<ProductResponseDTO> getLowStockProducts(Integer threshold);
	public boolean existsBySku(String sku);
}
