package in.scalive.swiftcart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.scalive.swiftcart.dto.request.ProductRequestDTO;
import in.scalive.swiftcart.dto.request.UpdateProductRequestDTO;
import in.scalive.swiftcart.dto.response.PageResponseDTO;
import in.scalive.swiftcart.dto.response.ProductResponseDTO;
import in.scalive.swiftcart.entity.Product;
import in.scalive.swiftcart.exception.DuplicateResourceException;
import in.scalive.swiftcart.exception.ResourceNotFoundException;
import in.scalive.swiftcart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repo;

	@Override
	public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
		// TODO Auto-generated method stub
		if(productRequestDTO.getSku()!=null && repo.existsBySku(productRequestDTO.getSku())) {
			throw new DuplicateResourceException("Product","Sku",productRequestDTO.getSku());
		}
		
		Product product=Product.builder()
				.name(productRequestDTO.getName())
				.description(productRequestDTO.getDescription())
				.price(productRequestDTO.getPrice())
				.stockQuantity(productRequestDTO.getStockQuantity())
				.categeory(productRequestDTO.getCategory())
				.brand(productRequestDTO.getBrand())
				.imageUrl(productRequestDTO.getImageUrl())
				.sku(productRequestDTO.getSku())
				.isAvailable(productRequestDTO.getIsAvailable()!=null ? productRequestDTO.getIsAvailable():true)
				.build();
		Product savedProduct= repo.save(product);
		return mapToProductResponseDTO(savedProduct);
	}

	@Override
	public ProductResponseDTO getProductById(Long id) {
		Product product=findProductById(id);
		return mapToProductResponseDTO(product);
	}

	@Override
	public ProductResponseDTO getProductBySku(String sku) {
		Optional<Product> optional=repo.findBySku(sku);
		if(optional.isPresent()) {
			return mapToProductResponseDTO(optional.get());
		}
		throw new ResourceNotFoundException("Product","sku",sku);
	}

	@Override
	public List<ProductResponseDTO> getAllProducts() {
		List<Product> products=repo.findAll();
		List<ProductResponseDTO> list=new ArrayList<>();;
		for(Product pro:products) {
			list.add(mapToProductResponseDTO(pro));
		}
		return list;
	}

	@Override
	public PageResponseDTO<ProductResponseDTO> getAllProductsPaginated(int page, int size, String sortBy,
			String sortDir) {
		Pageable pageable=createPageable(page, size, sortBy, sortDir);
		Page<Product> productPage = repo.findAll(pageable);
		return mapToPageResponse(productPage);
		
	}

	@Override
	public List<ProductResponseDTO> getAvailableProducts() {
		List<Product> availableProducts = repo.findByIsAvailableTrue();
		List<ProductResponseDTO> list=new ArrayList<>();;
		for(Product pro:availableProducts) {
			list.add(mapToProductResponseDTO(pro));
		}
		return list;
	}

	@Override
	public List<ProductResponseDTO> getProductsByCategory(String category) {
		List<Product> categorWiseProducts = repo.findByCategeoryIgnoreCase(category);
		List<ProductResponseDTO> list=new ArrayList<>();;
		for(Product pro:categorWiseProducts) {
			list.add(mapToProductResponseDTO(pro));
		}
		return list;
	}

	@Override
	public List<ProductResponseDTO> getProductsByPriceRange(double minPrice, double maxPrice) {
		List<Product> priceBetweenProducts = repo.findByPriceBetween(minPrice, maxPrice);
		List<ProductResponseDTO> list=new ArrayList<>();;
		for(Product pro:priceBetweenProducts) {
			list.add(mapToProductResponseDTO(pro));
		}
		return list;
	}

	@Override
	public PageResponseDTO<ProductResponseDTO> searchProducts(String keyword, int page, int size) {
		Pageable pageable=PageRequest.of(page, size);
		Page<Product> searchProduct = repo.searchProduct(keyword, pageable);
		return mapToPageResponse(searchProduct);
	}

	@Override
	public ProductResponseDTO updateProduct(Long id, UpdateProductRequestDTO updateProductRequestDTO) {
		Product product = findProductById(id);
		if(updateProductRequestDTO.getName()==null 
				&& updateProductRequestDTO.getDescription()==null
				&& updateProductRequestDTO.getPrice()==null
				&& updateProductRequestDTO.getStockQuantity()==null
				&& updateProductRequestDTO.getCategory()==null
				&& updateProductRequestDTO.getBrand()==null
				&& updateProductRequestDTO.getImageUrl()==null
				&& updateProductRequestDTO.getSku()==null
				&& updateProductRequestDTO.getIsAvailable()==null) {
			throw new IllegalArgumentException("At Least one field should be present for updation");
		}
		
		if(updateProductRequestDTO.getName()!=null) {
			if(updateProductRequestDTO.getName().isBlank()) {
				throw new IllegalArgumentException("Product Name cannot be left blank");
			}
			product.setName(updateProductRequestDTO.getName().trim());
		}
		
		if(updateProductRequestDTO.getDescription()!=null) {
			if(updateProductRequestDTO.getDescription().isBlank()) {
				throw new IllegalArgumentException("Product Desciption cannot be left blank");
			}
			product.setDescription(updateProductRequestDTO.getDescription().trim());
		}
		
		if(updateProductRequestDTO.getPrice()!=null) {
			if(updateProductRequestDTO.getPrice()<0) {
				throw new IllegalArgumentException("Product Price cannot be Negative");
			}
			product.setPrice(updateProductRequestDTO.getPrice());
		}
		
		if(updateProductRequestDTO.getCategory()!=null) {
			if(updateProductRequestDTO.getCategory().isBlank()) {
				throw new IllegalArgumentException("Product Category cannot be left blank");
			}
			product.setStockQuantity(updateProductRequestDTO.getStockQuantity());
		}
		
		if(updateProductRequestDTO.getBrand()!=null) {
			if(updateProductRequestDTO.getBrand().isBlank()) {
				throw new IllegalArgumentException("Product Brand cannot be left blank");
			}
			product.setBrand(updateProductRequestDTO.getBrand().trim());
		}
		
		if(updateProductRequestDTO.getImageUrl()!=null) {
			if(updateProductRequestDTO.getImageUrl().isBlank()) {
				throw new IllegalArgumentException("Product ImageURL cannot be left blank");
			}
			product.setImageUrl(updateProductRequestDTO.getImageUrl().trim());
		}
		
		if(updateProductRequestDTO.getStockQuantity()!=null) {
			if(updateProductRequestDTO.getStockQuantity()<0) {
				throw new IllegalArgumentException("Product Quantity cannot be Negative");
			}
			product.setPrice(updateProductRequestDTO.getPrice());
		}
		
		if(updateProductRequestDTO.getSku()!=null) {
			if(updateProductRequestDTO.getSku().isBlank()) {
				throw new IllegalArgumentException("Product Sku cannot be Negative");
			}
			
			String sku=updateProductRequestDTO.getSku();
			if(!sku.equals(product.getSku())
					&& repo.existsBySku(sku)) {
				throw new DuplicateResourceException("Product","Sku",sku);
			}
			product.setSku(sku);
		}
		
		if(updateProductRequestDTO.getIsAvailable()!=null) {
			
			product.setIsAvailable(updateProductRequestDTO.getIsAvailable());
		}
		
		
		if(updateProductRequestDTO.getSku()!=null
				&& !updateProductRequestDTO.getSku().equals(product.getSku())
				&& repo.existsBySku(updateProductRequestDTO.getSku())) {
			throw new DuplicateResourceException("Product","Sku",updateProductRequestDTO.getSku());
		}
		
		Product updatedProduct=repo.save(product);
		return mapToProductResponseDTO(updatedProduct);
	}

	@Override
	public void updateStock(Long productId, Integer quantity) {
		Product product = findProductById(productId);
		int newQuantity=product.getStockQuantity()+quantity;
		if(newQuantity<0) {
			throw new IllegalArgumentException("Stock Quantity cannot be negative,current stock:"+product.getStockQuantity());
		}
		product.setStockQuantity(newQuantity);
		repo.save(product);
	}

	@Override
	public List<ProductResponseDTO> getLowStockProducts(Integer threshold) {
		if(threshold<0) {
			throw new IllegalArgumentException("Threshold cannot be negative:"+threshold);
		}
		List<Product> products = repo.findLowStockProducts(threshold);
		List<ProductResponseDTO> list=new ArrayList<>();;
		for(Product pro:products) {
			list.add(mapToProductResponseDTO(pro));
		}
		return list;
	}

	@Override
	public boolean existsBySku(String sku) {
		return repo.existsBySku(sku);
	}

	private Product findProductById(Long id) {
		Optional<Product> opt = repo.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new ResourceNotFoundException("Product", "id", id);
	}

	private Pageable createPageable(int page, int size, String sortBy, String sortDir) {
		Sort sort;
		if (sortDir.equalsIgnoreCase("desc"))
			sort = Sort.by(sortBy).descending();
		else
			sort = Sort.by(sortBy).ascending();
		return PageRequest.of(page, size, sort);
	}

	private ProductResponseDTO mapToProductResponseDTO(Product product) {
		return ProductResponseDTO.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.stockQuantity(product.getStockQuantity())
				.category(product.getCategeory())
				.brand(product.getBrand())
				.imageUrl(product.getImageUrl())
				.sku(product.getSku())
				.isAvailable(product.getIsAvailable())
				.inStock(product.getStockQuantity()>0)
				.build();
	}
	
	private PageResponseDTO<ProductResponseDTO> mapToPageResponse(Page<Product> productPage) {
		List<ProductResponseDTO> products=new ArrayList<>();
		for(Product product:productPage.getContent()){
			products.add( mapToProductResponseDTO(product));
		}
		return PageResponseDTO.<ProductResponseDTO>builder()
				.content(products)
				.pageNumber(productPage.getNumber())
				.pageSize(productPage.getSize())
				.totalElements(productPage.getTotalElements())
				.totalPages(productPage.getTotalPages())
				.first(productPage.isFirst())
				.last(productPage.isLast())
				.hasNext(productPage.hasNext())
				.hasPrevious(productPage.hasPrevious())
				.build();
	}
}
