package in.scalive.swiftcart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO {

	@Size(min = 2, max = 100, message = "FullName must be between 2 and 100 chars")
	private String fullName;

	@Email(message = "Please provide a valid email address")
	@Size(max = 150, message = "Email must not exceed 150 characters")
	private String email;

	@Size(min = 6, max = 100, message = "password must be between 6 and 100 characters")
	private String password;

	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must of exact 10 digits only")
	private String phone;

	@Size(max = 255, message = "Address must not exceed 255 characters")
	private String address;
}
