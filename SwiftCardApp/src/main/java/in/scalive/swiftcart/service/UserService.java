package in.scalive.swiftcart.service;

import java.util.List;

import in.scalive.swiftcart.dto.request.UpdateUserRequestDTO;
import in.scalive.swiftcart.dto.request.UserRequestDTO;
import in.scalive.swiftcart.dto.response.UserResponseDTO;

public interface UserService {
	UserResponseDTO createUser(UserRequestDTO userRequestDTO);
	UserResponseDTO getUserById(Long id);
	UserResponseDTO getUserByEmail(String email);
	List<UserResponseDTO> getAllUsers();
	List<UserResponseDTO> getActiveUsers();
	UserResponseDTO updateUser(Long id,UpdateUserRequestDTO userRequest);
	void activateUser(Long id);
	void deActivateUser(Long id);
	List<UserResponseDTO> searchUser(String keyword);
	boolean  existsByEmail(String email);
}
