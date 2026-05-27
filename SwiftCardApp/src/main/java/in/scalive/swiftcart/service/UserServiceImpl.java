package in.scalive.swiftcart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.scalive.swiftcart.dto.request.UpdateUserRequestDTO;
import in.scalive.swiftcart.dto.request.UserRequestDTO;
import in.scalive.swiftcart.dto.response.UserResponseDTO;
import in.scalive.swiftcart.entity.Cart;
import in.scalive.swiftcart.entity.User;
import in.scalive.swiftcart.exception.DuplicateResourceException;
import in.scalive.swiftcart.exception.ResourceNotFoundException;
import in.scalive.swiftcart.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
	private final UserRepository userRepo;
	
	private UserResponseDTO mapToResponse(User user) {
		return UserResponseDTO.builder()
				.id(user.getId())
				.fullName(user.getFullName())
				.phone(user.getPhone())
				.email(user.getEmail())
				.isActive(user.getIsActive())
				.address(user.getAddress())
				.build();		
	}

	private User findUserById(Long id) {
		Optional<User> opt=userRepo.findById(id);
		if(opt.isPresent()) return opt.get();
		throw new ResourceNotFoundException("User","id",id);
	}

	@Override
	public UserResponseDTO createUser(UserRequestDTO userRequest) {
		if(existsByEmail(userRequest.getEmail())){
			throw new DuplicateResourceException("User","email",userRequest.getEmail());
		}
		User user=User.builder()
				.fullName(userRequest.getFullName())
				.email(userRequest.getEmail())
				.password(userRequest.getPassword())
				.phone(userRequest.getPhone())
				.address(userRequest.getAddress())
				.build();
		Cart cart=Cart.builder()
				.user(user)
				.build();
		user.setCart(cart);
		User savedUser=userRepo.save(user);
		return mapToResponse(savedUser);
	}
	

	@Override
	public UserResponseDTO getUserById(Long id) {
		User user=findUserById(id);
		return mapToResponse(user);
	}

	@Override
	public UserResponseDTO getUserByEmail(String email) {
		Optional<User> opt=userRepo.findByEmail(email);
		if(opt.isPresent()) return mapToResponse(opt.get());
		throw new ResourceNotFoundException("User","email",email);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		List<User> users=userRepo.findAll();
		List<UserResponseDTO> responseList=new ArrayList<>();
		for(User user:users) {
			responseList.add(mapToResponse(user));
		}
		return responseList;
	}

	@Override
	public List<UserResponseDTO> getActiveUsers() {
		List<User> users=userRepo.findByIsActiveTrue();
		List<UserResponseDTO> responseList=new ArrayList<>();
		for(User user:users) {
			responseList.add(mapToResponse(user));
		}
		return responseList;
	}

	@Override
	public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO userRequest) {
		User user=findUserById(id);
		if(userRequest.getFullName()==null &&
				userRequest.getPassword()==null &&
				userRequest.getPhone()==null &&
				userRequest.getAddress()==null &&
				userRequest.getEmail()==null) {
			throw new IllegalArgumentException("At least one field must be provided for update");
		}
		if(userRequest.getFullName()!=null) {
			if(userRequest.getFullName().isBlank()) {
				throw new IllegalArgumentException("Full name cannot be blank");
			}
			user.setFullName(userRequest.getFullName());
		}
		if(userRequest.getEmail()!=null) {
			if(userRequest.getEmail().isBlank()) {
				throw new IllegalArgumentException("Email cannot be blank");
			}
			//checkig existing email
			if(existsByEmail(userRequest.getEmail())){
				throw new DuplicateResourceException("User","email",userRequest.getEmail());
			}
			user.setEmail(userRequest.getEmail());
		}
		if(userRequest.getAddress()!=null) {
			if(userRequest.getAddress().isBlank()) {
				throw new IllegalArgumentException("Address cannot be blank");
			}
			user.setAddress(userRequest.getAddress());
		}
		if(userRequest.getPhone()!=null) {
			if(userRequest.getPhone().isBlank()) {
				throw new IllegalArgumentException("phone number cannot be blank");
			}
			user.setPhone(userRequest.getPhone());
		}
		if(userRequest.getPassword()!=null) {
			if(userRequest.getPassword().isBlank()) {
				throw new IllegalArgumentException("Password cannot be blank");
			}
			user.setPassword(userRequest.getPassword() );
		}
		userRepo.save(user);
		return mapToResponse(user);
		
	}

	@Override
	public void activateUser(Long id) {
		User user=findUserById(id);
		user.setIsActive(true);
		userRepo.save(user);
	}

	@Override
	public void deActivateUser(Long id) {
		User user=findUserById(id);
		user.setIsActive(false);
		userRepo.save(user);
	}

	@Override
	public List<UserResponseDTO> searchUser(String keyword) {
		List<User> users=userRepo.searchByNameOrEmail(keyword);
		List<UserResponseDTO> responseList=new ArrayList<>();
		for(User user:users) {
			responseList.add(mapToResponse(user));
		}
		return responseList;
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}

}
