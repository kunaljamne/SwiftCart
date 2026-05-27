package in.scalive.swiftcart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {
	@NotBlank(message = "Product Name is required")
	@Size(min = 2, max = 200, message = "Product Name must be in the range of 2-200 characters")
	private String name;

	@Size(max = 1000, message = "Description must not exceed 1000 characters")
	private String description;

	@NotNull(message = "Product Price is required")
	@Min(value = 0, message = "Price must be greater than or equal to 0")
	private Double price;

	@NotNull(message = "Quantity is required")
	@Min(value = 0, message = "Quantity must be greater than or equal to 0")
	private Integer stockQuantity;

	@Size(max = 100, message = "Category must not exceed 100 characters")
	private String category;

	@Size(max = 100, message = "Brand must not exceed 100 characters")
	private String brand;

	@Size(max = 500, message = "ImageURL must not exceed 500 characters")
	private String imageUrl;

	@Size(max = 50, message = "sku must not exceed 50 characters")
	private String sku;
	private Boolean isAvailable;

}
