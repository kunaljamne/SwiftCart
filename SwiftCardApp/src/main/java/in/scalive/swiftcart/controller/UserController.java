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

import in.scalive.swiftcart.dto.request.UpdateUserRequestDTO;
import in.scalive.swiftcart.dto.request.UserRequestDTO;
import in.scalive.swiftcart.dto.response.ApiResponseDTO;
import in.scalive.swiftcart.dto.response.UserResponseDTO;
import in.scalive.swiftcart.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
	private final UserService serv;

	@PostMapping
	public ResponseEntity<ApiResponseDTO<UserResponseDTO>> createUser(@Valid @RequestBody UserRequestDTO user) {
		UserResponseDTO dto = serv.createUser(user);
		ApiResponseDTO<UserResponseDTO> obj = ApiResponseDTO.success("USer created successfully", dto);
		return new ResponseEntity<>(obj, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(@PathVariable Long id) {
		UserResponseDTO dto = serv.getUserById(id);
		ApiResponseDTO<UserResponseDTO> obj = ApiResponseDTO.success(dto);
		return ResponseEntity.ok(obj);
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByEmail(@PathVariable String email) {
		UserResponseDTO dto = serv.getUserByEmail(email);
		ApiResponseDTO<UserResponseDTO> obj = ApiResponseDTO.success(dto);
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUsers() {
		List<UserResponseDTO> users = serv.getAllUsers();
		ApiResponseDTO<List<UserResponseDTO>> obj = ApiResponseDTO.success(users);
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping("/active")
	public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getActiveUsers() {
		List<UserResponseDTO> users = serv.getActiveUsers();
		ApiResponseDTO<List<UserResponseDTO>> obj = ApiResponseDTO.success(users);
		return ResponseEntity.ok(obj);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUser(@PathVariable Long id,@Valid @RequestBody UpdateUserRequestDTO userRequestDTO) {
		UserResponseDTO dto = serv.updateUser(id, userRequestDTO);
		ApiResponseDTO<UserResponseDTO> obj = ApiResponseDTO.success("USer updated successfully", dto);
		return ResponseEntity.ok(obj);
	}
	
	@PatchMapping("/{id}/activate")
	public ResponseEntity<ApiResponseDTO<Void>> activatedUser(@PathVariable Long id) {
		serv.activateUser(id);
		return ResponseEntity.ok(ApiResponseDTO.success("USer Activated successfully"));
	}
	
	@PatchMapping("/{id}/deactivate")
	public ResponseEntity<ApiResponseDTO<Void>> deActivatedUser(@PathVariable Long id) {
		serv.deActivateUser(id);
		return ResponseEntity.ok(ApiResponseDTO.success("USer De-Activated successfully"));
	}
	
	@GetMapping("/search")
	public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> searchUsers(@RequestParam String keyword) {
		List<UserResponseDTO> users = serv.searchUser(keyword);
		ApiResponseDTO<List<UserResponseDTO>> obj = ApiResponseDTO.success(users);
		return ResponseEntity.ok(obj);
	}
	@GetMapping("/check-email")
	public ResponseEntity<ApiResponseDTO<Boolean>> checkEmailExists(@RequestParam String email) {
		Boolean res = serv.existsByEmail(email);
		ApiResponseDTO<Boolean> obj = ApiResponseDTO.success(res?"Email already exists":"Email is available for use");
		return ResponseEntity.ok(obj);
	}
	
}
