package in.scalive.swiftcart.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
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
public class PlaceOrderRequestDTO {
	
	@NotNull(message = "User Id is Required")
	private Long userId;
	
	@Max(value = 500, message = "Notes should not exceed 500 characters")
	private String notes;
}
