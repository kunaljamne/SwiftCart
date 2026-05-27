package in.scalive.swiftcart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 200)
	private String name;
	@Column(length = 1000)
	private String description;
	@Column(nullable = false)
	private double price;
	@Builder.Default
	private Integer stockQuantity=0;
	@Column(length = 100)
	private String categeory;
	@Column(length = 100)
	private String brand;
	@Column(length = 500)
	private String imageUrl;
	
	@Column(unique = true,length = 50)
	private String sku;//stock keeping unit
	@Column(nullable = false,length = 50)
	@Builder.Default
	private Boolean isAvailable=true;
	
}
