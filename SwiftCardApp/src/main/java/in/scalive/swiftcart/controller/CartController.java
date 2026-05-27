package in.scalive.swiftcart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.swiftcart.dto.request.AddToCartRequestDTO;
import in.scalive.swiftcart.dto.request.UpdateCartItemRequestDTO;
import in.scalive.swiftcart.dto.response.ApiResponseDTO;
import in.scalive.swiftcart.dto.response.CartItemResponseDTO;
import in.scalive.swiftcart.dto.response.CartResponseDTO;
import in.scalive.swiftcart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {
	private final CartService serv;

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> getCart(@PathVariable Long userId) {
		CartResponseDTO dto = serv.getCartByUserId(userId);
		return ResponseEntity.ok(ApiResponseDTO.success(dto));
	}

	@PostMapping("/{userId}/addItems")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> addItemToCart(@PathVariable Long userId,
			@Valid @RequestBody AddToCartRequestDTO dto) {
		CartResponseDTO cartDto = serv.addItemToCart(userId, dto);
		return ResponseEntity.ok(ApiResponseDTO.success("Item Added successfully ", cartDto));
	}

	@PutMapping("/{userId}/items/{cartItemId}")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> updateCartItem(@PathVariable Long userId,
			@PathVariable Long cartItemId, @Valid @RequestBody UpdateCartItemRequestDTO dto) {
		CartResponseDTO cartDto = serv.updateCartItem(userId, cartItemId, dto);
		return ResponseEntity.ok(ApiResponseDTO.success("Item updated successfully ", cartDto));
	}

	@DeleteMapping("/{userId}/items/{cartItemId}")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> removeItemFromCart(@PathVariable Long userId,
			@PathVariable Long cartItemId) {
		CartResponseDTO cartDto = serv.removeItemFromCart(userId, cartItemId);
		return ResponseEntity.ok(ApiResponseDTO.success("Item Removed successfully ", cartDto));
	}

	@DeleteMapping("/{userId}/clear")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> clearCart(@PathVariable Long userId) {
		CartResponseDTO cartDto = serv.clearCart(userId);
		return ResponseEntity.ok(ApiResponseDTO.success("Item cleared successfully ", cartDto));
	}

	@GetMapping("/{userId}/items/{cartItemId}")
	public ResponseEntity<ApiResponseDTO<CartItemResponseDTO>> getCartItem(@PathVariable Long userId,
			@PathVariable Long cartItemId) {
		CartItemResponseDTO dto = serv.getCartItem(userId, cartItemId);
		return ResponseEntity.ok(ApiResponseDTO.success(dto));
	}

	@GetMapping("/{userId}/check-product/{productId}")
	public ResponseEntity<ApiResponseDTO<Boolean>> isProductInCart(@PathVariable Long userId,
			@PathVariable Long productId) {
		boolean productInCart = serv.isProductInCart(userId, productId);
		return ResponseEntity.ok(ApiResponseDTO
				.success(productInCart ? "Product is in the cart" : "Product is not in the cart", productInCart));
	}

	@PatchMapping("/{userId}/products/{productId}/increment")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> incrementItemQuantity(@PathVariable Long userId,
			@PathVariable Long productId, @RequestParam(defaultValue = "1") Integer quantity) {
		CartResponseDTO dto = serv.incrementItemQuantity(userId, productId, quantity);
		return ResponseEntity.ok(ApiResponseDTO.success("Quantity Increased successfully ", dto));
	}

	@PatchMapping("/{userId}/products/{productId}/decrement")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> decrementItemQuantity(@PathVariable Long userId,
			@PathVariable Long productId, @RequestParam(defaultValue = "1") Integer quantity) {
		CartResponseDTO dto = serv.decrementItemQuantity(userId, productId, quantity);
		return ResponseEntity.ok(ApiResponseDTO.success("Quantity decreased successfully ", dto));
	}

}
