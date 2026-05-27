package in.scalive.swiftcart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.swiftcart.dto.request.ProductRequestDTO;
import in.scalive.swiftcart.dto.request.UpdateProductRequestDTO;
import in.scalive.swiftcart.dto.response.ApiResponseDTO;
import in.scalive.swiftcart.dto.response.PageResponseDTO;
import in.scalive.swiftcart.dto.response.ProductResponseDTO;
import in.scalive.swiftcart.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
	private final ProductService serv;

	@PostMapping("/add")
	public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
		ProductResponseDTO product = serv.createProduct(dto);
		return new ResponseEntity<>(ApiResponseDTO.success("Product Created Successfully", product),
				HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getProductById(@PathVariable("id") Long id) {
		ProductResponseDTO productById = serv.getProductById(id);
		ApiResponseDTO<ProductResponseDTO> responseDTO = ApiResponseDTO.success(productById);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/sku/{sku}")
	public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getProductBySku(@PathVariable String sku) {
		ProductResponseDTO productBySku = serv.getProductBySku(sku);
		ApiResponseDTO<ProductResponseDTO> responseDTO = ApiResponseDTO.success(productBySku);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getAllProducts() {
		List<ProductResponseDTO> allProducts = serv.getAllProducts();
		ApiResponseDTO<List<ProductResponseDTO>> responseDTO = ApiResponseDTO
				.success("Fetched " + allProducts.size() + " Products", allProducts);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping
	public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> getAllProductsPaginated(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {
		PageResponseDTO<ProductResponseDTO> products = serv.getAllProductsPaginated(page, size, sortBy, sortDir);
		ApiResponseDTO<PageResponseDTO<ProductResponseDTO>> responseDTO = ApiResponseDTO.success(products);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/available")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getAvailableProducts() {
		List<ProductResponseDTO> allProducts = serv.getAvailableProducts();
		ApiResponseDTO<List<ProductResponseDTO>> responseDTO = ApiResponseDTO.success(allProducts);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/category/{category}")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getProductsByCategory(
			@PathVariable String category) {
		List<ProductResponseDTO> products = serv.getProductsByCategory(category);
		ApiResponseDTO<List<ProductResponseDTO>> responseDTO = ApiResponseDTO.success(products);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/price-range")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getProductsByPriceRange(@RequestParam double min,
			@RequestParam double max) {
		List<ProductResponseDTO> products = serv.getProductsByPriceRange(min, max);
		ApiResponseDTO<List<ProductResponseDTO>> responseDTO = ApiResponseDTO.success(products);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> searchProducts(
			@RequestParam String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		PageResponseDTO<ProductResponseDTO> products = serv.searchProducts(keyword, page, size);
		return ResponseEntity.ok(ApiResponseDTO.success(products));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> updateProducts(@PathVariable Long id,
			@Valid @RequestBody UpdateProductRequestDTO updateProductRequestDTO) {
		ProductResponseDTO product = serv.updateProduct(id, updateProductRequestDTO);
		return ResponseEntity.ok(ApiResponseDTO.success("Product Updated successfully", product));
	}

	@PatchMapping("/{id}/stock")
	public ResponseEntity<ApiResponseDTO<Void>> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
		serv.updateStock(id, quantity);
		return ResponseEntity.ok(ApiResponseDTO.success("Stock Updated successfully"));
	}

	@GetMapping("/low-stock")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getLowStockProducts(
			@RequestParam(defaultValue = "10") Integer threshold) {
		List<ProductResponseDTO> allProducts = serv.getLowStockProducts(threshold);
		ApiResponseDTO<List<ProductResponseDTO>> responseDTO = ApiResponseDTO.success(allProducts);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/check-sku/{sku}")
	public ResponseEntity<ApiResponseDTO<Boolean>> checkSkuExists(@PathVariable String sku) {
		boolean exists = serv.existsBySku(sku);
		return ResponseEntity.ok(ApiResponseDTO.success(exists ? "sku Already exists" : "SKU is available", exists));
	}

}
